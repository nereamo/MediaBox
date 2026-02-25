package montoya.mediabox.tokenuser;

/**
 * Almacena una llave de autenticación del usuario.
 * <p> Contiene la llave de acceso (token) y el email del usuario asociado
 * 
 * @author Nerea
 */
public class TokenUser {
    
    /** Llave de acceso del usuario */
    private String token;
    
    /** Email del usuario */
    private String email;
    
    /** Constructor por defecto*/
    public TokenUser(){}
    
    /**
     * Crea objeto {@code TokenUser} con el token y el email del usuario.
     * 
     * @param token Llave de acceso
     * @param email Email del usuario
     */
    public TokenUser(String token, String email){
        this.token = token;
        this.email = email;
    }
    /** @return Devuelve el Token guardado */
    public String getToken() {
        return token;
    }

    /** @param token Almacena el token */
    public void setToken(String token) {
        this.token = token;
    }

    /** @return Devuelve el email del usuario guardado */
    public String getEmail() {
        return email;
    }

    /** @param email Almacena el email del usuario */
    public void setEmail(String email) {
        this.email = email;
    }
}
