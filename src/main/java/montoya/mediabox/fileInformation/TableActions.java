
package montoya.mediabox.fileInformation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private final JButton btnPlay, btnDelete, btnDownload;
    private final JTable table;
    private final FileTableModel model;
    private final InfoMedia infoMedia;

    public TableActions(JTable table, int column, FileTableModel model, InfoMedia infoMedia) {
        this.table = table;
        this.model = model;
        this.infoMedia = infoMedia;

        this.panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0)); //Panel que contiene los botones de la columna
        this.panel.setOpaque(false);

        this.btnPlay = createButton("/images/play2.png", "Reproduce");
        this.btnDelete = createButton("/images/delete.png", "Delete file");
        this.btnDownload = createButton("/images/download2.png", "Download");

        actionPlay(btnPlay);
        actionDelete(btnDelete);
        actionDownload(btnDownload);

        panel.add(btnPlay);
        panel.add(btnDelete);
        panel.add(btnDownload);

        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        tableColumn.setCellRenderer(this);
        tableColumn.setCellEditor(this);
    }
    
    //Establecer acción al botón play de la columna
    private void actionPlay(JButton btn){
        
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMedia.playSelectedFile();
                fireEditingStopped();
            }
        });
    }
    
    //Establecer acción al botón delete de la columna
    private void actionDelete(JButton btn) {

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                infoMedia.deleteSelectedFile();
            }
        });
    }
    
    //Establecer acción al botón download de la columna
    private void actionDownload(JButton btn) {

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getEditingRow();

                if (row == -1) {
                    row = table.rowAtPoint(panel.getLocation());
                }

                if (row != -1) {
                    fireEditingStopped();
                    table.setRowSelectionInterval(row, row);
                    infoMedia.downloadFile();
                }
            }
        });
    }

    //Controla que botones mostrar dependiendo de la carpeta seleccionada
    private void updateActionVisibility() {
        boolean isNetwork = infoMedia.isNetworkFileSelected();
        btnDownload.setVisible(isNetwork);
        btnPlay.setVisible(!isNetwork);
        btnDelete.setVisible(!isNetwork);
    }

    //Método auxiliar para crear botones con icono (limpia tu constructor)
    private JButton createButton(String iconPath, String toolTip) {
        JButton btn = new JButton();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btn.setText("?");
        }
        btn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setToolTipText(toolTip);
        return btn;
    }

    //Dibuja celda mostrando los botones dependiendo de la carpeta seleccionada
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        updateActionVisibility();
        panel.setToolTipText(infoMedia.isNetworkFileSelected() ? "Download file" : "Play / Delete");
        return panel;
    }

    //Botones interactivos
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        updateActionVisibility();
        return panel;
    }

    //Deveulve valor de la celda
    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
