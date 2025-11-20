package montoya.mediabox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Nerea
 */
public class LoginPanel extends JPanel{
    
    private JTextField txtEmail = new JTextField();
    private JTextField txtPassword = new JTextField();
    private JPanel pnlEmail = new JPanel();
    private JPanel pnlPassword = new JPanel();
    private final JPanel pnlButtons = new JPanel();
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnClean = new JButton("Clean");
    
    public LoginPanel(){
        
        this.setBounds(0, 0, 1300, 770);
        this.setLayout(new MigLayout("center", "[][grow][]", "100[]10[]20[]"));
        
        configEmail();
        configPassword();
        configButtons();
        
        this.add(pnlEmail, "cell 0 0 3 1, align center");
        this.add(pnlPassword, "cell 0 1 3 1, align center");
        this.add(pnlButtons, "cell 0 2 3 1, align center");

        applyFontToAll(new Font("Arial", Font.BOLD, 14));
        cleanTextFields();
    } 
    
    private void configEmail(){
        pnlEmail.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlEmail.setBorder(BorderFactory.createTitledBorder("Email"));
        txtEmail.setPreferredSize(new Dimension(300, 30));
        pnlEmail.add(txtEmail, "wrap, align center");
    }
    
    private void configPassword(){
        pnlPassword.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlPassword.setBorder(BorderFactory.createTitledBorder("Password"));
        txtPassword.setPreferredSize(new Dimension(300, 30));
        pnlPassword.add(txtPassword, "wrap, align center");
    }
    
    private void configButtons(){
        pnlButtons.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlButtons.add(btnClean, "split 2, align center");
        pnlButtons.add(btnLogin);
        
        btnLogin.setBackground(new Color(255, 204, 153)); 
    }
    
    private void applyFontToAll(Font font) {
        txtEmail.setFont(font);
        txtPassword.setFont(font);
        btnLogin.setFont(font);
        btnClean.setFont(font);

        ((TitledBorder) pnlEmail.getBorder()).setTitleFont(font);
        ((TitledBorder) pnlPassword.getBorder()).setTitleFont(font);
    }
    
    private void cleanTextFields(){
        btnClean.addActionListener(e -> {
            txtEmail.setText("");
            txtPassword.setText("");
        });
    } 
    
}
