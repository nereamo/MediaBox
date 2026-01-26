
package montoya.mediabox.controller;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.panels.InfoMedia;

/**
 *
 * @author Nerea
 */
public class TableActions extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener{
    
    private final JPanel panel;
    private final JButton btnPlay, btnDelete, btnDownload;
    private final JTable table;
    private final FileTableModel model;
    private final InfoMedia infoMedia; // Necesario para llamar a la lógica de borrar

    public TableActions(JTable table, int column, FileTableModel model, InfoMedia infoMedia) {
        this.table = table;
        this.model = model;
        this.infoMedia = infoMedia;
        
        // Creamos un panel contenedor transparente
        this.panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        this.panel.setOpaque(false);

        // Inicializamos los botones
        this.btnPlay = createButton("/images/play2.png", "Reproduce");
        this.btnDelete = createButton("/images/delete.png", "Delete file");
        this.btnDownload = createButton("/images/download2.png", "Download");

        // Añadimos acciones directamente
        btnPlay.addActionListener(e -> {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            fireEditingStopped();
            reproducir(row);
        });

        btnDelete.addActionListener(e -> {
            // Detenemos la edición antes de lanzar el diálogo de borrar
            fireEditingStopped();
            // Llamamos al método que ya tienes en InfoMedia
            infoMedia.deleteSelectedFile();
        });
        
        btnDownload.addActionListener(e -> {
    // 1. Intentamos obtener la fila actual de edición
    int row = table.getEditingRow();
    
    // 2. Si es -1 (porque perdió el foco), usamos la posición del panel
    if (row == -1) {
        row = table.rowAtPoint(panel.getLocation());
    }

    // 3. Verificamos que sea una fila válida antes de actuar
    if (row != -1) {
        fireEditingStopped(); 
        
        // Seleccionamos la fila para que InfoMedia sepa qué archivo descargar
        table.setRowSelectionInterval(row, row);
        
        // Llamamos a la descarga
        infoMedia.downloadFile();
    }

});

        panel.add(btnPlay);
        panel.add(btnDelete);
        panel.add(btnDownload);

        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        tableColumn.setCellRenderer(this);
        tableColumn.setCellEditor(this);
    }
    
    private void updateActionVisibility() {
        boolean isNetwork = infoMedia.isNetworkFileSelected();
        // Si es red: mostramos descargar, ocultamos play/delete
        btnDownload.setVisible(isNetwork);
        btnPlay.setVisible(!isNetwork);
        btnDelete.setVisible(!isNetwork);
    }

    // Método auxiliar para crear botones con icono (limpia tu constructor)
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

    private void reproducir(int row) {
        FileInformation info = model.getFileAt(row);
        File file = new File(info.getFolderPath(), info.getName());
        if (file.exists()) {
            try { Desktop.getDesktop().open(file); } 
            catch (Exception ex) { JOptionPane.showMessageDialog(table, "Error opening file."); }
        } else {
            JOptionPane.showMessageDialog(table, "File not found.");
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        updateActionVisibility();
        panel.setToolTipText(infoMedia.isNetworkFileSelected() ? "Download file" : "Play / Delete");
    return panel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        updateActionVisibility();
    return panel;
    }

    @Override
    public Object getCellEditorValue() { return ""; }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
