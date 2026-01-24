package montoya.mediabox.tokenuser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import tools.jackson.databind.ObjectMapper;

/**
 *
 * @author Nerea
 */
public class TokenController {
    
    private static final Path FOLDER_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "MediaBox");
    private static final Path JSON_PATH = FOLDER_PATH.resolve("token.json");
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    //Guarar el TOKEN en archivo token.json
    public static void saveToken(String token, String email) throws IOException {
        
        Files.createDirectories(FOLDER_PATH);
        TokenUser tu = new TokenUser(token, email);
        mapper.writeValue(JSON_PATH.toFile(), tu);
        System.out.println("Token guardado en: " + JSON_PATH.toAbsolutePath());
    }
    
    //Leer archivo token.json
    public static TokenUser readToken() throws IOException  {
        
        if(!Files.exists(JSON_PATH)){
            System.out.println("Archivo token.json no encontrado en: " + JSON_PATH.toAbsolutePath());
            return null;
        }
        
        return mapper.readValue(JSON_PATH.toFile(), TokenUser.class);
    }
    
    public static void deleteToken(){
        
        try {
            if (Files.exists(JSON_PATH)) {
                Files.delete(JSON_PATH);
                System.out.println("Token eliminado correctamente.");
            }
        } catch (IOException e) {
            System.err.println("No se pudo eliminar el token: " + e.getMessage());
        }
    }
}
