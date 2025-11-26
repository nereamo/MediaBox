package montoya.mediabox.tokenuser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import tools.jackson.databind.ObjectMapper;

/**
 *
 * @author Nerea
 */
public class TokenController {
    
    private static final String FOLDER_NAME = System.getProperty("user.home") + "/Descargas MediaBox";
    private static final Path JSON_PATH = Paths.get(FOLDER_NAME, "token.json");
    private static final ObjectMapper mapper = new ObjectMapper();
    
    //Guarar el TOKEN en archivo token.json
    public static void saveToken(String token) throws Exception{
        
        Files.createDirectories(Paths.get(FOLDER_NAME));
        TokenUser tu = new TokenUser(token);
        mapper.writeValue(JSON_PATH.toFile(), tu);
    }
    
    //Leer archivo token.json
    public static TokenUser readToken() throws Exception {
        
        if(!Files.exists(JSON_PATH)){
            return null;
        }
        
        return mapper.readValue(JSON_PATH.toFile(), TokenUser.class);
    }
}
