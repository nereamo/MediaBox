package montoya.mediabox.panels;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.controller.CardManager;
import montoya.mediabox.configUI.UIStyles;
import montoya.mediapollingcomponent.MediaPollingComponent;
import net.miginfocom.swing.MigLayout;

/**
 * Panel que gestiona el login del usuario.
 * 
 * <p> Encargado de:
 * <ul>
 * <li> Mostra campos de texto para email y contraseña </li>
 * <li> Recordar credenciales si se ha seleccionado la opción "Remember me" </li>
 * <li> Autologin si el token es válido </li>
 * <li> Acción del botón login </li>
 * </ul>
 * 
 * @author Nerea
 */
public class Login extends JPanel{
    
    /** {@link MainFrame} Ventana principal donde se muestra el panel */
    private MainFrame frame;
    
    /** {@link CardManager} Gestionar el intercambio de paneles. */
    private CardManager cardManager;
    
    /** Campo de texto para el email del usuario */
    private JTextField txtEmail = new JTextField();
    
    /** Campo de texto para la contraseña del usuario */
    private JPasswordField txtPassword = new JPasswordField();
    
    /** Botón para iniciar sesión */
    private JButton btnLogin = new JButton();
    
    /** Checkbox para recordar las credenciales del usuario */
    private JCheckBox remember = new JCheckBox();
    
    /** Label para mostrar mensajes de error o información */
    public JLabel lblMessage = new JLabel();
    
    /** Label para mostrar el logo de la aplicación */
    private JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/images/logo_login.png")));
    
    /** Email del usuario loggeado actualmente */
    private String loggedEmail;
    
    /** Token de acceso del usuario loggeado */
    private static String token;

    /**
     * Constructor que nicializa el panel Login
     * 
     * @param frame Ventana principal donde se añade el panel 
     * @param cardManager Gestiona el intercambio de paneles.
     */
    public Login(MainFrame frame, CardManager cardManager){
        
        this.frame = frame;
        this.cardManager = cardManager;
 
        setupLayout(); 
        setupStyle();
        configKeyActions();
        initialLoginUser();
    } 
    
    /** @return El email del usuario que inició sesión */
    public String getLoggedEmail(){
        return loggedEmail;
    }

    /** Configura posición de los componentes */
    private void setupLayout() {
        
        this.setLayout(new MigLayout("fill, wrap, center", "[grow]", "push[]20[]10[]10[]20[]10[]push")); //Layout principal
        
        this.add(iconLabel, "align center, gapbottom 20"); //Logo
        
        this.add(txtEmail, "align center, w 400!, h 35!"); //Campo de texto email
        this.add(txtPassword, "align center, w 400!, h 35!, gaptop 10"); //Campo de texto password
        
        this.add(remember, "align center, gaptop 10");  //Checkbox remember
        this.add(btnLogin, "align center, w 70!, h 70!, gaptop 20"); //Botón login
        
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblMessage, "align center, gaptop 100, growx"); //Mensages informativos
    }
    
    /** Configura el estilo de los componentes */
    private void setupStyle() {
        this.setBackground(UIStyles.DARK_GREY_COLOR);

        UIStyles.styleField(txtEmail, "/images/email2.png", " Enter email", "/images/delete_url.png", null, false); //Campo de texto email
        UIStyles.styleField(txtPassword, "/images/pss.png", " Enter password", "/images/show.png", null, false); //Campo de texto password
        
        UIStyles.styleCheckBox(remember, "Remember me", "Remember credentials"); //Checkbox remember
        UIStyles.styleButtons(btnLogin, null, "/images/login.png", UIStyles.LIGHT_PURPLE, new Color(0, 0, 0, 0), true, "Login user", null); //Botón login 
    }

    
    /** Configura la tecla ENTER para mover el foco o ejecutar login. */
    private void configKeyActions(){
        txtEmail.addActionListener(new ActionListener (){ //pulsar ENTER envía foco a Password
            @Override
            public void actionPerformed(ActionEvent e){
                txtPassword.requestFocusInWindow();
            }
        });
        
        txtPassword.addActionListener(new ActionListener (){ //pulsar ENTER envía foco a botón para hacer login
            @Override
            public void actionPerformed(ActionEvent e){
                btnLogin.doClick();
            }
        }); 
    }

    /**
     * Configura la acción del botón login y autentifica al usuario.
     */
    private void initialLoginUser() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String email = txtEmail.getText();
                String password = String.valueOf(txtPassword.getPassword());

                if (email == null || email.trim().equals("") || password == null || password.trim().equals("")) {
                    UIStyles.showMessageInfo(lblMessage, "Please, enter an Email and Password");
                    return;
                }

                loggedEmail = email;//Guarda email del usuario loggeado

                frame.setMenuVisible(true);

                UIStyles.styleLabelUserName(frame.lblMessage, email);

                //Muestra el panel Downloads y refresca la tabla
                cardManager.showCard("downloads");
                frame.pnlDownload.infoMedia.reloadMediaData();
            }
        });
    }

    /**
     * Realiza autologin si el usuario indico que se recordaran sus credenciales y si el token es vállido.
     */
    public void autoLogin() {

    }
}