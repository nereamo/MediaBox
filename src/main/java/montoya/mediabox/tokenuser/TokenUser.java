package montoya.mediabox.tokenuser;

/**
 *
 * @author Nerea
 */
public class TokenUser {
    
    private String token;
    private String email;
    
    public TokenUser(){}
    
    public TokenUser(String token, String email){
        this.token = token;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
