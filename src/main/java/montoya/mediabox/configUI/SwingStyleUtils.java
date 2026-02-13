package montoya.mediabox.configUI;

import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicPasswordFieldUI;

/**
 *
 * @author Nerea
 */
public class SwingStyleUtils {

    //Fonts
    public static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 16);
    public static final Font FONT_PLAIN = new Font("Arial", Font.PLAIN, 16);

    //Colors
    public static final Color GREY_COLOR = new Color(181, 182, 189);
    public static final Color WHITE_COLOR = new Color(255, 255, 255);
    public static final Color LIGHT_BLUE_COLOR = new Color(135, 139, 222);
    public static final Color DARK_BLUE_COLOR = new Color(46, 51, 94);

    //Estilo para los titleBorders
    public static void createTitleBorder(JPanel panel, String title) {
        Border border = BorderFactory.createLineBorder(LIGHT_BLUE_COLOR);
        TitledBorder titBorder = BorderFactory.createTitledBorder(border, title);

        titBorder.setTitleColor(LIGHT_BLUE_COLOR);
        titBorder.setTitleFont(FONT_BOLD);

        panel.setOpaque(false);
        panel.setBorder(titBorder);
    }

    //Estilo para CheckBox
    public static void styleCheckBox(JCheckBox check, String text, String toolTip) {
        check.setOpaque(false);
        check.setForeground(LIGHT_BLUE_COLOR);
        check.setFont(FONT_PLAIN);
        check.setFocusPainted(false);
        check.setText(text);
        check.setToolTipText(toolTip);
    }

    //Estilo para Buttongroup
    public static void styleButtonGroup(String toolTip, JRadioButton... radio) {
        for (JRadioButton rb : radio) {
            rb.setOpaque(true);
            rb.setFocusPainted(false);
            rb.setForeground(LIGHT_BLUE_COLOR);
            rb.setBackground(DARK_BLUE_COLOR);
            rb.setFont(FONT_PLAIN);
            rb.setToolTipText(toolTip);
        }
    }

    //Estilo para los botones que SI tienen icono
    public static void styleIconButton(JButton btn, String iconPath, String tootlTip) {
        btn.setOpaque(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(50, 50));
        btn.setIcon(new ImageIcon(SwingStyleUtils.class.getResource(iconPath)));
        btn.setToolTipText(tootlTip);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBorderPainted(true);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btn.setBorder(BorderFactory.createBevelBorder(1));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBorderPainted(false);
            }
        });
    }

    //Estilo para los botones que NO tienen icono
    public static void styleSimpleButton(JButton btn, String txt, String tootlTip, int width, int height, Color fondo, Color letras) {
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setText(txt);
        btn.setBackground(fondo);
        btn.setForeground(letras);
        btn.setFont(FONT_BOLD);
        btn.setToolTipText(tootlTip);
        btn.setPreferredSize(new Dimension(width, height));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBorderPainted(true);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btn.setBorder(BorderFactory.createBevelBorder(1));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBorderPainted(false);
            }
        });
    }

    //Estilo de los items del MenuBar
    public static void styleMenuItems(JMenuItem item, String iconPath, String text, String toolTip) {
        item.setText(text == null ? "" : text);
        item.setOpaque(false);
        item.setContentAreaFilled(false);
        item.setBorderPainted(false);
        item.setPreferredSize(new Dimension(90, 50));
        item.setIcon(new ImageIcon(SwingStyleUtils.class.getResource(iconPath)));
        item.setToolTipText(toolTip);
    }

    //Estilo de los mensajes mostrados en aplicación
    public static void showMessageInfo(JLabel lbl, String text) {
        lbl.setForeground(LIGHT_BLUE_COLOR);
        lbl.setFont(FONT_BOLD);
        lbl.setText(text);
    }

    //Estilo de los Labels estáticos como los del Panel Preferences
    public static void styleFixLabel(JLabel label, String text) {
        label.setOpaque(true);
        label.setText(text);
        label.setForeground(LIGHT_BLUE_COLOR);
        label.setBackground(DARK_BLUE_COLOR);
        label.setFont(FONT_PLAIN);
    }

    //Estilo del Logo
    public static void setLogo(JLabel lblLogo, String logoPath) {
        lblLogo.setPreferredSize(new Dimension(150, 40));
        lblLogo.setIcon(new ImageIcon(SwingStyleUtils.class.getResource(logoPath)));
    }

    //Estilo del cursor sobre componentes
    public static void handCursor(JComponent... components) {
        for (JComponent component : components) {
            component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    //Color seleccion en tabla
    public static void selectionColorTable(JTable table) {
        table.setSelectionBackground(LIGHT_BLUE_COLOR);
        table.setSelectionForeground(WHITE_COLOR);
    }

    //Color seleccion en lista
    public static void selectionColorList(JList<?> list) {
        list.setSelectionBackground(LIGHT_BLUE_COLOR);
        list.setSelectionForeground(WHITE_COLOR);
    }

    //Color seleccion en comboBox
    public static void selectionColorComboBox(JComboBox combo) {
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
    }

    //Iconos en JTextFields
    public static void addIconsTextField(JTextField txtField, String leftIconPath, String rightIconPath, String toolTip) {
        ImageIcon leftIcon = new ImageIcon(SwingStyleUtils.class.getResource(leftIconPath));
        ImageIcon rightIcon = new ImageIcon(SwingStyleUtils.class.getResource(rightIconPath));

        txtField.setBorder(BorderFactory.createCompoundBorder(txtField.getBorder(), BorderFactory.createEmptyBorder(0, 30, 0, 30)));
        txtField.setFont(FONT_PLAIN);
        txtField.setPreferredSize(new Dimension(300, 30));
        txtField.setToolTipText(toolTip);

        txtField.setUI(new javax.swing.plaf.basic.BasicTextFieldUI() {
            @Override
            protected void paintBackground(java.awt.Graphics g) {
                super.paintBackground(g);

                // Icono Izquierdo (Pegar)
                int yLeft = (txtField.getHeight() - leftIcon.getIconHeight()) / 2;
                leftIcon.paintIcon(txtField, g, 2, yLeft);

                // Icono Derecho (Borrar)
                int yRight = (txtField.getHeight() - rightIcon.getIconHeight()) / 2;
                int xRight = txtField.getWidth() - rightIcon.getIconWidth() - 2;
                rightIcon.paintIcon(txtField, g, xRight, yRight);
            }
        });
        addPlaceholder(txtField, toolTip);
    }

    //Iconos en JPasswordFields
    public static void addIconsPasswordField(JPasswordField pwdField, String leftIconPath, String rightIconPath, String toolTip) {
        ImageIcon leftIcon = new ImageIcon(SwingStyleUtils.class.getResource(leftIconPath));
        ImageIcon rightIcon = new ImageIcon(SwingStyleUtils.class.getResource(rightIconPath));

        pwdField.setBorder(BorderFactory.createCompoundBorder(pwdField.getBorder(), BorderFactory.createEmptyBorder(0, 30, 0, 30)));
        pwdField.setFont(FONT_PLAIN);
        pwdField.setPreferredSize(new Dimension(300, 30));
        pwdField.setToolTipText(toolTip);

        pwdField.setUI(new BasicPasswordFieldUI() {
            @Override
            protected void paintBackground(Graphics g) {
                super.paintBackground(g);
                int yLeft = (pwdField.getHeight() - leftIcon.getIconHeight()) / 2;
                leftIcon.paintIcon(pwdField, g, 2, yLeft);

                int yRight = (pwdField.getHeight() - rightIcon.getIconHeight()) / 2;
                int xRight = pwdField.getWidth() - rightIcon.getIconWidth() - 2;
                rightIcon.paintIcon(pwdField, g, xRight, yRight);
            }
        });

        addPlaceholder(pwdField, toolTip);
    }

    private static void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(java.awt.Color.GRAY); // Color del texto fantasma

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                // Si el texto es el placeholder, lo borramos para que el usuario escriba
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(java.awt.Color.BLACK); // O el color que uses
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                // Si el usuario no escribió nada, restauramos el placeholder
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(java.awt.Color.GRAY);
                }
            }
        });
    }
    
    //Estilo para el spinner
    public static void styleSpinner(JSpinner spinner) {
        spinner.setFont(FONT_PLAIN);
        spinner.setOpaque(false);

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setBackground(WHITE_COLOR);
            textField.setForeground(DARK_BLUE_COLOR);
            textField.setCaretColor(WHITE_COLOR); // Color del cursor al escribir
            textField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        }

        spinner.setBorder(BorderFactory.createLineBorder(LIGHT_BLUE_COLOR, 1));
    }
}
