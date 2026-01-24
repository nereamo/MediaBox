package montoya.mediabox.panels;

import java.awt.event.*;
import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.controller.CardManager;
import montoya.mediabox.styleConfig.StyleConfig;
import montoya.mediabox.tokenuser.TokenController;
import montoya.mediabox.tokenuser.TokenUser;
import montoya.mediapollingcomponent.MediaPollingComponent;
import net.miginfocom.swing.MigLayout;

/**
 * Panel que gestiona el login del usuario
 * @author Nerea
 */
public class Login extends JPanel{
    
    private MainFrame frame;
    private CardManager cardManager;
    private MediaPollingComponent mediaPollingComponent;
    private JTextField txtEmail = new JTextField();
    private JPasswordField txtPassword = new JPasswordField();
    private JPanel pnlButtons = new JPanel();
    private JButton btnLogin = new JButton();
    private JButton btnClean = new JButton();
    private JCheckBox showPassword = new JCheckBox();
    private JCheckBox remember = new JCheckBox();
    private JLabel lblMessage = new JLabel();
    private String loggedEmail;
    private static final String API_BASE_URL = "https://difreenet9.azurewebsites.net";
    private static String token;
    private static final String FOLDER_NAME = System.getProperty("user.home") + "/AppData/Local/MediaBox";
    
    public Login(MainFrame frame, CardManager cardManager, MediaPollingComponent mediaPollingComponent){
        
        this.frame = frame;
        this.cardManager = cardManager;
        this.mediaPollingComponent = mediaPollingComponent;
        
        this.setBounds(0, 0, 1300, 770);
        this.setLayout(new MigLayout("center", "[][grow][]", "100[]10[]20[]"));
        this.setBackground(StyleConfig.DARK_BLUE_COLOR);
        
        configComponents(); //Configuracion de componentes
        configKeyActions();
        
        cleanButton(); //Limpia los campos
        
        txtPassword.setEchoChar('•'); //Configuracion de visibilidad de contraseña
        showPassword();
        
        loginUser(); //Configuracion de login
    } 
    
    public String getLoggedEmail(){ //Devuelve el email loggeado
        return loggedEmail;
    }
    
    //Establece posicion para los componentes
    private void configComponents(){
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.png"));
        JLabel lblIcon = new JLabel(icon);
        this.add(lblIcon, "cell 0 0 3 1, align center"); 
        
        JPanel emailRow = StyleConfig.createLoginField("/images/email.png", txtEmail);
        StyleConfig.styleTextFieldAndPasswordLogin(txtEmail, "Enter your email");
        this.add(emailRow, "cell 0 2 3 1, growx, align center, wmin 200, w 300, wmax 300");
        
        JPanel passwordRow = StyleConfig.createLoginField("/images/password.png", txtPassword);
        StyleConfig.styleTextFieldAndPasswordLogin(txtPassword, "Enter your password");
        this.add(passwordRow, "cell 0 3 3 1, growx, align center, wmin 200, w 300, wmax 300");

        JPanel showPasswordRow = StyleConfig.createLoginField("", showPassword);
        this.add(showPasswordRow, "cell 0 4 3 1, growx, align center, wmin 200, w 300, wmax 300");
        StyleConfig.styleCheckBox(showPassword,"Show Password", "Show password");
        
        JPanel rememberRow = StyleConfig.createLoginField("", remember);
        this.add(rememberRow, "cell 0 5 3 1, growx, align center, wmin 200, w 300, wmax 300");
        StyleConfig.styleCheckBox(remember,"Remember me", "Remember credentials");
        
        this.add(pnlButtons, "cell 0 6 3 1, align center");
        
        pnlButtons.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlButtons.setBackground(StyleConfig.DARK_BLUE_COLOR);

        pnlButtons.add(btnClean, "split 2, align center");
        pnlButtons.add(btnLogin);
        
        StyleConfig.styleButton(btnLogin, "/images/login.png", "Login user");
        
        lblMessage.setForeground(StyleConfig.GREY_COLOR); // o el color que prefieras 
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER); 
        this.add(lblMessage, "cell 0 7 3 1, growx, align center, gaptop 10");
    }

    
    //Boton clean limpia los campos
    private void cleanButton() {
        StyleConfig.styleButton(btnClean, "/images/clear.png", "Clear fields");

        btnClean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtEmail.setText("");
                txtPassword.setText("");
                txtPassword.setEchoChar('•');
                showPassword.setSelected(false);
                remember.setSelected(false);
                lblMessage.setText("");
            }
        });
    }
    
    //Al pulsar ENTER hace login
    private void configKeyActions(){
        txtEmail.addActionListener(new ActionListener (){
            @Override
            public void actionPerformed(ActionEvent e){
                txtPassword.requestFocusInWindow();
            }
        });
        
        txtPassword.addActionListener(new ActionListener (){
            @Override
            public void actionPerformed(ActionEvent e){
                btnLogin.doClick();
            }
        }); 
    }
    
    //CheckBox que permite ver la contraseña introducida
    private void showPassword() {
        showPassword.setBackground(StyleConfig.DARK_BLUE_COLOR);
        showPassword.setForeground(StyleConfig.GREY_COLOR);
        showPassword.setToolTipText("Show password");
        
        showPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    txtPassword.setEchoChar((char) 0);
                } else {
                    txtPassword.setEchoChar('•');
                }
            }
        });
    }

    //Loguea usuario al pulsar boton Login y guarda token
    private void loginUser() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String email = txtEmail.getText();
                loggedEmail = email;
                String password = String.valueOf(txtPassword.getPassword());

                if (email == null || email.trim().equals("")
                        || password == null || password.trim().equals("")) {
                    StyleConfig.showMessage(lblMessage, "Please, enter an Email and Password");
                    return;
                }

                try {
                    mediaPollingComponent.setApiUrl(API_BASE_URL);
                    String newToken = mediaPollingComponent.login(email, password);

                    if (newToken != null) {

                        try {
                            mediaPollingComponent.getAllMedia(newToken);
                        } catch (Exception ex) {
                            StyleConfig.showMessage(lblMessage, "User logged out. Please log in again.");
                            return;
                        }
                        token = newToken;
                        mediaPollingComponent.setToken(token);
                        TokenController.saveToken(token, email);
                        
                        frame.setMenuVisible(true); 
                        frame.initializePolling(token);
                        
                        frame.lblMessage.setText("Welcome: " + email);
                        frame.lblMessage.setForeground(StyleConfig.GREY_COLOR); //Usuario loggeado en label menuBar
                        cardManager.showCard("downloads");

                        if (remember.isSelected()) {
                            TokenController.saveToken(token, email);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Login.this, "Login failed: Incorrect credentials or expired token.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    //Lee token y si es correcto, accede a Frame directamente
    public void autoLogin(){
        try{
            TokenUser save = TokenController.readToken();
            String emailUser = save.getEmail();
            
            if(save != null){
                String savedToken = save.getToken();
                mediaPollingComponent.setApiUrl(API_BASE_URL);
                
                try{
                    mediaPollingComponent.getAllMedia(savedToken); //Llamada a api para comprobar si el token es correcto
                    
                }catch(Exception e){
                    StyleConfig.showMessage(lblMessage, "User logged out. Please log in again.");
                    token = null; 
                    cardManager.showCard("login"); 
                    return;
                }
                token = savedToken;
                mediaPollingComponent.setToken(token);
                frame.initializePolling(token);
                frame.setMenuVisible(true);
                frame.lblMessage.setText("Welcome: " + emailUser); //Usuario loggeado en label menuBar
                cardManager.showCard("downloads");
                System.out.println("Login successful.");
                return;
            }
        } catch (Exception ex) {
            System.getLogger(Login.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        token = null;
        cardManager.showCard("login");
    }
}
