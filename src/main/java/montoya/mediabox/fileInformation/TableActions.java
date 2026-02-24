package montoya.mediabox.fileInformation;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import montoya.mediabox.panels.InfoMedia;

/**
 * Clase encargada de la interacción con los botones de la columna "Actions" de la tabla.
 * <p>
 * Muestra iconos de reproducir, eliminar o descargar un archivo dependiendo del directorio seleccionado.
 * </p>
 * 
 * @author Nerea
 */
public class TableActions extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    /** Panel que contiene los botones de acción */
    private final JPanel panel;
    
    /** Labels que muetran el icono */
    private final JLabel lblPlay, lblDelete, lblDownload;
    
    /** Tabla asociada */
    private final JTable table;
    
    /** {@link FileProperties} Panel (vista) que muestra la información d elas descargas. */
    private final InfoMedia infoMedia;

    /**
     * Constructor que inicializalos botones y los añade a la columna "Actions"
     * 
     * @param table Tabla donde se muestran los botones
     * @param columnIndex Índice de la columna
     * @param infoMedia Panel que muestra la información de las descargas
     */
    public TableActions(JTable table, int columnIndex, InfoMedia infoMedia) {
        this.table = table;
        this.infoMedia = infoMedia;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 4));
        panel.setOpaque(true);

        //Etiquetas con iconos
        lblPlay = createLabel("/images/play.png");
        lblDelete = createLabel("/images/delete.png");
        lblDownload = createLabel("/images/download.png");

        //Listener para reproducir archivo local
        lblPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onPlay();
            }
        });

        //Listener para eliminar archivo local
        lblDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onDelete();
            }
        });

        //Listener para descargar archivo de la API
        lblDownload.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onDownload();
            }
        });

        //Añadir los JLabel al panel
        panel.add(lblPlay);
        panel.add(lblDelete);
        panel.add(lblDownload);
        
        //Configurar la columna de la tabla
        TableColumn tableColumn = table.getColumnModel().getColumn(columnIndex);
        tableColumn.setCellRenderer(this);
        tableColumn.setCellEditor(this);
    }

    /** Ejecuta la acción de reproducir */
    private void onPlay() {
        stopEditing();
        infoMedia.playSelectedFile();
    }

    /** Ejecuta la acción de eliminar */
    private void onDelete() {
        stopEditing();
        infoMedia.deleteSelectedFile();
    }

    /** Ejecuta la acción de descargar */
    private void onDownload() {
        stopEditing();
        infoMedia.downloadFile();
    }

    /** Detiene la edición de la celda de forma segura */
    private void stopEditing() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (table.isEditing()) {
                    fireEditingStopped();
                }
            }
        });
    }

    /**
     * Crea un JLabel con el icono especificado y estilo de cursor
     * 
     * @param iconPath Ruta del icono
     * @return JLabel configurado con el icono
     */
    private JLabel createLabel(String iconPath) {
        JLabel label = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img)); //Establece icono
        } catch (Exception ex) {
            label.setText("?");
        }
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Establece el cursor
        label.setOpaque(false);
        return label;
    }

    /** Actualiza visibilidad de botones según si es archivo de red o local */
    private void updateActionVisibility() {
        boolean isNetwork = infoMedia.isNetworkFileSelected();
        lblDownload.setVisible(isNetwork);
        lblPlay.setVisible(!isNetwork);
        lblDelete.setVisible(!isNetwork);
    }

    /**
     * Muestra los botones de reproducir, eiliminar o descargar según el directorio seleccionado en la lista
     * 
     * @param table Tabla sonde se muestran los botones
     * @param value Valor de la celda
     * @param isSelected True si la fila está seleccionada
     * @param hasFocus True si la celda tiene el foco
     * @param row Índice de la fila
     * @param column Índice de la columna
     * @return El panel con los botones configurados
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        updateActionVisibility();
        panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        panel.setToolTipText(infoMedia.isNetworkFileSelected() ? "Download file" : "Play / Delete");
        return panel;
    }

    /**
     * Devuelve el componente que se usa como editor de la celda.
     * 
     * @param table Tabla sonde se muestran los botones
     * @param value Valor de la celda
     * @param isSelected True si la fila está seleccionada
     * @param row Índice de la fila
     * @param column Índice de la columna
     * @return El panel con los botones configurados para edición
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        updateActionVisibility();
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    /** @return null ya qu ela celda no tiene valores relevantes*/
    @Override
    public Object getCellEditorValue() {
        return null;
    }

    /**
     * Devuelve el texto de ayuda al pasar el ratón por el panel.
     * 
     * @return El tooltip del JLabel, o null si no hay ninguno
     */
    public String getToolTipText() {
        //Si el panel no es nulo, buscamos qué hay bajo el ratón
        if (panel != null) {
            PointerInfo pi = MouseInfo.getPointerInfo();
            Point p = pi.getLocation();
            SwingUtilities.convertPointFromScreen(p, panel);

            Component c = panel.getComponentAt(p);
            if (c instanceof JLabel) {
                return ((JLabel) c).getToolTipText();
            }
        }
        return null;
    }
}
