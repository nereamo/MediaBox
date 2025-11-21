package montoya.mediabox;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import montoya.mediabox.controller.MainViewController;
import montoya.mediabox.fileInformation.DirectoryInformation;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.fileInformation.FolderItem;

/**
 *
 * @author Nerea
 */
public class DownloadsPanel extends javax.swing.JPanel {

    private FileTableModel tblModel;
    private List<FileInformation> fileList = new ArrayList<>();
    private Set<String> downloadDirectories = new HashSet<>();
    private final MainViewController mvc;
    private Preferences preferences;
    private final FileProperties fp;

    public DownloadsPanel(FileProperties fp, MainViewController mvc, List<FileInformation> fileList, Set<String> downloadDirectories) {
        initComponents();
        this.fp = fp;
        this.mvc = mvc;
        this.fileList = fileList;
        this.downloadDirectories = downloadDirectories;
        this.setSize(630, 400);

        //Carga datos guardados en archivo .json
        DirectoryInformation data = fp.loadDownloads();
        fileList = data.downloads;
        downloadDirectories.addAll(data.downloadFolders);

        //AbstractTableMode
        tblModel = new FileTableModel(fileList);
        tblInfo.setModel(tblModel);

        //Lista de objeto 'descarga'
        DefaultListModel listModel = new DefaultListModel();
        for (String folder : downloadDirectories) {
            listModel.addElement(new FolderItem(folder));
        }
        listDirectories.setModel(listModel);

        //Muestra las descargas pertenecientes a un directorio
        mvc.configDownloadList(listDirectories, cbbxTypeFilter, tblInfo);
        
        //Aplica el filtro por tipo de archivo
        mvc.applyFilters(cbbxTypeFilter);
 
    }

    public JList<FolderItem> getListDirectories() {
        return listDirectories;
    }

    public JTable getTable() {
        return tblInfo;
    }

    public FileTableModel getTableModel() {
        return tblModel;
    }

    public JComboBox<String> getTypeFilter() {
        return cbbxTypeFilter;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        listDirectories = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblInfo = new javax.swing.JTable();
        cbbxTypeFilter = new javax.swing.JComboBox<>();
        btnPlay = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Downloads", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        setLayout(null);

        listDirectories.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listDirectories.setPreferredSize(new java.awt.Dimension(40, 60));
        listDirectories.setSelectionBackground(new java.awt.Color(255, 204, 153));
        jScrollPane2.setViewportView(listDirectories);

        add(jScrollPane2);
        jScrollPane2.setBounds(16, 80, 100, 240);

        tblInfo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Size", "Type", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblInfo.setPreferredSize(new java.awt.Dimension(60, 80));
        tblInfo.setSelectionBackground(new java.awt.Color(255, 204, 153));
        jScrollPane3.setViewportView(tblInfo);

        add(jScrollPane3);
        jScrollPane3.setBounds(120, 80, 490, 240);

        cbbxTypeFilter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbbxTypeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filter" }));
        cbbxTypeFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbxTypeFilterActionPerformed(evt);
            }
        });
        add(cbbxTypeFilter);
        cbbxTypeFilter.setBounds(240, 40, 250, 23);

        btnPlay.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnPlay.setText("Play");
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        add(btnPlay);
        btnPlay.setBounds(420, 350, 90, 24);

        btnDelete.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        add(btnDelete);
        btnDelete.setBounds(522, 350, 90, 24);
    }// </editor-fold>//GEN-END:initComponents

    private void cbbxTypeFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxTypeFilterActionPerformed
        //Objeto seleccionado de JList
        Object selected = listDirectories.getSelectedValue();

        //Obtiene la ruta absoluta del archivo y el valor seleccionado de cbbx
        if (selected instanceof FolderItem) {
            String folderPath = ((FolderItem) selected).getFullPath();
            String filtro = (String) cbbxTypeFilter.getSelectedItem();

            //Obtiene los archivos gurdados en .json
            FileProperties fp = new FileProperties();
            DirectoryInformation allData = fp.loadDownloads();
            List<FileInformation> allFiles = allData.downloads;

            //Filtrar por directorio
            List<FileInformation> filteredByDirectory = mvc.filterByDirectory(allFiles, folderPath);

            //Filtrar por tipo
            List<FileInformation> filteredByType = mvc.filterByType(filteredByDirectory, filtro);

            //Actualizar tabla con los elementos filtrados
            FileTableModel model = (FileTableModel) tblInfo.getModel();
            model.setFileList(filteredByType);
            model.fireTableDataChanged();
        }
    }//GEN-LAST:event_cbbxTypeFilterActionPerformed

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed

        int row = tblInfo.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a file to play.");
            return;
        }

        int modelRow = tblInfo.convertRowIndexToModel(row);
        FileInformation info = tblModel.getFileAt(modelRow);

        File file = new File(info.folderPath, info.name);

        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Could not open file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "File not found: " + file.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int row = tblInfo.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a file to delete.");
            return;
        }

        FileInformation info = tblModel.getFileAt(row);

        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to delete this file? - " + info.name, "Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        //Carga lista de .json
        FileProperties fp = new FileProperties();
        DirectoryInformation data = fp.loadDownloads();
        List<FileInformation> allFiles = data.downloads;
        Set<String> allDirs = data.downloadFolders;

        //Borra archivo físico y de .json
        fp.deleteDownload(info, allFiles, allDirs);

        //Actualiza la tabla dependiento del directorio seleccionado
        Object selected = listDirectories.getSelectedValue();
        if (selected instanceof FolderItem folder) {
            String filtro = (String) cbbxTypeFilter.getSelectedItem();

            List<FileInformation> filteredByDirectory = mvc.filterByDirectory(allFiles, folder.getFullPath());
            List<FileInformation> filteredByType = mvc.filterByType(filteredByDirectory, filtro);

            tblModel.setFileList(filteredByType);
            tblModel.fireTableDataChanged();
        } else {
            tblModel.setFileList(allFiles);
            tblModel.fireTableDataChanged();
        }

        //Actualiza los directorios en el listado
        DefaultListModel<FolderItem> newModel = new DefaultListModel<>();
        for (String folderPath : allDirs) {
            newModel.addElement(new FolderItem(folderPath));
        }
        listDirectories.setModel(newModel);
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPlay;
    private javax.swing.JComboBox<String> cbbxTypeFilter;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<FolderItem> listDirectories;
    private javax.swing.JTable tblInfo;
    // End of variables declaration//GEN-END:variables
}
