package montoya.mediabox.tokenuser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import tools.jackson.databind.ObjectMapper;

/**
 * Permite guardar el token del usuario al loguearse, leerlo para el autologin y eliminarlo.
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
     * Guarada el token (llave de autenticación) en un archivo JSON
     * 
     * @param token Identificador único de autenticación del usuario
     * @param email Email con el que se registra el usuario
     * @throws IOException Si ocurre un error al crear la carpeta o escribir el archivo
     */
    public static void saveToken(String token, String email) throws IOException {
        
        Files.createDirectories(FOLDER_PATH);
        TokenUser tu = new TokenUser(token, email);
        mapper.writeValue(JSON_PATH.toFile(), tu);
        System.out.println("Saved token in: " + JSON_PATH.toAbsolutePath());
    }
    
    /**
     * Lee el token (llave de autenticación) almacenado en JSON.
     * 
     * @return Objeto {@link TokenUser} en el token y email almacenado o {@code null} si el JSON no existe
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public static TokenUser readToken() throws IOException  {
        
        if(!Files.exists(JSON_PATH)){
            System.out.println("token.json file not found in: " + JSON_PATH.toAbsolutePath());
            return null;
        }
        
        return mapper.readValue(JSON_PATH.toFile(), TokenUser.class);
    }
    
    /**
     * Elimina el archivo JSON que contiene el token (llave de autenticación).
     */
    public static void deleteToken(){
        
        try {
            if (Files.exists(JSON_PATH)) {
                Files.delete(JSON_PATH);
                System.out.println("Token successfully deleted.");
            }
        } catch (IOException e) {
            System.err.println("The token could not be deleted: " + e.getMessage());
        }
    }
}
