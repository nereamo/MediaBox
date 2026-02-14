package montoya.mediabox.panels;

import java.awt.Cursor;
import java.awt.event.*;
import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.controller.CardManager;
import montoya.mediabox.configUI.SwingStyleUtils;
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
    private JButton btnLogin = new JButton();
    private JCheckBox remember = new JCheckBox();
    public JLabel lblMessage = new JLabel();
    private String loggedEmail;
    private static final String API_BASE_URL = "https://difreenet9.azurewebsites.net";
    private static String token;
    private static final String FOLDER_NAME = System.getProperty("user.home") + "/AppData/Local/MediaBox";
    private boolean isPasswordVisible = false;
    
    public Login(MainFrame frame, CardManager cardManager, MediaPollingComponent mediaPollingComponent){
        
        this.frame = frame;
        this.cardManager = cardManager;
        this.mediaPollingComponent = mediaPollingComponent;
 
        setupLayout(); 
        configKeyActions();
        initialLoginUser();
    } 
    
    public String getLoggedEmail(){ //Devuelve el email loggeado
        return loggedEmail;
    }

    //Configuracion de la posición de los componentes
    private void setupLayout() {
        
        this.setLayout(new MigLayout("wrap, center", "[grow]", "push[]20[]10[]10[]20[]10[]push"));
        this.setBackground(SwingStyleUtils.DARK_GREY_COLOR);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo_login.png"));
        JLabel lblIcon = new JLabel(icon);
        this.add(lblIcon, "align center, gapbottom 20");
        
        configemailField();
        this.add(txtEmail, "align center, w 300!, h 30!");
        
        configPasswordField();
        this.add(txtPassword, "align center, w 300!, h 30!, gaptop 10");
        
        SwingStyleUtils.styleCheckBox(remember, "Remember me", "Remember credentials");
        this.add(remember, "align center, gaptop 10");

        SwingStyleUtils.styleIconButton(btnLogin, "/images/login.png", "Login user");
        this.add(btnLogin, "align center, w 200!, h 40!, gaptop 20");

        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblMessage, "align center, gaptop 180, growx");
    }

    //Estilo del JTextField email
    private void configemailField() {
        SwingStyleUtils.addIconsTextField(txtEmail, "/images/email2.png", "/images/delete_url.png", "Enter email");

        txtEmail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int width = txtEmail.getWidth();

                if (e.getX() >= width -30){
                    txtEmail.setText("");
                }
            }
        });

        txtEmail.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int width = txtEmail.getWidth();

                if (e.getX() >= width -30) {
                    SwingStyleUtils.handCursor(txtEmail);
                } else {
                    txtEmail.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
            }
        });
    }
    
    //Estilo del JPasswordField password
    private void configPasswordField() {
        SwingStyleUtils.addIconsPasswordField(txtPassword, "/images/pss.png", "/images/show.png", "Enter password");
        
        txtPassword.setEchoChar('•');
        isPasswordVisible = false;

        txtPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int width = txtPassword.getWidth();
                

                if (e.getX() >= width -30){
                    
                    isPasswordVisible = !isPasswordVisible;
                    
                    if(isPasswordVisible){
                        txtPassword.setEchoChar((char) 0);
                    } else {
                    txtPassword.setEchoChar('•');
                }
                }
            }
        });

        txtPassword.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int width = txtPassword.getWidth();

                if (e.getX() >= width -30) {
                    SwingStyleUtils.handCursor(txtEmail);
                } else {
                    txtPassword.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
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

    //Loguea usuario al pulsar boton Login y guarda token
    private void initialLoginUser() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String email = txtEmail.getText();
                loggedEmail = email;
                String password = String.valueOf(txtPassword.getPassword());

                if (email == null || email.trim().equals("")
                        || password == null || password.trim().equals("")) {
                    SwingStyleUtils.showMessageInfo(lblMessage, "Please, enter an Email and Password");
                    return;
                }

                try {
                    mediaPollingComponent.setApiUrl(API_BASE_URL);
                    String newToken = mediaPollingComponent.login(email, password);

                    if (newToken != null) {

                        try {
                            mediaPollingComponent.getAllMedia(newToken);
                        } catch (Exception ex) {
                            SwingStyleUtils.showMessageInfo(lblMessage, "User logged out. Please login again.");
                            return;
                        }
                        token = newToken;
                        mediaPollingComponent.setToken(token);
                        //TokenController.saveToken(token, email);
                        
                        frame.setMenuVisible(true); 
                        frame.initializePolling(token);
                        
                        frame.lblMessage.setText(email);
                        frame.lblMessage.setFont(SwingStyleUtils.FONT_BOLD);
                        frame.lblMessage.setForeground(SwingStyleUtils.WHITE_COLOR); //Usuario loggeado en label menuBar
                        cardManager.showCard("downloads");
                        frame.pnlDownload.infoMedia.refreshFiles(); //Refresca la tabla al hacer login

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
                    SwingStyleUtils.showMessageInfo(lblMessage, "User logged out. Please log in again.");
                    token = null; 
                    cardManager.showCard("login"); 
                    return;
                }
                token = savedToken;
                mediaPollingComponent.setToken(token);
                frame.initializePolling(token);
                frame.setMenuVisible(true);
                frame.lblMessage.setText(emailUser); //Usuario loggeado en label menuBar
                frame.lblMessage.setFont(SwingStyleUtils.FONT_BOLD);
                frame.lblMessage.setForeground(SwingStyleUtils.WHITE_COLOR);
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