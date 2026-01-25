
package montoya.mediabox.styleConfig;

import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Nerea
 */
public class StyleConfig {
    
    //Fonts
    public static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 16);
    public static final Font FONT_PLAIN = new Font("Arial", Font.PLAIN, 16);

    //Colors
    public static final Color GREY_COLOR = new Color(181, 182, 189);
    public static final Color WHITE_COLOR = new Color (255,255,255);
    public static final Color LIGHT_BLUE_COLOR = new Color (135,139,222);
    public static final Color DARK_BLUE_COLOR = new Color(46, 51, 94);
    
    //Border
    public static void createTitleBorder(JPanel panel, String title){
        Border border = BorderFactory.createLineBorder(LIGHT_BLUE_COLOR);
        TitledBorder titBorder = BorderFactory.createTitledBorder(border, title);
        
        titBorder.setTitleColor(LIGHT_BLUE_COLOR);
        titBorder.setTitleFont(FONT_BOLD);
        
        panel.setOpaque(false);
        panel.setBorder(titBorder);
        //panel.setBackground(PANEL_COLOR_AZULCLARO);
    }
    
    //Labels
    public static void styleLabel(JLabel label, String text){
        label.setText(text);
        label.setForeground(GREY_COLOR);
        label.setFont(FONT_PLAIN);
    }
    
    //CheckBox
    public static void styleCheckBox(JCheckBox check, String text, String toolTip){
        check.setOpaque(false);
        check.setForeground(GREY_COLOR);
        check.setFont(FONT_PLAIN);
        check.setFocusPainted(false);
        check.setText(text);
        check.setToolTipText(toolTip);
    }
    
    //Textfiles Login
    public static void styleTextFieldAndPasswordLogin(JTextField txtField, String toolTip){
        txtField.setFont(FONT_PLAIN);
        txtField.setPreferredSize(new Dimension(300, 25));
        txtField.setToolTipText(toolTip);
    }
    
    //Buttons
    public static void styleButton(JButton btn, String iconPath, String tootlTip) {
        btn.setOpaque(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(50, 50));
        btn.setIcon(new ImageIcon(StyleConfig.class.getResource(iconPath)));
        btn.setToolTipText(tootlTip);
        
        btn.addMouseListener(new MouseAdapter (){
            @Override
            public void mouseEntered(MouseEvent e){
                btn.setBorderPainted(true);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btn.setBorder(BorderFactory.createBevelBorder(1));
            }
            
            @Override 
            public void mouseExited(MouseEvent e){
                btn.setBorderPainted(false);
            }
        });
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
    
    //Muestra mensajes en jlabels de la app
    public static void showMessage(JLabel lbl, String text) { 
        lbl.setForeground(StyleConfig.DARK_BLUE_COLOR); 
        lbl.setFont(StyleConfig.FONT_BOLD);
        lbl.setText(text); 
    }
    
    //Logo
    public static void setLogo(JLabel lblLogo, String logoPath){
        lblLogo.setPreferredSize(new Dimension(150, 40));
        lblLogo.setIcon(new ImageIcon(StyleConfig.class.getResource(logoPath)));
    }
    
    //Panel login
    public static JPanel createLoginField(String iconPath, JComponent field) {
        JPanel panel = new JPanel(new MigLayout("insets 0", "[]10[grow]", "[]"));
        panel.setBackground(StyleConfig.DARK_BLUE_COLOR);
        
        JLabel lblIcon = new JLabel(new ImageIcon(StyleConfig.class.getResource(iconPath)));
        panel.add(lblIcon);
        panel.add(field, "growx");
        
        return panel;
    }
    
    //Panel preferences
    public static JPanel createFieldWrapper(JComponent field) {
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", "[]"));
        panel.setOpaque(false);
        panel.add(field, "growx");
        return panel;
    }
    
    //Cambio de cursor en componente
    public static void handCursor(JComponent... components) {
        for (JComponent component : components) {
            component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
    
    //Color seleccion en tabla
    public static void selectionColorTable(JTable table){
        
        table.setSelectionBackground(LIGHT_BLUE_COLOR); 
        table.setSelectionForeground(WHITE_COLOR);

    }
    
    //Color seleccion en lista
    public static void selectionColorList(JList<?> list){
        list.setSelectionBackground(LIGHT_BLUE_COLOR); 
        list.setSelectionForeground(WHITE_COLOR);
    }
    
    //Color seleccion en comboBox
    public static void renderComboBox(JComboBox combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

               if (isSelected) {
                    setBackground(LIGHT_BLUE_COLOR);
                    setForeground(WHITE_COLOR);
                }
                return this;
            }
        });
        
        combo.setEditable(true);
        Component editor = combo.getEditor().getEditorComponent();
        editor.setBackground(WHITE_COLOR);
    }
}
