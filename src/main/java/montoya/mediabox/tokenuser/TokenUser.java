package montoya.mediabox.tokenuser;

/**
 *
 * @author Nerea
 */
public class TokenUser {
    
    private String token;
    
    public TokenUser(){}
    
    public TokenUser(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    } 
}
