
package montoya.mediabox.styleConfig;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

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
    
    //Border
    public static Border createTitleBorder(String title){
        Border border = BorderFactory.createLineBorder(Color.WHITE);
        TitledBorder titBorder = BorderFactory.createTitledBorder(border, title);
        titBorder.setTitleColor(Color.WHITE);
        titBorder.setTitleFont(FONT_BOLD);
        return titBorder;
    }
    
    //CheckBox
    public static void styleCheckBox(JCheckBox check){
        check.setBackground(BACKGROUND);
        check.setForeground(Color.WHITE);
        check.setFont(FONT_PLAIN);
        check.setFocusPainted(false);
    }
    
    //Textfiles Login
    public static void styleTextField(JTextField txtField){
        txtField.setFont(FONT_PLAIN);
        txtField.setPreferredSize(new Dimension(300, 30));
    }
    
    public static void orangeButtons(JButton btn, String iconPath) {
        btn.setBackground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 40));
        btn.setIcon(new ImageIcon(StyleConfig.class.getResource(iconPath)));
    }
    
    public static void defaultButtons(JButton btn, String iconPath){
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 40));
        btn.setIcon(new ImageIcon(StyleConfig.class.getResource(iconPath)));
    }

}
