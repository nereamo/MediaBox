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
 * Clase encargada de la interacción con los botones de la columna actions
 *
 * @author Nerea
 */
public class TableActions extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private final JPanel panel;
    private final JLabel lblPlay, lblDelete, lblDownload;
    private final JTable table;
    private final InfoMedia infoMedia;

    public TableActions(JTable table, int column, InfoMedia infoMedia) {
        this.table = table;
        this.infoMedia = infoMedia;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 4));
        panel.setOpaque(true);

        lblPlay = createLabel("/images/play.png");
        lblDelete = createLabel("/images/delete.png");
        lblDownload = createLabel("/images/download.png");

        lblPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onPlay();
            }
        });

        lblDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onDelete();
            }
        });

        lblDownload.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onDownload();
            }
        });

        panel.add(lblPlay);
        panel.add(lblDelete);
        panel.add(lblDownload);

        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        tableColumn.setCellRenderer(this);
        tableColumn.setCellEditor(this);
    }

    // ---------------- Actions ----------------
    private void onPlay() {
        stopEditing();
        infoMedia.playSelectedFile();
    }

    private void onDelete() {
        stopEditing();
        infoMedia.deleteSelectedFile();
    }

    private void onDownload() {
        stopEditing();
        infoMedia.downloadFile();
    }

    // Método auxiliar para cerrar el editor suavemente
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

    // ---------------- Helpers ----------------
    private JLabel createLabel(String iconPath) {
        JLabel label = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            label.setText("?");
        }
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setOpaque(false);
        return label;
    }

    private void updateActionVisibility() {
        boolean isNetwork = infoMedia.isNetworkFileSelected();
        lblDownload.setVisible(isNetwork);
        lblPlay.setVisible(!isNetwork);
        lblDelete.setVisible(!isNetwork);
    }

    // ---------------- Renderer ----------------
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        updateActionVisibility();
        panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        panel.setToolTipText(infoMedia.isNetworkFileSelected() ? "Download file" : "Play / Delete");
        return panel;
    }

    // ---------------- Editor ----------------
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        updateActionVisibility();
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    public String getToolTipText() {
        // Si el panel no es nulo, buscamos qué hay bajo el ratón
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
