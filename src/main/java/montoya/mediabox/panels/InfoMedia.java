package montoya.mediabox.panels;

import montoya.mediabox.fileInformation.TableActions;
import java.io.File;
import java.nio.file.Files;
import java.time.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import montoya.mediabox.controller.*;
import montoya.mediabox.fileInformation.*;
import montoya.mediabox.styleConfig.StyleConfig;
import montoya.mediapollingcomponent.MediaPollingComponent;

/**
 * Panel que muestra las descargas realizadas y su información, permitiendo eliminar o reproducir archivos
 *
 * @author Nerea
 */
public class InfoMedia extends javax.swing.JPanel {

    private FileTableModel tblModel;
    private FileManager fileManager;
    private final TypeFilter typeFilter;
    private final MediaPollingComponent mediaPollingComponent;
    private final FileProperties fileProperties;
    private DirectoryInformation allData;
    private List<FileInformation> allFiles;
    private Set<String> folderPaths = new HashSet<>();

    public InfoMedia(FileProperties fileProperties, TypeFilter typeFilter, List<FileInformation> allFiles, Set<String> folderPaths, MediaPollingComponent mediaPollingComponent) {
        initComponents();

        this.typeFilter = typeFilter;
        this.fileProperties = fileProperties;
        this.mediaPollingComponent = mediaPollingComponent;
        this.allData = fileProperties.loadDownloads();
        this.fileManager = new FileManager(allData, typeFilter, mediaPollingComponent, fileProperties);
        
        this.allFiles = allData.getFileList(); //Archivos locales
        folderPaths.addAll(allData.getFolderPaths());

        tblModel = new FileTableModel(allFiles); //Modelo de tabla
        tblMedia.setModel(tblModel);
        tblMedia.setRowHeight(25);
        ToolTipManager.sharedInstance().registerComponent(tblMedia);
        
        new TableActions(tblMedia, 4, tblModel, this); //Botones de acción
 
        filterOptions(cbbxTypeFilter); //Aplica el filtro según tipo de archivo
        styleComponents(); //Estilo de los componentes
        initDirectoryList(); //Inicia la lista de directorios
        configDownloadList(folderList, cbbxTypeFilter); //Muestra las descargas pertenecientes a un directorio

        SwingUtilities.invokeLater(new Runnable() { //Mostrar archivos al iniciar la app
            @Override
            public void run() {
                if (folderList.getModel().getSize() > 0) {
                    folderList.setSelectedIndex(0);
                }
            }
        });
    }

    //Aplica estilos a los componentes
    private void styleComponents() {
        StyleConfig.handCursor(tblMedia, folderList, cbbxTypeFilter);
        StyleConfig.createTitleBorder(this, "DOWNLOAD INFORMATION");
        StyleConfig.selectionColorList(folderList);
        StyleConfig.selectionColorTable(tblMedia);
        StyleConfig.selectionColorComboBox(cbbxTypeFilter);
        cbbxTypeFilter.setEditable(true);
        StyleConfig.styleSimpleButton(btnUpload, "UPLOAD", "Upload file to API", 70, 23, StyleConfig.LIGHT_BLUE_COLOR, StyleConfig.DARK_BLUE_COLOR);
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
        cbbxFilter.setSelectedIndex(0); //Seleccion por defecto "ALL"
    }

    //Inicia los directorios
    private void initDirectoryList() {

        DefaultListModel<FolderItem> listModel = new DefaultListModel();
        listModel.addElement(new FolderItem("API FILES", true, false)); //directorio Api files (muestra archivos de api)
        listModel.addElement(new FolderItem("BOTH", false, true)); //directorio Both (muestra archivos que esten en ambos lados)

        folderPaths.clear();
        for (FileInformation fi : allFiles) {
            folderPaths.add(fi.getFolderPath());
        }

        for (String folder : folderPaths) {
            listModel.addElement(new FolderItem(folder, false, false));
        }
        folderList.setModel(listModel);
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
                        fileManager.refreshFiles(); //Refresca los archivos de la tabla 
                        
                        List<FileInformation> resultFiles;
                        if (folder.isIsNetwork()) {
                            resultFiles = fileManager.getNetworkFiles(selectedFilter); //archivos de api
                        } else if (folder.isIsBoth()) {
                            resultFiles = fileManager.getBothFiles(selectedFilter); //archivos api y local
                        } else {
                            resultFiles = fileManager.getLocalFiles(folder.getFullPath(), selectedFilter); //archivos locales
                        }
                        
                        tblModel.setFileList(resultFiles);
                        tblModel.fireTableDataChanged();
                    }
                }
            }
        });
    }
    
    //Retorna el archivo seleccionado en la tabla
    public FileInformation getSelectedFile() {
        int row = tblMedia.getSelectedRow();
        if (row < 0) {
            return null;
        }
        int modelRow = tblMedia.convertRowIndexToModel(row);
        return tblModel.getFileAt(modelRow);
    }
    
    //Indica si el directorio seleccionado es de la API
    public boolean isNetworkFileSelected() {
        FolderItem folder = folderList.getSelectedValue();
        return folder != null && folder.isIsNetwork();
    }
    
    //Reproduce el archivo cuando se pulsa el boton play
    public void playSelectedFile() {
        FileInformation info = getSelectedFile();
        if (info == null) {
            JOptionPane.showMessageDialog(this, "Please select a file.");
            return;
        }

        try {
            fileManager.playFile(info);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening file.");
        }
    }
    
    //Borra archivo seleccionado
    public void deleteSelectedFile() {

        FileInformation info = getSelectedFile();

       if(info == null){
           return;
       }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Do you want to delete: " + info.getName() + "?", "Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            fileManager.deleteFile(info);
            refreshFiles();
            JOptionPane.showMessageDialog(this, "File deleted.");
        }
    }
    
    //Refresca la tabla de archivos después de descargar/subir
    public void refreshFiles() {

        FolderItem selectedFolder = folderList.getSelectedValue();
        String selectedFolderPath = (selectedFolder != null) ? selectedFolder.getFullPath() : null;

        FileInformation selectedFile = getSelectedFile();
        String selectedFileName = (selectedFile != null) ? selectedFile.getName() : null;

        fileManager.refreshFiles();
        allFiles = fileProperties.loadDownloads().getFileList();
        tblModel.setFileList(allFiles);
        tblModel.fireTableDataChanged();

        initDirectoryList();

        if (selectedFolderPath != null) {
            ListModel<FolderItem> model = folderList.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                FolderItem item = model.getElementAt(i);
                if (item.getFullPath().equals(selectedFolderPath)) {
                    folderList.setSelectedIndex(i);
                    break;
                }
            }
        }

        if (selectedFileName != null) {
            for (int i = 0; i < tblModel.getRowCount(); i++) {
                if (tblModel.getFileAt(i).getName().equals(selectedFileName)) {
                    int viewRow = tblMedia.convertRowIndexToView(i);
                    tblMedia.setRowSelectionInterval(viewRow, viewRow);
                    break;
                }
            }
        }
    }
    
    public void downloadFile() {
        
        FileInformation info = this.getSelectedFile();
        if (info == null) {
            JOptionPane.showMessageDialog(this, "Please select a file in the table.");
            return;
        }

        // Elegir directorio local
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File folder = fileChooser.getSelectedFile();
        try {
            fileManager.downloadFile(info, folder);
            refreshFiles();
            JOptionPane.showMessageDialog(this, "Download completed.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Download failed:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrFolderList = new javax.swing.JScrollPane();
        folderList = new javax.swing.JList<>();
        scrTableMedia = new javax.swing.JScrollPane();
        tblMedia = new javax.swing.JTable();
        cbbxTypeFilter = new javax.swing.JComboBox<>();
        btnUpload = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Downloads", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        setFocusCycleRoot(true);
        setMinimumSize(new java.awt.Dimension(700, 540));
        setPreferredSize(new java.awt.Dimension(700, 540));
        setLayout(null);

        folderList.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        folderList.setPreferredSize(new java.awt.Dimension(40, 60));
        folderList.setSelectionBackground(new java.awt.Color(255, 204, 153));
        scrFolderList.setViewportView(folderList);

        add(scrFolderList);
        scrFolderList.setBounds(16, 80, 130, 350);

        tblMedia.setAutoCreateRowSorter(true);
        tblMedia.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblMedia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Name", "Size", "Type", "Date", "Actions"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblMedia.setSelectionBackground(new java.awt.Color(255, 204, 153));
        scrTableMedia.setViewportView(tblMedia);

        add(scrTableMedia);
        scrTableMedia.setBounds(150, 80, 670, 350);

        cbbxTypeFilter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbbxTypeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filter" }));
        cbbxTypeFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbxTypeFilterActionPerformed(evt);
            }
        });
        add(cbbxTypeFilter);
        cbbxTypeFilter.setBounds(440, 50, 250, 23);

        btnUpload.setMaximumSize(new java.awt.Dimension(120, 25));
        btnUpload.setMinimumSize(new java.awt.Dimension(120, 25));
        btnUpload.setPreferredSize(new java.awt.Dimension(120, 25));
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });
        add(btnUpload);
        btnUpload.setBounds(700, 50, 120, 25);
    }// </editor-fold>//GEN-END:initComponents

    private void cbbxTypeFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxTypeFilterActionPerformed

        FolderItem folder = folderList.getSelectedValue();

        if (folder != null) {

            String selectedFilter = (String) cbbxTypeFilter.getSelectedItem();
            List<FileInformation> filesToShow;

            if (folder.isIsNetwork()) {
                filesToShow = fileManager.getNetworkFiles(selectedFilter); //Obtiene los archivos de la API
            } else if (folder.isIsBoth()) {
                filesToShow = fileManager.getBothFiles(selectedFilter); //Obtiene los archivos de la API y local
            } else {
                filesToShow = fileManager.getLocalFiles(folder.getFullPath(), selectedFilter); //Obtiene los archivos locales
            }

            //Actualizar tabla con los elementos filtrados
            tblModel.setFileList(filesToShow);
            tblModel.fireTableDataChanged();
        }
    }//GEN-LAST:event_cbbxTypeFilterActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = fileChooser.getSelectedFile();
            if (file == null || !file.exists()) {
                JOptionPane.showMessageDialog(this, "Cannot find the selected file.");
                return;
            }

            String mimeType = Files.probeContentType(file.toPath());
            mediaPollingComponent.uploadFileMultipart(file, mimeType, mediaPollingComponent.getToken());
            mediaPollingComponent.setLastChecked(OffsetDateTime.now().minusMinutes(1).toString());

            this.refreshFiles();

            JOptionPane.showMessageDialog(this, "Upload completed.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Upload failed:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUploadActionPerformed

    //Getters
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUpload;
    private javax.swing.JComboBox<String> cbbxTypeFilter;
    private javax.swing.JList<FolderItem> folderList;
    private javax.swing.JScrollPane scrFolderList;
    private javax.swing.JScrollPane scrTableMedia;
    private javax.swing.JTable tblMedia;
    // End of variables declaration//GEN-END:variables
}
