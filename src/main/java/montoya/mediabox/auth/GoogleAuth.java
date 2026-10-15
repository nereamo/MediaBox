package montoya.mediabox.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Gestiona la autenticación con Google mediante OAuth 2.0 para la aplicación de escritorio.
 * Lee las credenciales de forma segura desde el archivo de configuración interno
 * y maneja el flujo de inicio de sesión interactivo y automático.
 * 
 * @author Nerea
 */
public class GoogleAuth {

    private static final String CLIENT_ID;
    private static final String CLIENT_SECRET;
    
    static {
        Properties prop = new Properties();
        try (InputStream input = GoogleAuth.class.getResourceAsStream("/properties/config.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo /properties/config.properties");
            }
            prop.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar las credenciales de Google", e);
        }
        CLIENT_ID = prop.getProperty("google.client.id");
        CLIENT_SECRET = prop.getProperty("google.client.secret");
    }

    static final String localAppData = System.getenv("LOCALAPPDATA");
    static final File credentialsFolder = new File(localAppData, "MediaBox/google-auth");

    /**
     * Segundos máximos esperando a que el usuario termine en el navegador
     */
    private static final int TIMEOUT_SECONDS = 120;

    /**
     * Login con Google (OAuth 2.0 para aplicaciones de escritorio).
     * <p>
     * Abre el navegador, el usuario inicia sesión y se obtiene su email.
     * Los tokens de acceso se almacenan de forma segura localmente en AppData.
     * 
     * @return El correo electrónico verificado de la cuenta de Google del usuario.
     * @throws Exception Si ocurre algún error de red, si expira el tiempo de espera o si Google no devuelve un email válido.
     */
    public static String signIn() throws Exception {

        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory json = GsonFactory.getDefaultInstance();

        GoogleClientSecrets secrets = new GoogleClientSecrets().setInstalled(
                new GoogleClientSecrets.Details()
                        .setClientId(CLIENT_ID)
                        .setClientSecret(CLIENT_SECRET));

        //Si la carpeta de las credenciales no existe, la crea
        if (!credentialsFolder.exists()) {
            credentialsFolder.mkdirs();
        }

        FileDataStoreFactory dataStore = new FileDataStoreFactory(credentialsFolder);

        GoogleAuthorizationCodeFlow flow = (GoogleAuthorizationCodeFlow) new GoogleAuthorizationCodeFlow.Builder(
                transport, json, secrets, List.of("openid", "email"))
                .enablePKCE()
                .setDataStoreFactory(dataStore)
                .build();

        final LocalServerReceiver receiver = new LocalServerReceiver.Builder().build();

        //Si el usuario cierra el navegador sin terminar, se corta la espera
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_SECONDS * 1000L);
                receiver.stop();
            } catch (Exception ignored) {
            }
        });
        watchdog.setDaemon(true);
        watchdog.start();

        Credential cred;
        try {
            cred = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } finally {
            watchdog.interrupt();
        }

        //Pide el email a Google con el token recién obtenido
        HttpRequest req = transport.createRequestFactory(cred)
                .buildGetRequest(new GenericUrl("https://www.googleapis.com/oauth2/v3/userinfo"));
        req.setParser(new JsonObjectParser(json));
        GenericJson info = req.execute().parseAs(GenericJson.class);

        // Valida que el correo electrónico esté verificado por Google
        if (!Boolean.TRUE.equals(info.get("email_verified"))) {
            throw new IllegalStateException("The Google email is not verified");
        }

        String email = (String) info.get("email");

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalStateException("Google did not return an email");
        }

        return email;
    }

    /**
     * Intenta autenticar automáticamente al usuario utilizando los tokens guardados localmente
     * en el almacenamiento de la aplicación, evitando abrir el navegador de nuevo si ya hay sesión.
     *
     * @return El correo electrónico del usuario si la sesión es válida o se pudo refrescar; o {@code null} si no existe sesión previa.
     * @throws Exception Si ocurre un error crítico durante la validación de la sesión o de red.
     */
    public static String autoSignIn() throws Exception {

        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory json = GsonFactory.getDefaultInstance();

        File localAppData = new File(System.getenv("LOCALAPPDATA"));
        File credentialsFolder = new File(localAppData, "MediaBox/google-auth");

        FileDataStoreFactory dataStore = new FileDataStoreFactory(credentialsFolder);

        GoogleClientSecrets secrets = new GoogleClientSecrets().setInstalled(
                new GoogleClientSecrets.Details()
                        .setClientId(CLIENT_ID)
                        .setClientSecret(CLIENT_SECRET));

        GoogleAuthorizationCodeFlow flow = (GoogleAuthorizationCodeFlow) new GoogleAuthorizationCodeFlow.Builder(
                transport, json, secrets, List.of("openid", "email"))
                .enablePKCE()
                .setDataStoreFactory(dataStore)
                .build();

        // Carga las credenciales guardadas previamente en disco
        Credential cred = flow.loadCredential("user");

        if (cred == null) {
            return null;
        }

        // Comprueba que tenemos un token de acceso válido o renovable
        if (cred.getAccessToken() == null
                || cred.getExpiresInSeconds() == null
                || cred.getExpiresInSeconds() <= 60) {

            if (!cred.refreshToken()) {
                return null;
            }
        }

        // Realiza la petición a la API de Google para obtener la información del usuario autenticado
        HttpRequest req = transport.createRequestFactory(cred)
                .buildGetRequest(new GenericUrl("https://www.googleapis.com/oauth2/v3/userinfo"));

        req.setParser(new JsonObjectParser(json));

        GenericJson info = req.execute().parseAs(GenericJson.class);

        if (!Boolean.TRUE.equals(info.get("email_verified"))) {
            return null;
        }

        String email = (String) info.get("email");

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        return email;
    }

}
