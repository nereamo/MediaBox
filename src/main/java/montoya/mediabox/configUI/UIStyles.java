package montoya.mediabox.configUI;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.JTableHeader;

/**
 * Gestiona los distintos estilos de los componentes.
 * 
 * @author Nerea
 */
public class UIStyles {

    /**
     * Fuentes utilizadas en los componentes
     */
    public static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 16);
    public static final Font FONT_PLAIN = new Font("Arial", Font.PLAIN, 16);

    /**
     * Colores utilizados en los componentes
     */
    public static final Color WHITE_COLOR = new Color(255, 255, 255);
    public static final Color LIGHT_PURPLE = new Color(126, 134, 204);
    public static final Color LIGHT_GREY_COLOR = new Color(220, 220, 222);
    public static final Color MEDIUM_GREY_COLOR = new Color(72, 72, 74);
    public static final Color DARK_GREY_COLOR = new Color(37, 37, 38);
    public static final Color BLACK_COLOR = new Color(15, 15, 15);

    /**
     * Configuración de los bordes en los paneles con esquinas redondeadas.
     * Aplica color de fondo, radio en esquinas y hace panel no opaco.
     * 
     * @param panel JPanel al que se aplica el borde y apariencia
     * @param color Color de fondo del panel
     * @param radius Radio de las esquinas
     * @see paintRounded
     */
    public static void panelsBorders(JPanel panel, Color color, int radius) {
        panel.setOpaque(false);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: " + radius);
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    /**
     * Configura un JLabel que contiene texto informativo para las acciones realizadas por le usuario.
     * Aplica color de texto, fuente y texto.
     * 
     * @param lbl JLabel a configurar
     * @param text Texto que se mostrará en el JLabel
     */
    public static void showMessageInfo(JLabel lbl, String text) {
        lbl.setForeground(LIGHT_PURPLE);
        lbl.setFont(FONT_BOLD);
        lbl.setText(text);
    }

    /**
     * Configura un JLabel que contiene un texto fijo como títulos o etiuqetas de controles.
     * Aplica opacidad, color de fondo, color de texto, fuente, texto y un posible icono.
     * 
     * @param label JLabel a configurar
     * @param text Texto que se mostrará en el JLabel
     * @param iconPath Ruta del icono a mostrar; puede ser null si no debe tener icono
     */
    public static void styleFixLabel(JLabel label, String text, String iconPath) {
        
        label.setOpaque(true);
        label.setBackground(DARK_GREY_COLOR);
        label.setForeground(LIGHT_GREY_COLOR);
        label.setFont(FONT_PLAIN);
        label.setText(text);
        label.setIcon(createIcon(iconPath));
    }

    /**
     * Configura un JCheckBox.
     * Aplica color de texto, fuente, texto y un mensaje de ayuda.
     * 
     * @param check JCheckBox a configurar
     * @param text Texto que se mostrará en el JCheckBox
     * @param toolTip Texto de ayuda para el usuario
     */
    public static void styleCheckBox(JCheckBox check, String text, String toolTip) {
        check.setOpaque(false);
        check.setFocusPainted(false);
        check.setForeground(LIGHT_GREY_COLOR);
        check.setFont(FONT_PLAIN);
        check.setText(text);
        check.setToolTipText(toolTip);
    }

    /**
     * Configura un ButtonGroup.
     * Aplica opacidad, color de fondo, color de texto, fuente, y un mensaje de ayuda.
     * 
     * @param toolTip Texto de ayuda para el usuario
     * @param radioButton JRadioButtons a configurar
     */
    public static void styleButtonGroup(String toolTip, JRadioButton... radioButton) {
        for (JRadioButton rb : radioButton) {
            rb.setOpaque(true);
            rb.setFocusPainted(false);
            rb.setBackground(MEDIUM_GREY_COLOR);
            rb.setForeground(LIGHT_GREY_COLOR);
            rb.setFont(FONT_PLAIN);
            rb.setToolTipText(toolTip);
        }
    }

    /**
     * Configura un JButton personalizado.
     * Aplica color de fondo, color de texto, icono, color de fondo, color de texto, si está habilitado y un mensaje de ayuda.
     * 
     * @param btn JButton a configurar
     * @param text Texto que se mostrará en el JButton
     * @param iconPath Ruta del icono a mostrar; puede ser null si no debe tener icono
     * @param backGround Color de fondo del JButton
     * @param foreGround Color del texto del JButton
     * @param enabled Si el botón estará habilitado 
     * @param tootlTip Texto de ayuda para el usuario
     */
    public static void styleButtons(JButton btn, String text, String iconPath, Color backGround, Color foreGround, boolean enabled, String tootlTip) {

        btn.setText(text);
        btn.setBackground(backGround);
        btn.setForeground(foreGround);
        btn.setEnabled(enabled);
        btn.setToolTipText(tootlTip);
        btn.setFont(FONT_BOLD);
        btn.setIcon(createIcon(iconPath));
        
        
        btn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        btn.putClientProperty("JButton.arc", 5);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Configura un JMenuItem personalizado.
     * Aplica tamaño, texto, icono, un mensaje de ayuda y cambia el icono (color) al seleccionar un ítem.
     * 
     * @param item JMenuItem a configurar
     * @param text Texto que se mostrará en el JButton
     * @param iconPath Ruta del icono a mostrar
     * @param toolTip Texto de ayuda para el usuario
     */
    public static void styleMenuAndItems(JMenuItem item, String text, String iconPath, String toolTip, String replaceIcon) {
        item.setOpaque(false);
        item.setContentAreaFilled(false);
        item.setBorderPainted(false);
        item.setPreferredSize(new Dimension(90, 50));

        item.setText(text == null
                ? ""
                : text);

        Icon normalIcon = createIcon(iconPath);
        item.setIcon(normalIcon);

        String blackIconPath = iconPath.replace(iconPath, replaceIcon); //Remplaza el icono al ser seleccionado el item
        item.setSelectedIcon(createIcon(blackIconPath));
        item.setRolloverIcon(createIcon(blackIconPath));
    }

    /**
     * Configura un JComponent personalizado y un JScroll.
     * Aplica color de seleccion, color de texto en la selección, color de fondo, JScroll al que pertenece y cursor de mano.
     * Si es una JTable, aplica el estilo para el encabezado.
     * 
     * @param component JComponent a configurar
     * @param scroll JScrollPane JScrollPane que contiene el JComponent
     */
    public static void styleScrollComponent(JComponent component, JScrollPane scroll) {
        component.setBackground(LIGHT_GREY_COLOR);
        component.setForeground(MEDIUM_GREY_COLOR);
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        if (component instanceof JTable) { //Configuración para la tabla
            JTable table = (JTable) component;
            table.setSelectionBackground(LIGHT_PURPLE);
            table.setSelectionForeground(WHITE_COLOR);

            JTableHeader header = table.getTableHeader();
            header.setPreferredSize(new Dimension(header.getWidth(), 30));
            header.setFont(FONT_BOLD);
            header.setBackground(LIGHT_GREY_COLOR);
            header.setForeground(MEDIUM_GREY_COLOR);

        } else if (component instanceof JList) { //Configuración para la lista
            JList<?> list = (JList<?>) component;
            list.setSelectionBackground(LIGHT_PURPLE);
            list.setSelectionForeground(WHITE_COLOR);
            list.setFont(FONT_PLAIN);
        }
        
        //Redondez del borde
        scroll.putClientProperty( FlatClientProperties.STYLE, "arc: 20; borderColor: #a6a6a6; borderWidth: 0" ); 
    }

    /**
     * Configura una JComboBox personalizado.
     * Aplica esquinas redondeadas y elimina el borde por defecto.
     * 
     * @param combo JComboBox a configurar
     */
    public static void styleComboBox(JComboBox combo) {

        combo.setRenderer(new DefaultListCellRenderer() { //Cambia el color de fondo y de texto cuando un ítem está seleccionado
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (isSelected) {
                    setBackground(LIGHT_PURPLE);
                    setForeground(WHITE_COLOR);
                }
                return this;
            }
        });

        combo.setForeground(DARK_GREY_COLOR);
        combo.setBackground(LIGHT_GREY_COLOR);
        combo.setFont(FONT_PLAIN);
        combo.setEditable(false);
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Configura una JScrollPane personalizado. Aplica color de seleccion, color de texto en la selección, color de fondo, fuente, si es editable y cursor de mano.
     * 
     * @param field
     * @param leftIconPath
     * @param placeholder
     * @param rightIconPath
     * @param leftAction 
     */
    public static void styleField(JTextField field, String leftIconPath, String placeholder, String rightIconPath, Runnable leftAction) {
        field.setOpaque(false);
        field.setBorder(UIManager.getBorder("TextField.border"));
        field.setBackground(MEDIUM_GREY_COLOR);
        field.setForeground(WHITE_COLOR);
        field.setCaretColor(LIGHT_PURPLE);
        field.setFont(FONT_PLAIN);
        
        setupLeadingIcon(field, leftIconPath, leftAction); //Icono izquierdo

        if (field instanceof JPasswordField) { //Icono derecho
            setupPasswordVisibilityButton((JPasswordField) field, rightIconPath);
        } else {
            setupClearButton(field, rightIconPath);
        }
        field.putClientProperty("JTextField.placeholderText", placeholder); //Texto de ayuda
        field.putClientProperty("JTextField.arc", 30); //Redondez
    }

    private static void setupLeadingIcon(final JTextField field, String path, final Runnable action) {
        ImageIcon icon = createIcon(path);
        if (icon == null) {
            return;
        }

        if (action == null) { //Icono decorativo
            field.putClientProperty("JTextField.leadingIcon", icon);
            return;
        }

        JLabel lbl = createClickableIcon(icon); //Icono con acción
        field.putClientProperty("JTextField.leadingComponent", lbl);

        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
                field.requestFocus();
            }
        });
        lbl.setToolTipText("Paste from clipboard");
    }

    private static void setupPasswordVisibilityButton(final JPasswordField pf, String path) {
        ImageIcon icon = createIcon(path);
        if (icon == null) {
            return;
        }

        JLabel lbl = createClickableIcon(icon);
        pf.putClientProperty("JTextField.trailingComponent", lbl);

        lbl.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            pf.setEchoChar(pf.getEchoChar() == 0 ? '•' : 0);
            pf.requestFocus();
        }
    });
        lbl.setToolTipText("Show Password");
    }

    private static void setupClearButton(final JTextField field, String path) {
        ImageIcon icon = createIcon(path);
        if (icon == null) {
            return;
        }

        JLabel lbl = createClickableIcon(icon);
        field.putClientProperty("JTextField.trailingComponent", lbl);

        lbl.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            field.setText("");
            field.requestFocus();
        }
    });

        lbl.setToolTipText("Clear field");
    }
    
    private static JLabel createClickableIcon(ImageIcon icon) {
    JLabel lbl = new JLabel(icon);
    lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    return lbl;
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
        spinner.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
    }


    // ================== UTILIDADES PRIVADAS==================
    private static ImageIcon createIcon(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        return new ImageIcon(UIStyles.class.getResource(path));
    }



    
    
}
