package montoya.mediabox.panels;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import montoya.mediabox.controller.FileManager;
import montoya.mediabox.controller.TypeFilter;
import montoya.mediabox.fileInformation.DirectoryInformation;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.fileInformation.FolderItem;
import montoya.mediapollingcomponent.MediaPollingComponent;

/**
 * Panel que muestra las descargas realizadas y su información, permitiendo eliminar o reproducir archivos
 * @author Nerea
 */
public class InfoMedia extends javax.swing.JPanel {

    private FileTableModel tblModel;
    private FileManager fileManager;
    private final TypeFilter typeFilter;
    private final MediaPollingComponent mediaPollingComponent;

    private DirectoryInformation allData;
    private List<FileInformation> allFiles;

    private final FileProperties fileProperties;
    private Set<String> folderPaths = new HashSet<>();

    public InfoMedia(FileProperties fileProperties, TypeFilter typeFilter, List<FileInformation> allFiles, Set<String> folderPaths, MediaPollingComponent mediaPollingComponent) {
        initComponents();

        this.typeFilter = typeFilter;
        this.fileProperties = fileProperties;
        this.mediaPollingComponent = mediaPollingComponent;
        
        this.allData = fileProperties.loadDownloads();
        this.fileManager = new FileManager(allData, typeFilter, mediaPollingComponent);
        this.allFiles = allData.fileList;
        folderPaths.addAll(allData.folderPaths);

        tblModel = new FileTableModel(allFiles);
        tblMedia.setModel(tblModel);

        //Aplica el filtro según tipo de archivo
        filterOptions(cbbxTypeFilter);

        //Lista de objeto 'descarga'
        DefaultListModel<FolderItem> listModel = new DefaultListModel();
        listModel.addElement(new FolderItem("API FILES", true, false));
        listModel.addElement(new FolderItem("BOTH", false, true));
        
        for (String folder : folderPaths) {
            listModel.addElement(new FolderItem(folder, false, false));
        }
        folderList.setModel(listModel);

        //Muestra las descargas pertenecientes a un directorio
        configDownloadList(folderList, cbbxTypeFilter);
    }

    public JList<FolderItem> getListDirectories() {
        return folderList;
    }

    public JTable getTable() {
        return tblMedia;
    }

    public FileTableModel getTableModel() {
        return tblModel;
    }

    public JComboBox<String> getTypeFilter() {
        return cbbxTypeFilter;
    }

    //Añade los filtros a JComboBox
    private void filterOptions(JComboBox cbbxFilter) {
        cbbxFilter.removeAllItems();
        cbbxFilter.addItem("All");
        cbbxFilter.addItem("MP4");
        cbbxFilter.addItem("MKV");
        cbbxFilter.addItem("WEBM");
        cbbxFilter.addItem("MP3");
        cbbxFilter.addItem("WAV");
        cbbxFilter.addItem("M4A");
    }

    //Configuración de JList, al seleccionar un directorio muestra las descargas.
    private void configDownloadList(JList<FolderItem> folderList, JComboBox<String> cbbxFilter) {

        folderList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {
                    FolderItem folder = folderList.getSelectedValue();
                    if (folder != null) {

                        String selectedFilter = (String) cbbxFilter.getSelectedItem();

                        List<FileInformation> resultFiles = new ArrayList<>();

                        if (folder.isIsNetwork()) {

                            resultFiles = fileManager.getNetworkFiles(selectedFilter);

                        } else if (folder.isIsBoth()) {

                            resultFiles = fileManager.getBothFiles(selectedFilter);

                        } else {

                            resultFiles = fileManager.getLocalFiles(folder.getFullPath(), selectedFilter);

                        }
                        tblModel.setFileList(resultFiles);
                        tblModel.fireTableDataChanged();
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrFolderList = new javax.swing.JScrollPane();
        folderList = new javax.swing.JList<>();
        scrTableMedia = new javax.swing.JScrollPane();
        tblMedia = new javax.swing.JTable();
        btnPlay = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        cbbxTypeFilter = new javax.swing.JComboBox<>();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Downloads", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        setMinimumSize(new java.awt.Dimension(630, 400));
        setLayout(null);

        folderList.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        folderList.setPreferredSize(new java.awt.Dimension(40, 60));
        folderList.setSelectionBackground(new java.awt.Color(255, 204, 153));
        scrFolderList.setViewportView(folderList);

        add(scrFolderList);
        scrFolderList.setBounds(16, 80, 100, 240);

        tblMedia.setAutoCreateRowSorter(true);
        tblMedia.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblMedia.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMedia.setPreferredSize(null);
        tblMedia.setSelectionBackground(new java.awt.Color(255, 204, 153));
        scrTableMedia.setViewportView(tblMedia);

        add(scrTableMedia);
        scrTableMedia.setBounds(120, 80, 490, 240);

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
        btnDelete.setBounds(520, 350, 90, 24);

        cbbxTypeFilter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbbxTypeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filter" }));
        cbbxTypeFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbxTypeFilterActionPerformed(evt);
            }
        });
        add(cbbxTypeFilter);
        cbbxTypeFilter.setBounds(240, 40, 250, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed

        int row = tblMedia.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a file to play.");
            return;
        }

        int modelRow = tblMedia.convertRowIndexToModel(row);
        FileInformation info = tblModel.getFileAt(modelRow);

        File file = new File(info.folderPath, info.name);

        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Could not open file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "File not found: " + file.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int row = tblMedia.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a file to delete.");
            return;
        }

        int modelRow = tblMedia.convertRowIndexToModel(row);
        FileInformation info = tblModel.getFileAt(modelRow);

        Object selected = folderList.getSelectedValue();
        if (selected instanceof FolderItem folder && folder.isIsNetwork()) {
            JOptionPane.showMessageDialog(this, "Cannot delete network files.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to delete this file? - " + info.name, "Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        //Borra archivo físico y del archivo JSON
        fileProperties.deleteDownload(info, allFiles, folderPaths);
        
        //Carga los archivo actuales del archivo JSON
        DirectoryInformation updateData = fileProperties.loadDownloads();
        allFiles = updateData.fileList;
        folderPaths.clear();
        folderPaths.addAll(allData.folderPaths);

        //Actualiza la tabla dependiento del directorio seleccionado
        if (selected instanceof FolderItem folder) {
            String filtro = (String) cbbxTypeFilter.getSelectedItem();

            List<FileInformation> filteredByDirectory = typeFilter.filterByDirectory(allFiles, folder.getFullPath());
            List<FileInformation> filteredByType = typeFilter.filterByType(filteredByDirectory, filtro);

            tblModel.setFileList(filteredByType);
            tblModel.fireTableDataChanged();
        } else {
            tblModel.setFileList(allFiles);
            tblModel.fireTableDataChanged();
        }

        //Actualiza los directorios en el listado
        DefaultListModel<FolderItem> newModel = new DefaultListModel<>();
        for (String folderPath : folderPaths) {
            newModel.addElement(new FolderItem(folderPath, false, false));
        }
        folderList.setModel(newModel);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void cbbxTypeFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxTypeFilterActionPerformed

        FolderItem folder = folderList.getSelectedValue();

        //Obtiene la ruta del archivo y el valor seleccionado de cbbx
        if (folder != null) {
            
            String selectedFilter = (String) cbbxTypeFilter.getSelectedItem();
            List<FileInformation> filesToShow;

            if (folder.isIsNetwork()) {
                //Obtiene los archivos de la API
                filesToShow = fileManager.getNetworkFiles(selectedFilter);
                
            } else if(folder.isIsBoth()){
                filesToShow = fileManager.getBothFiles(selectedFilter);
           
            }else{

                filesToShow = fileManager.getLocalFiles(folder.getFullPath(), selectedFilter);
            }

            //Actualizar tabla con los elementos filtrados
            tblModel.setFileList(filesToShow);
            tblModel.fireTableDataChanged();
        }
    }//GEN-LAST:event_cbbxTypeFilterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPlay;
    private javax.swing.JComboBox<String> cbbxTypeFilter;
    private javax.swing.JList<FolderItem> folderList;
    private javax.swing.JScrollPane scrFolderList;
    private javax.swing.JScrollPane scrTableMedia;
    private javax.swing.JTable tblMedia;
    // End of variables declaration//GEN-END:variables
}
