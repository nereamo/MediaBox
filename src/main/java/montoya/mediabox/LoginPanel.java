package montoya.mediabox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import montoya.mediabox.apiclient.ApiClient;
import montoya.mediabox.tokenuser.TokenController;
import montoya.mediabox.tokenuser.TokenUser;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Nerea
 */
public class LoginPanel extends JPanel{
    
    private JTextField txtEmail = new JTextField();
    private JPasswordField txtPassword = new JPasswordField("**********");
    private JPanel pnlEmail = new JPanel();
    private JPanel pnlPassword = new JPanel();
    private final JPanel pnlButtons = new JPanel();
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnClean = new JButton("Clean");
    private JCheckBox chkShowPssw = new JCheckBox("Show Password");
    private JCheckBox chkRemember = new JCheckBox("Remember me");
    private Font bold = new Font("Arial", Font.BOLD, 14);
    private Font plain = new Font("Arial", Font.PLAIN, 14);
    private static ApiClient client;
    private static String token;
    private static final String FOLDER_NAME = System.getProperty("user.home") + "/Token MediaBox";
    private static final Path JSON_PATH = Paths.get(FOLDER_NAME, "token.json");
    
    public LoginPanel(){
        
        this.setBounds(0, 0, 1300, 770);
        this.setLayout(new MigLayout("center", "[][grow][]", "100[]10[]20[]"));
        
        configEmail();
        configPassword();
        configButtons();
        writerPassword();
        showPassword();
        
        this.add(pnlEmail, "cell 0 0 3 1, align center");
        this.add(pnlPassword, "cell 0 1 3 1, align center");
        this.add(pnlButtons, "cell 0 2 3 1, align center");

        applyFontToAll(bold,plain);
        cleanTextFields();
    } 
    
    //Configuracion del panel que contiene el campo para introducir email
    private void configEmail(){
        pnlEmail.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlEmail.setBorder(BorderFactory.createTitledBorder("Email"));
        txtEmail.setPreferredSize(new Dimension(300, 30));
        pnlEmail.add(txtEmail, "wrap, align center");
    }
    
    //Configuracion del panel que contiene el campo para password y ver password
    private void configPassword(){
        pnlPassword.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlPassword.setBorder(BorderFactory.createTitledBorder("Password"));
        txtPassword.setPreferredSize(new Dimension(300, 30));
        pnlPassword.add(txtPassword, "wrap, align center");
        pnlPassword.add(chkShowPssw);
    }
    
    //Configuracion del panel que contiene los botones clean, login y el checkBox remember
    private void configButtons(){
        pnlButtons.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlButtons.add(chkRemember, "wrap, align center");
        pnlButtons.add(btnClean, "split 2, align center");
        pnlButtons.add(btnLogin);
        
        btnLogin.setBackground(new Color(255, 204, 153)); 
    }
    
    //Configuración de la fuente de los componentes
    private void applyFontToAll(Font fontBold, Font fontPlain) {
        txtEmail.setFont(fontPlain);
        txtPassword.setFont(fontPlain);
        btnLogin.setFont(fontBold);
        btnClean.setFont(fontBold);
        chkShowPssw.setFont(fontPlain);
        chkRemember.setFont(fontPlain);

        ((TitledBorder) pnlEmail.getBorder()).setTitleFont(fontBold);
        ((TitledBorder) pnlPassword.getBorder()).setTitleFont(fontBold);
    }
    
    //Boton clean limpia el texto escrito en txtEmail y txtPassword
    private void cleanTextFields() {
        btnClean.addActionListener(e -> {
            txtEmail.setText("");
            txtPassword.setText("**********");
            txtPassword.setEchoChar((char) 0);
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
    private void showPassword(){
        
        chkShowPssw.addActionListener(e ->{
            if(chkShowPssw.isSelected()){
                txtPassword.setEchoChar((char)0);
            }else{
                txtPassword.setEchoChar('*');
            }
        });  
    }
    
    
    private void loginUser() {

        btnLogin.addActionListener(e -> {

            String email = txtEmail.getText();
            String password = String.valueOf(txtPassword.getPassword());

            try {
                token = client.login(email, password);

                if (chkRemember.isSelected()) {
                    
                    TokenController.saveToken(token);
                    System.out.println("Archivo Token.json creado en " + FOLDER_NAME);
                }
            } catch (Exception ex) {
                System.getLogger(LoginPanel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }
    
    public static void autoLogin(){
        try{
            TokenUser save = TokenController.readToken();
            
            if(save != null){
                token = save.getToken();
                client.getMe(token);
                return;
            }
        } catch (Exception ex) {
            System.getLogger(LoginPanel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        try{
            Files.deleteIfExists(JSON_PATH);
            
        }catch(IOException e){
            System.getLogger(LoginPanel.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        
        token = null;
    }
    
}
