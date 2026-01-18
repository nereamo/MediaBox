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
    private JPasswordField txtPassword = new JPasswordField("**********");
    private JPanel pnlEmail = new JPanel();
    private JPanel pnlPassword = new JPanel();
    private final JPanel pnlButtons = new JPanel();
    private final JButton btnLogin = new JButton();
    private final JButton btnClean = new JButton();
    private JCheckBox chkShowPssw = new JCheckBox("Show Password");
    private JCheckBox chkRemember = new JCheckBox("Remember me");
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
        
        configEmail();
        configPassword();
        configComponents();
        configKeyActions();
        writerPassword();
        showPassword();
        loginUser();
        
        this.add(pnlEmail, "cell 0 0 3 1, align center");
        this.add(pnlPassword, "cell 0 1 3 1, align center");
        this.add(pnlButtons, "cell 0 2 3 1, align center");

        cleanTextFields();
    } 
    
    //Configuracion del panel que contiene el campo para introducir email
    private void configEmail(){
        pnlEmail.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlEmail.setBorder(StyleConfig.createTitleBorder("EMAIL"));
        pnlEmail.setBackground(StyleConfig.BACKGROUND);
        
        StyleConfig.styleTextFieldAndPasswordLogin(txtEmail, "Email");
        
        pnlEmail.add(txtEmail, "wrap, align center");
    }
    
    //Configuracion del panel que contiene el campo para password y ver password
    private void configPassword(){
        pnlPassword.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlPassword.setBorder(StyleConfig.createTitleBorder("PASSWORD"));
        pnlPassword.setBackground(StyleConfig.BACKGROUND);
   
        StyleConfig.styleTextFieldAndPasswordLogin(txtPassword, "Password");
        
        pnlPassword.add(txtPassword, "wrap, align center");
        pnlPassword.add(chkShowPssw); 
    }
    
    //Configuracion del panel que contiene los botones clean, login y el checkBox remember
    private void configComponents(){
        pnlButtons.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlButtons.setBackground(StyleConfig.BACKGROUND);
        
        pnlButtons.add(chkRemember, "wrap, align center");
        StyleConfig.styleCheckBox(chkRemember, "Remember credentials");

        pnlButtons.add(btnClean, "split 2, align center");
        pnlButtons.add(btnLogin);
        
        StyleConfig.styleButtons(btnLogin, "/images/login.png", "Login user");
    }
    
    //Resetea los campos
    public void resetFields() {
        txtEmail.setText("");
        txtPassword.setText("**********");
        txtPassword.setEchoChar((char) 0);
        chkShowPssw.setSelected(false);
        chkRemember.setSelected(false);
    }
    
    //Boton clean limpia el texto escrito en txtEmail y txtPassword
    private void cleanTextFields() {
        StyleConfig.styleButtons(btnClean, "/images/clear.png", "Clear fields");
        
        btnClean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
    }
    
    //Al pulsar ENTER hace login
    public void configKeyActions(){
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
        chkShowPssw.setBackground(StyleConfig.BACKGROUND);
        chkShowPssw.setForeground(Color.WHITE);
        chkShowPssw.setToolTipText("Show password");
        
        chkShowPssw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chkShowPssw.isSelected()) {
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

                        if (chkRemember.isSelected()) {
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
                System.out.println("Login Exitoso." + token);
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
