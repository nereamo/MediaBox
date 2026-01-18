
package montoya.mediabox.styleConfig;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Nerea
 */
public class StyleConfig {
    
    //Fonts
    public static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 14);
    public static final Font FONT_PLAIN = new Font("Arial", Font.PLAIN, 14);

    //Colors
    public static final Color BACKGROUND = new Color(61, 61, 64);
    public static final Color ORANGE_COLOR = new Color(255, 153, 51);
    public static final Color PANEL_COLOR = new Color(177, 178, 189);
    
    //Border
    public static Border createTitleBorder(String title){
        Border border = BorderFactory.createLineBorder(Color.WHITE);
        TitledBorder titBorder = BorderFactory.createTitledBorder(border, title);
        titBorder.setTitleColor(Color.WHITE);
        titBorder.setTitleFont(FONT_BOLD);
        return titBorder;
    }
    
    //CheckBox
    public static void styleCheckBox(JCheckBox check, String toolTip){
        check.setBackground(BACKGROUND);
        check.setForeground(Color.WHITE);
        check.setFont(FONT_PLAIN);
        check.setFocusPainted(false);
        check.setToolTipText(toolTip);
    }
    
    //Textfiles Login
    public static void styleTextFieldAndPasswordLogin(JTextField txtField, String toolTip){
        txtField.setFont(FONT_PLAIN);
        txtField.setPreferredSize(new Dimension(300, 40));
        txtField.setToolTipText(toolTip);
    }
    
    //Buttons
    public static void styleButton(JButton btn, String iconPath, String tootlTip) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 50));
        btn.setIcon(new ImageIcon(StyleConfig.class.getResource(iconPath)));
        btn.setToolTipText(tootlTip);
    }
    
    //Items menu
    public static void styleMenuItems(JMenuItem item, String iconPath, String text, String toolTip) {
        item.setText(text == null ? "" : text);
        item.setOpaque(false);
        item.setContentAreaFilled(false);
        item.setBorderPainted(false);
        item.setPreferredSize(new Dimension(90, 50));
        item.setIcon(new ImageIcon(StyleConfig.class.getResource(iconPath)));
        item.setToolTipText(toolTip);
    }
    
    //Logo
    public static void setLogo(JLabel lblLogo, String logoPath){
        lblLogo.setPreferredSize(new Dimension(150, 40));
        lblLogo.setIcon(new ImageIcon(StyleConfig.class.getResource(logoPath)));
    }
    
    //Panel login
    public static JPanel createFieldWithIcon(String iconPath, JComponent field) {
        JPanel panel = new JPanel(new MigLayout("insets 0", "[]10[grow]", "[]"));
        panel.setBackground(StyleConfig.BACKGROUND);
        
        JLabel lblIcon = new JLabel(new ImageIcon(StyleConfig.class.getResource(iconPath)));
        panel.add(lblIcon);
        panel.add(field, "growx");
        
        return panel;
    }
}
