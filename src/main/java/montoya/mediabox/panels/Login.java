package montoya.mediabox.panels;

import java.awt.*;
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
    private static final String API_BASE_URL = "https://difreenet9.azurewebsites.net";
    private static String token;
    private static final String FOLDER_NAME = System.getProperty("user.home") + "/AppData/Local/MediaBox";
    
    public Login(MainFrame frame, CardManager cardManager, MediaPollingComponent mediaPollingComponent){
        
        this.frame = frame;
        this.cardManager = cardManager;
        this.mediaPollingComponent = mediaPollingComponent;
        
        this.setBounds(0, 0, 1300, 770);
        this.setLayout(new MigLayout("center", "[][grow][]", "100[]10[]20[]"));
        this.setBackground(StyleConfig.BACKGROUND);
        
        configComponents();
        configKeyActions();
        writerPassword();
        showPassword();
        loginUser();
        cleanButton();
    } 
    
    //Establece posicion para los componentes
    private void configComponents(){
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/big_logo2.png"));
        JLabel lblIcon = new JLabel(icon);
        this.add(lblIcon, "cell 0 0 3 1, align center"); 
        
        JPanel emailRow = StyleConfig.createLoginField("/images/email.png", txtEmail);
        this.add(emailRow, "cell 0 2 3 1, growx, align center, wmin 200, w 300, wmax 300");
        
        JPanel passwordRow = StyleConfig.createLoginField("/images/password.png", txtPassword);
        this.add(passwordRow, "cell 0 3 3 1, growx, align center, wmin 200, w 300, wmax 300");
        
        StyleConfig.styleCheckBox(showPassword,"Show Password",Color.WHITE, "Show password");
        JPanel showPasswordRow = StyleConfig.createLoginField("/images/show_password.png", showPassword);
        this.add(showPasswordRow, "cell 0 4 3 1, growx, align center, wmin 200, w 300, wmax 300");
        
        this.add(pnlButtons, "cell 0 6 3 1, align center");
        
        pnlButtons.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlButtons.setBackground(StyleConfig.BACKGROUND);
        
        pnlButtons.add(remember, "wrap, align center");
        StyleConfig.styleCheckBox(remember,"Remember me",Color.WHITE, "Remember credentials");

        pnlButtons.add(btnClean, "split 2, align center");
        pnlButtons.add(btnLogin);
        
        StyleConfig.styleButton(btnLogin, "/images/login.png", "Login user");
    }

    
    //Boton clean limpia los campos
    private void cleanButton() {
        StyleConfig.styleButton(btnClean, "/images/clear.png", "Clear fields");

        btnClean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtEmail.setText("");
                txtPassword.setText("**********");
                txtPassword.setEchoChar((char) 0);
                showPassword.setSelected(false);
                remember.setSelected(false);
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
    
    //Muestra '*' en campo password y oculta password al ser escrita
    private void writerPassword(){
        txtPassword.setEchoChar((char)0);
        
        txtPassword.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
                if(String.valueOf(txtPassword.getPassword()).equals("**********")){
                    txtPassword.setText("");
                    txtPassword.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(txtPassword.getPassword().length == 0){
                    txtPassword.setText("**********");
                    txtPassword.setEchoChar((char)0);
                }
            }
        });
    }
    
    //CheckBox que permite ver la contraseña introducida
    private void showPassword() {
        showPassword.setBackground(StyleConfig.BACKGROUND);
        showPassword.setForeground(Color.WHITE);
        showPassword.setToolTipText("Show password");
        
        showPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    txtPassword.setEchoChar((char) 0);
                } else {
                    txtPassword.setEchoChar('*');
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
                String password = String.valueOf(txtPassword.getPassword());

                if (email == null || email.trim().equals("")
                        || password == null || password.trim().equals("")) {
                    JOptionPane.showMessageDialog(Login.this,"Please, enter an Email and Password","Login error",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    mediaPollingComponent.setApiUrl(API_BASE_URL);
                    token = mediaPollingComponent.login(email, password);

                    if (token != null) {
                        mediaPollingComponent.setToken(token);
                        frame.setMenuVisible(true); 
                        TokenController.saveToken(token);
                        frame.initializePolling(token);
                        JOptionPane.showMessageDialog(Login.this,"Login successful: " + email,"Success",JOptionPane.INFORMATION_MESSAGE);
                        
                        cardManager.showCard("downloads");

                        if (remember.isSelected()) {
                            TokenController.saveToken(token);
                            System.out.println("Archivo Token.json creado en " + FOLDER_NAME);
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
            
            if(save != null){
                token = save.getToken();
                mediaPollingComponent.setToken(token);
                frame.initializePolling(token);
                System.out.println("Login Exitoso.");
                frame.setMenuVisible(true);
                cardManager.showCard("downloads");
                return;
            }
        } catch (Exception ex) {
            System.getLogger(Login.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        token = null;
    }
}
