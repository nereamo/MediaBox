package montoya.mediabox.configUI;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.JTableHeader;

/**
 * Gestiona los distintos estilos visuales de los componentes.
 * No es necesário instanciar esta clase.
 * <p>
 * Personalización de componentes:
 * <ul> 
 * <li> Paneles.</li>
 * <li> Botones y menús.</li>
 * <li> Campos de texto.</li>
 * <li> Listas y tablas.</li>
 * <li> Control de selección.</li>
 * <li> Barras de progreso.</li>
 * </ul> 
 * 
 * 
 * @author Nerea
 */
public class UIStyles {

    /** Fuentes utilizadas en los componentes */
    public static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 16);
    public static final Font FONT_PLAIN = new Font("Arial", Font.PLAIN, 16);

    /** Colores utilizados en los componentes */
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
     * @param scroll JScrollPane que contiene el JComponent
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
        
        UIManager.put("ComboBox.selectionBackground", UIStyles.LIGHT_PURPLE);
    UIManager.put("ComboBox.selectionForeground", Color.WHITE);
    UIManager.put("List.selectionBackground", UIStyles.LIGHT_PURPLE);
    UIManager.put("List.selectionForeground", Color.WHITE);
        combo.setForeground(DARK_GREY_COLOR);
        combo.setBackground(LIGHT_GREY_COLOR);
        combo.setFont(FONT_PLAIN);
        combo.setEditable(false);
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    public static void itemsInCombobox(JComboBox<String> combo, String items) {
        combo.removeAllItems();
        String[] elementos = items.split(","); //Separar por coma
        for (String item : elementos) {
            combo.addItem(item.trim()); //Quita espacios
        }
        combo.setFont(FONT_PLAIN);
    }

    /**
     * Configura una JTextField personalizado. 
     * Aplica borde,color de resalte del borde, color de fondo, color de texto, fuente. 
     * 
     * @param field JTextField a configurar
     * @param leftIconPath Icono izquierdo
     * @param placeholder Texto informativo
     * @param rightIconPath Icono derecho
     * @param leftAction Acción del icono
     */
    public static void styleField(JTextField field, String leftIconPath, String placeholder, String rightIconPath, Runnable leftAction) {
        field.setOpaque(false);
        field.setBorder(UIManager.getBorder("TextField.border"));
        field.setCaretColor(LIGHT_PURPLE);
        field.setBackground(MEDIUM_GREY_COLOR);
        field.setForeground(WHITE_COLOR);
        field.setFont(FONT_PLAIN);
        
        setupLeadingIcon(field, leftIconPath, leftAction); //Icono izquierdo

        if (field instanceof JPasswordField) { //Icono derecho
            setupPasswordVisibility((JPasswordField) field, rightIconPath);
        } else {
            setupClearButton(field, rightIconPath);
        }
        field.putClientProperty("JTextField.placeholderText", placeholder); //Texto de ayuda
        field.putClientProperty("JTextField.arc", 30); //Redondez
    }

    /**
     * Configura el icono izquierdo del JTextField personalizado. 
     * Si el icono es {@code null} es solamente decorativo.
     * Si el icono  no es {@code null}, el icono es clicable y ejecuta una acción.
     * 
     * @param field JTextField con icono a la izquierda
     * @param path Ruta del icono
     * @param action La acción al hacer clic en el icono. Si es {@code null}, el icono es decorativo.
     */
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

        lbl.addMouseListener(new MouseAdapter() { //Listener para el icono izquierdo no decorativo
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
                field.requestFocus();
            }
        });
        lbl.setToolTipText("Paste from clipboard");
    }

    /**
     * Configura el icono de visibilidad del JTextField personalizado. 
     * Permite mostrar la contraseña y ocultarla de nuevo.
     * 
     * @param pf JPasswordField con icono de visibilidad
     * @param path Ruta del icono
     */
    private static void setupPasswordVisibility(final JPasswordField pf, String path) {
        ImageIcon icon = createIcon(path);
        if (icon == null) {
            return;
        }

        JLabel lbl = createClickableIcon(icon);
        pf.putClientProperty("JTextField.trailingComponent", lbl);

        lbl.addMouseListener(new MouseAdapter() { //Listener para hacer visible la contraseña
            @Override
            public void mouseClicked(MouseEvent e) {
                pf.setEchoChar(pf.getEchoChar() == 0 
                        ? '•' //Oculta los caracteres
                        : 0); 
                pf.requestFocus();
            }
        });
        lbl.setToolTipText("Show Password");
    }

    /**
     * Configura el icono de limpieza del JTextField personalizado. 
     * Permite eliminar el contenido del JTextField.
     * 
     * @param field JPasswordField con icono de limpieza
     * @param path Ruta del icono
     */
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
                field.setText(""); //Limpia el campo del field
                field.requestFocus();
            }
        });

        lbl.setToolTipText("Clear field");
    }
    
    /**
     * Configura el icono del JTextFieldpersonalizado.
     * 
     * @param icon Icono mostrado en el JLabel
     * @return Un JLabel como icono clicable
     */
    private static JLabel createClickableIcon(ImageIcon icon) {
        JLabel lbl = new JLabel(icon);
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return lbl;
    }

    /**
     * Configura un JSpinner personalizado.
     * Aplica fuente, color de fondo, color de selección y color de fuente en selección, un borde y cursor de mano.
     * 
     * @param spinner JSpinner a configurar
     */
    public static void styleSpinner(JSpinner spinner) {
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
        spinner.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Configura un JProgressBar personalizado.
     * Aplica color de fondo, color de relleno, fuente, visibilidad del texto y un borde.
     * 
     * @param bar JProgressBar a configurar
     */
    public static void styleProgressBar(JProgressBar bar) {
        bar.setBackground(MEDIUM_GREY_COLOR);
        bar.setForeground(LIGHT_PURPLE); //Color carga
        bar.setFont(FONT_BOLD);
        bar.setStringPainted(true);
        bar.putClientProperty(FlatClientProperties.STYLE, "arc: 999");

        bar.setUI(new com.formdev.flatlaf.ui.FlatProgressBarUI() {
            @Override
            protected Color getSelectionBackground() {
                return Color.BLACK; // Texto cuando está sobre el fondo (gris)
            }

            @Override
            protected Color getSelectionForeground() {
                return Color.BLACK; // Texto cuando está sobre la carga (púrpura)
            }
        });

        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    /**
     * Configura un ImageIcon personalizado.
     * Aplica icono si no es {@code null}.
     * 
     * @param path Ruta del icono
     * @return El icono en el componente
     */
    private static ImageIcon createIcon(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        return new ImageIcon(UIStyles.class.getResource(path));
    }
}
