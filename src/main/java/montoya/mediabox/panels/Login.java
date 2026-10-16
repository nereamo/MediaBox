package montoya.mediabox.panels;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.controller.CardManager;
import montoya.mediabox.configUI.UIStyles;
import net.miginfocom.swing.MigLayout;
import montoya.mediabox.auth.GoogleAuth;
import montoya.mediabox.utils.Logger;

/**
 * Panel que gestiona el login del usuario.
 * 
 * <p> Encargado de:
 * <ul>
 * <li> Mostra campos de texto para email y contraseña </li>
 * <li> Recordar credenciales si se ha seleccionado la opción "Remember me" </li>
 * <li> Acción del botón login </li>
 * <li> Acceder con Google </li>
 * </ul>
 * 
 * @author Nerea
 */
public class Login extends JPanel {

    /**
     * {@link MainFrame} Ventana principal donde se muestra el panel
     */
    private MainFrame frame;

    /**
     * {@link CardManager} Gestionar el intercambio de paneles.
     */
    private CardManager cardManager;

    /**
     * Botón para iniciar sesión con Goolge
     */
    private JButton btnGoogle = new JButton();
    
    /**
     * Botón para acceder como invitado (Modo Demo)
     */
    private JButton btnGuest = new JButton();

    /**
     * Label para mostrar mensajes de error o información
     */
    public JLabel lblMessage = new JLabel();

    /**
     * Label para mostrar el logo de la aplicación
     */
    private JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/images/logo_login.png")));

    /**
     * Email del usuario loggeado actualmente
     */
    private String loggedEmail;

    /**
     * Constructor que nicializa el panel Login
     *
     * @param frame Ventana principal donde se añade el panel
     * @param cardManager Gestiona el intercambio de paneles.
     */
    public Login(MainFrame frame, CardManager cardManager) {

        this.frame = frame;
        this.cardManager = cardManager;

        setupLayout();
        setupStyle();
        initGuestLogin();
        initGoogleLogin();
        autoLogin();
    }

    /**
     * @return El email del usuario que inició sesión
     */
    public String getLoggedEmail() {
        return loggedEmail;
    }

    /**
     * Configura posición de los componentes
     */  
    private void setupLayout() {
        // Layout centrado y limpio, ideal para un diseño moderno
        this.setLayout(new MigLayout("fill, wrap, center", "[grow]", "push[]30[]20[]20[]30[]push"));

        this.add(iconLabel, "align center, gapbottom 10"); // Logo

        this.add(btnGoogle, "align center, w 260!, h 50!, gaptop 10"); // Botón Google
        this.add(btnGuest, "align center, w 260!, h 50!, gaptop 10");  // Botón Invitado

        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblMessage, "align center, gaptop 20, growx"); // Mensajes informativos
    }

    /**
     * Configura el estilo de los componentes
     */
    private void setupStyle() {
        this.setBackground(UIStyles.DARK_GREY_COLOR);

        // Estilos para los botones de acceso
        UIStyles.styleButtons(btnGoogle, "Login with Google", "/images/google.png", UIStyles.LIGHT_PURPLE, Color.DARK_GRAY, true, "Login with Google", null);
        UIStyles.styleButtons(btnGuest, "Guest Mode (Demo)", "/images/guest.png", UIStyles.LIGHT_GREY_COLOR, Color.DARK_GRAY, true, "Access as Guest for testing", null);
    }

    /**
     * Realiza autologin si el usuario indico que se recordaran sus credenciales y si el token es vállido.
     */
    public void autoLogin() {

        new SwingWorker<String, Void>() {

            @Override
            protected String doInBackground() throws Exception {
                return GoogleAuth.autoSignIn();
            }

            @Override
            protected void done() {
                try {
                    String email = get();

                    if (email != null && !email.trim().isEmpty()) {
                        enter(email);
                    }

                } catch (Exception ex) {
                    Logger.logError("Google auto-login failed", ex);
                }
            }

        }.execute();
    }

    /**
     * Entra en la app con el email del usuario.
     */
    private void enter(String email) {
        loggedEmail = email;

        frame.setMenuVisible(true);

        //Mostrar email del usuario loggeado en JMenuBar
        UIStyles.styleLabelUserName(frame.lblMessage, email);

        //Muestra el panel Downloads y refresca la tabla
        cardManager.showCard("downloads");
        frame.pnlDownload.infoMedia.reloadMediaData();
    }
    
    /**
     * Inicia sesión como invitado
     */
    private void initGuestLogin(){
        
        btnGuest.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                enter("guest.mediaBox@demo.com");
            }
        });
        
    }

    /**
     * Configura la acción del botón "SignIn with Google".
     */
    private void initGoogleLogin() {
        btnGoogle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Desactiva temporalmente el botón y muestra un mensaje informativo al usuario
                btnGoogle.setEnabled(false);
                UIStyles.showMessageInfo(lblMessage, "Waiting for Google sign-in in your browser...");

                //Fuera del hilo de Swing para no congelar la ventana
                new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        // Llama al proceso de inicio de sesión de Google (abre el navegador local)
                        return GoogleAuth.signIn();
                    }

                    @Override
                    protected void done() {
                        // Vuelve a habilitar el botón una vez finalizada o interrumpida la tarea
                        btnGoogle.setEnabled(true);
                        try {
                            // Obtiene el correo electrónico devuelto por el proceso en segundo plano
                            String email = get();

                            // Valida que el email obtenido no esté vacío o nulo
                            if (email == null || email.trim().isEmpty()) {
                                UIStyles.showMessageInfo(lblMessage, "Could not obtain your Google email");
                                return;
                            }
                            
                            // Si todo es correcto, permite el acceso del usuario a la aplicación
                            enter(email);

                        } catch (Exception ex) {
                            // Registra el error en los logs y muestra un aviso visual si el login falló o fue cancelado
                            Logger.logError("Google sign-in failed", ex);
                            UIStyles.showMessageInfo(lblMessage, "Google sign-in failed or was cancelled");
                        }
                    }
                }.execute();
            }
        });
    }
}
