package montoya.mediabox.configUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Nerea
 */
public class UIStyles {

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

    //================== PANELES ==================
    public static void panelsBorders(JPanel panel, Color color, int radius) {
        panel.setOpaque(false);
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.setUI(new javax.swing.plaf.basic.BasicPanelUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                paintRounded(g, c, radius);
                super.paint(g, c);
            }
        });
    }

    //================== LABELS ==================
    //Label que muestra información
    public static void showMessageInfo(JLabel lbl, String text) {
        lbl.setForeground(LIGHT_PURPLE);
        lbl.setFont(FONT_BOLD);
        lbl.setText(text);
    }

    //Label estático
    public static void styleFixLabel(JLabel label, String text, String logoPath) {
        label.setOpaque(true);
        label.setText(text);
        label.setForeground(LIGHT_GREY_COLOR);
        label.setBackground(DARK_GREY_COLOR);
        label.setFont(FONT_PLAIN);
        label.setIcon(createIcon(logoPath));
    }

    //================== CHECKBOX ==================
    public static void styleCheckBox(JCheckBox check, String text, String toolTip) {
        check.setOpaque(false);
        check.setForeground(LIGHT_GREY_COLOR);
        check.setFont(FONT_PLAIN);
        check.setFocusPainted(false);
        check.setText(text);
        check.setToolTipText(toolTip);
    }

    //================== BUTTON GROUP ==================
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

    //================== BUTTONS ==================
    public static void styleButtons(JButton btn, String iconPath, String Text, String tootlTip, Color fondo, Color letras, boolean enabled) {

        btn.setText(Text);
        btn.setFont(FONT_BOLD);

        ImageIcon normalIcon = createIcon(iconPath);
        btn.setIcon(normalIcon);
        btn.setDisabledIcon(normalIcon);

        btn.setBackground(fondo);
        btn.setForeground(letras);
        btn.setToolTipText(tootlTip);
        btn.setEnabled(enabled);

        btn.setOpaque(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBorderPainted(true);
                    btn.setBorder(BorderFactory.createBevelBorder(1));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        });

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {

                Graphics2D g2 = (Graphics2D) g.create();

                if (!c.isEnabled()) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.20f));
                }

                paintRounded(g2, c, 20);
                super.paint(g2, c);
                g2.dispose();
            }
        });
        handCursor(btn);
    }

    //================== MENU ITEMS ==================
    public static void styleMenuItems(JMenuItem item, String iconPath, String text, String toolTip) {
        item.setText(text == null ? "" : text);
        item.setOpaque(false);
        item.setContentAreaFilled(false);
        item.setBorderPainted(false);
        item.setPreferredSize(new Dimension(90, 50));
        item.setIcon(createIcon(iconPath));
        item.setToolTipText(toolTip);
    }

    //================== LISTAS, TABLAS Y COMBOBOX ==================
    public static void selectionColorTable(JTable table, JScrollPane scroll) {
        table.setSelectionBackground(LIGHT_PURPLE);
        table.setSelectionForeground(WHITE_COLOR);
        table.setBackground(WHITE_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setFont(UIStyles.FONT_BOLD);
        header.setBackground(UIStyles.LIGHT_GREY_COLOR);

        styleViewScroll(scroll);
        handCursor(table);
    }

    public static void selectionColorList(JList<?> list, JScrollPane scroll) {
        list.setBackground(LIGHT_GREY_COLOR);
        list.setSelectionBackground(LIGHT_PURPLE);
        list.setSelectionForeground(WHITE_COLOR);
        list.setFont(FONT_PLAIN);
        styleViewScroll(scroll);
        handCursor(list);
    }

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
        handCursor(combo);
    }

    //================== JSCROLL ==================
    public static void styleRoundedScroll(JScrollPane scroll, int radius, Color backgroundColor) {
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        scroll.setUI(new javax.swing.plaf.basic.BasicScrollPaneUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), radius, radius);

                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    //================== TEXT Y PASSWORD ==================
    public static void addIconsTextField(JTextField txtField, String leftIconPath, String rightIconPath, String text) {
        ImageIcon leftIcon = new ImageIcon(UIStyles.class.getResource(leftIconPath));
        ImageIcon rightIcon = new ImageIcon(UIStyles.class.getResource(rightIconPath));
        configureCommonTextFieldProps(txtField, text);

        if (txtField instanceof JPasswordField) {
            txtField.setUI(new BasicPasswordFieldUI() {
                @Override
                public void update(Graphics g, JComponent c) {
                    paintRounded(g, c, 15);
                    paintSafely(g);
                }

                @Override
                protected void paintSafely(Graphics g) {
                    paintFieldIcons(g, txtField, leftIcon, rightIcon);
                    super.paintSafely(g);
                }
            });
        } else {
            txtField.setUI(new javax.swing.plaf.basic.BasicTextFieldUI() {
                @Override
                public void update(Graphics g, JComponent c) {
                    paintRounded(g, c, 15);
                    paintSafely(g);
                }

                @Override
                protected void paintSafely(Graphics g) {
                    paintFieldIcons(g, txtField, leftIcon, rightIcon);
                    super.paintSafely(g);
                }
            });
        }
        addPlaceholder(txtField, text);
    }

    //================== SPINNER ==================
    public static void styleSpinner(JSpinner spinner) {
        spinner.setFont(FONT_PLAIN);
        spinner.setOpaque(false);

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
        handCursor(spinner);
    }

    //================== PROGRESSBAR ==================
    public static void styleProgressBar(JProgressBar bar) {
        
        UIManager.put("ProgressBar.selectionBackground", BLACK_COLOR);
        UIManager.put("ProgressBar.selectionForeground", BLACK_COLOR);
        bar.setBackground(MEDIUM_GREY_COLOR);
        bar.setForeground(LIGHT_PURPLE);
        bar.setFont(FONT_BOLD);
        bar.setStringPainted(true);

        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = c.getWidth();
                int height = c.getHeight();
                int arc = height;

                double percent = bar.getPercentComplete();
                if (percent > 0) {
                    int progressWidth = (int) (width * percent);
                    g2.setColor(LIGHT_PURPLE);
                    g2.fillRoundRect(0, 0, progressWidth, height, arc, arc);
                }

                if (bar.isStringPainted()) {
                    g2.setColor(BLACK_COLOR);
                    paintString(g, 0, 0, width, height, 0, c.getInsets());
                }
                g2.dispose();
            }
        });
    }

    //================== CURSOR ==================
    public static void handCursor(JComponent... components) {
        for (JComponent component : components) {
            component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    // ================== UTILIDADES PRIVADAS==================
    private static ImageIcon createIcon(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        return new ImageIcon(UIStyles.class.getResource(path));
    }

    private static void styleViewScroll(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(LIGHT_GREY_COLOR, 1));
        scroll.getViewport().setBackground(DARK_GREY_COLOR);
    }

    //Propiedades de los fields
    private static void configureCommonTextFieldProps(JTextField field, String text) {
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        field.setBackground(MEDIUM_GREY_COLOR);
        field.setForeground(WHITE_COLOR);
        field.setCaretColor(WHITE_COLOR);
        field.setFont(FONT_PLAIN);
        field.setToolTipText(text);
    }

    //Texto mostrado en txtFiels y PasswordFields
    private static void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

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
                    field.setForeground(Color.GRAY);
                } else {
                    field.setForeground(WHITE_COLOR); // Si hay una URL, se queda BLANCO
                }
            }
        });
    }
    
    public static void resetPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(java.awt.Color.GRAY);
        field.getParent().requestFocusInWindow();
    }

    // Método centralizado para pintar los iconos
    private static void paintFieldIcons(Graphics g, JComponent c, ImageIcon leftIcon, ImageIcon rightIcon) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Icono Izquierdo
        int yLeft = (c.getHeight() - leftIcon.getIconHeight()) / 2;
        leftIcon.paintIcon(c, g2, 10, yLeft);

        // Icono Derecho
        int yRight = (c.getHeight() - rightIcon.getIconHeight()) / 2;
        int xRight = c.getWidth() - rightIcon.getIconWidth() - 10;
        rightIcon.paintIcon(c, g2, xRight, yRight);

        g2.dispose();
    }

    //Bordes redondeados
    private static void paintRounded(Graphics g, JComponent c, int arc) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getBackground());
        g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), arc, arc);
        g2.dispose();
    }
}
