package montoya.mediabox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
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
    private JCheckBox chkRemember = new JCheckBox("Remember");
    private Font bold = new Font("Arial", Font.BOLD, 14);
    private Font plain = new Font("Arial", Font.PLAIN, 14);
    
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
        pnlPassword.add(chkShowPssw);
    }
    
    private void configButtons(){
        pnlButtons.setLayout(new MigLayout("center", "[grow]", "[]"));
        pnlButtons.add(chkRemember, "wrap, align center");
        pnlButtons.add(btnClean, "split 2, align center");
        pnlButtons.add(btnLogin);
        
        btnLogin.setBackground(new Color(255, 204, 153)); 
    }
    
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
    
    private void cleanTextFields(){
        btnClean.addActionListener(e -> {
            txtEmail.setText("");
            txtPassword.setText("");
        });
    } 
    
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
    
    private void showPassword(){
        
        chkShowPssw.addActionListener(e ->{
            if(chkShowPssw.isSelected()){
                txtPassword.setEchoChar((char)0);
            }else{
                txtPassword.setEchoChar('*');
            }
        });  
    }
    
    
    
    private void rememberUser(){
        
    }
    
}
