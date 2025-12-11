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
    
    private static final String FOLDER_NAME = System.getProperty("user.home") + "/AppData/Local/MediaBox";
    private static final Path JSON_PATH = Paths.get(FOLDER_NAME, "token.json");
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    //Guarar el TOKEN en archivo token.json
    public static void saveToken(String token) throws IOException {
        
        Files.createDirectories(Paths.get(FOLDER_NAME));
        TokenUser tu = new TokenUser(token);
        mapper.writeValue(JSON_PATH.toFile(), tu);
    }
    
    //Leer archivo token.json
    public static TokenUser readToken() throws IOException  {
        
        if(!Files.exists(JSON_PATH)){
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
