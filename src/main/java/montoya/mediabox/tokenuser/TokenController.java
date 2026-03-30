package montoya.mediabox.tokenuser;

import montoya.mediabox.utils.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import tools.jackson.databind.ObjectMapper;

/**
 * Gestiona el almacenamiento del token.
 * 
 * <p>Permite:
 * <ul>
 * <li> Guardar el token del usuario al loguearse por primera vez </li>
 * <li> Leer el token para abrir la aplicación automaticamente </li>
 * <li> Eliminar el token del archivo donde se almacenan </li>
 * </ul>
 * 
 * @author Nerea
 */
public class TokenController {
    
    /** Ruta del archiv JSON */
    private static final Path FOLDER_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "MediaBox");
    private static final Path JSON_PATH = FOLDER_PATH.resolve("token.json");
    
     /** Objeto utilizado para serializar y deserializar JSON */
    private static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Guarda el token (llave de autenticación) en un archivo JSON
     * 
     * @param token Identificador único de autenticación del usuario
     * @param email Email con el que se registra el usuario
     */
    public static void saveToken(String token, String email){
        try {
            Files.createDirectories(FOLDER_PATH);
            TokenUser tu = new TokenUser(token, email);
            mapper.writeValue(JSON_PATH.toFile(), tu);
            System.out.println("Saved token in: " + JSON_PATH.toAbsolutePath());
            Logger.logError("Token saved successfully at: " + JSON_PATH.toAbsolutePath(), null);
        } catch (IOException e) {
            Logger.logError("Error saving token for email: " + email, e);
        }
    }
    
    /**
     * Lee el token (llave de autenticación) almacenado en JSON.
     *
     * @return Objeto {@link TokenUser} en el token y email almacenado o {@code null} si el JSON no existe
     */
    public static TokenUser readToken() {

        if (!Files.exists(JSON_PATH)) {
            System.out.println("token.json file not found in: " + JSON_PATH.toAbsolutePath());
            Logger.logError("token.json file not found at: " + JSON_PATH.toAbsolutePath(), null);
            return null;
        }

        try {
            return mapper.readValue(JSON_PATH.toFile(), TokenUser.class);

        } catch (Exception e) {
            Logger.logError("Error reading token from: " + JSON_PATH.toAbsolutePath(), e);
            return null;
        }
    }


    
    /**
     * Elimina el archivo JSON que contiene el token (llave de autenticación).
     */
    public static void deleteToken() {
        
        try {
            if (Files.exists(JSON_PATH)) {
                Files.delete(JSON_PATH);
                System.out.println("Token successfully deleted.");
                Logger.logError("Token successfully deleted at: " + JSON_PATH.toAbsolutePath(), null);
            }
        } catch (IOException e) {
            Logger.logError("Failed to delete token at: " + JSON_PATH.toAbsolutePath(), e);
        }
    }
}
