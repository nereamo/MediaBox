package montoya.mediabox.configUI;

import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;
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
    public static final Color WHITE_COLOR = new Color(255, 255, 255);
    public static final Color LIGHT_PURPLE = new Color(126, 134, 204);
    public static final Color LIGHT_GREY_COLOR = new Color(220, 220, 222);
    public static final Color MEDIUM_GREY_COLOR = new Color(72, 72, 74);
    public static final Color DARK_GREY_COLOR = new Color(37, 37, 38);
    public static final Color BLACK_COLOR = new Color(15, 15, 15);
    
    //Estilo de los bordes
    public static void createTitleBorder(JPanel panel, Color color) {
        Border border = BorderFactory.createLineBorder(color, 2, true);
        panel.setBackground(color);
        panel.setOpaque(true);
        panel.setBorder(border);
    }

    //Estilo para CheckBox
    public static void styleCheckBox(JCheckBox check, String text, String toolTip) {
        check.setOpaque(false);
        check.setForeground(LIGHT_GREY_COLOR);
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
            rb.setForeground(LIGHT_GREY_COLOR);
            rb.setBackground(MEDIUM_GREY_COLOR);
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
    public static void styleTextButton(JButton btn, String txt, String tootlTip, int width, int height, Color fondo, Color letras) {
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        UIManager.put("Button.select", fondo);
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
    
    //Estilo para los botones que SI tienen icono
    public static void styleIconAndTextButton(JButton btn, String iconPath, String Text, String tootlTip, Color fondo, Color letras) {
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(50, 50));
        btn.setIcon(new ImageIcon(SwingStyleUtils.class.getResource(iconPath)));
        btn.setText(Text);
        btn.setFont(FONT_BOLD);
        btn.setToolTipText(tootlTip);
        btn.setBackground(fondo);
        btn.setForeground(letras);

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
    
    public static void styleGhostButton(JButton btn, String txt, String toolTip, int width, int height, Color fondo, Color letras) {
    // 1. Reutilizamos el estilo base (o lo configuramos manualmente aquí)
    styleTextButton(btn, txt, toolTip, width, height, fondo, letras);
    
    // 2. Configuramos la transparencia especial
    btn.setOpaque(false);
    btn.setContentAreaFilled(false);
    
    btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
        @Override
        public void update(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (c.isEnabled()) {
                // Estado activo: sólido
                g2.setColor(c.getBackground()); 
                btn.setForeground(letras);
            } else {
                // Estado inactivo: transparencia sutil
                Color bg = c.getBackground();
                g2.setColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 60)); // Fondo transparente
                btn.setForeground(new Color(letras.getRed(), letras.getGreen(), letras.getBlue(), 40)); // Texto transparente
            }
            
            g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            g2.dispose();
            super.update(g, c);
        }
    });

    btn.setEnabled(false); // Por defecto nace desactivado
    btn.setVisible(true);
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
        lbl.setForeground(LIGHT_PURPLE);
        lbl.setFont(FONT_BOLD);
        lbl.setText(text);
    }

    //Estilo de los Labels estáticos como los del Panel Preferences
    public static void styleFixLabel(JLabel label, String text) {
        label.setOpaque(true);
        label.setText(text);
        label.setForeground(LIGHT_GREY_COLOR);
        label.setBackground(DARK_GREY_COLOR);
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
        table.setSelectionBackground(LIGHT_PURPLE);
        table.setSelectionForeground(WHITE_COLOR);
        table.setBackground(LIGHT_GREY_COLOR);
    }

    //Color seleccion en lista
    public static void selectionColorList(JList<?> list) {
        list.setBackground(LIGHT_GREY_COLOR);
        list.setSelectionBackground(LIGHT_PURPLE);
        list.setSelectionForeground(WHITE_COLOR);
        list.setFont(FONT_PLAIN);
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
                    setBackground(LIGHT_PURPLE);
                    setForeground(WHITE_COLOR);
                }
                return this;
            }
        });

        combo.setBackground(LIGHT_GREY_COLOR);
        combo.setFont(FONT_PLAIN);
        combo.setEditable(false);
    }
    
    //Estilo de los JTextFields que no tienen botones
    public static void styleTextFields(JTextField txtField, String toolTip){
        txtField.setBackground(MEDIUM_GREY_COLOR);
        txtField.setForeground(WHITE_COLOR);
        txtField.setFont(FONT_PLAIN);
        
        Border line = BorderFactory.createLineBorder(new Color(100, 100, 100), 1); // Gris más discreto
        Border margin = BorderFactory.createEmptyBorder(0, 30, 0, 30);
        txtField.setBorder(BorderFactory.createCompoundBorder(line, margin));
        
        addPlaceholder(txtField, toolTip);
    }

    //Iconos en JTextFields
    public static void addIconsTextField(JTextField txtField, String leftIconPath, String rightIconPath, String toolTip) {
        ImageIcon leftIcon = new ImageIcon(SwingStyleUtils.class.getResource(leftIconPath));
        ImageIcon rightIcon = new ImageIcon(SwingStyleUtils.class.getResource(rightIconPath));
        
        Border line = BorderFactory.createLineBorder(new Color(100, 100, 100), 1); // Gris más discreto
        Border margin = BorderFactory.createEmptyBorder(0, 30, 0, 30);
        txtField.setBorder(BorderFactory.createCompoundBorder(line, margin));

        txtField.setBorder(BorderFactory.createCompoundBorder(txtField.getBorder(), BorderFactory.createEmptyBorder(0, 30, 0, 30)));
        txtField.setFont(FONT_PLAIN);
        txtField.setBackground(MEDIUM_GREY_COLOR);
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
        
        Border line = BorderFactory.createLineBorder(new Color(100, 100, 100), 1); // Gris más discreto
        Border margin = BorderFactory.createEmptyBorder(0, 30, 0, 30);
        pwdField.setBorder(BorderFactory.createCompoundBorder(line, margin));

        pwdField.setBorder(BorderFactory.createCompoundBorder(pwdField.getBorder(), BorderFactory.createEmptyBorder(0, 30, 0, 30)));
        pwdField.setFont(FONT_PLAIN);
        pwdField.setBackground(MEDIUM_GREY_COLOR);
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
        field.setForeground(java.awt.Color.GRAY);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setFont(FONT_PLAIN);
                    field.setText("");
                    field.setForeground(WHITE_COLOR);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(LIGHT_GREY_COLOR);
                }
            }
        });
    }
    
    //Estilo para el spinner
    public static void styleSpinner(JSpinner spinner) {
        spinner.setFont(FONT_PLAIN);
        spinner.setOpaque(false);
        spinner.setFont(FONT_PLAIN);

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setFont(FONT_PLAIN);
            textField.setBackground(LIGHT_GREY_COLOR);
            textField.setForeground(DARK_GREY_COLOR);
            textField.setCaretColor(LIGHT_GREY_COLOR);
            textField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        }
        spinner.setBorder(BorderFactory.createLineBorder(DARK_GREY_COLOR, 1));
    }
    
    //Estilo para progressBar
    public static void styleProgressBar(JProgressBar bar) {
        bar.setBackground(MEDIUM_GREY_COLOR);
        bar.setForeground(LIGHT_PURPLE);
        bar.setFont(FONT_BOLD);
        bar.setStringPainted(true);

        Border line = BorderFactory.createLineBorder(new Color(100, 100, 100), 1);
        bar.setBorder(line);
        bar.setBorderPainted(true);
        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
    }
}
