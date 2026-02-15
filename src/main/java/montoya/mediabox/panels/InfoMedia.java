package montoya.mediabox.panels;

import montoya.mediabox.fileInformation.TableActions;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import montoya.mediabox.controller.*;
import montoya.mediabox.fileInformation.*;
import montoya.mediabox.configUI.SwingStyleUtils;
import montoya.mediapollingcomponent.MediaPollingComponent;
import net.miginfocom.swing.MigLayout;

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
        
        setupLayout();
        applyStylesComponent();

        configFilterOptions(cbbxTypeFilter);
        initDirectoryList();
        configDownloadList(folderList);
        
        SwingUtilities.invokeLater(new Runnable() { //Mostrar archivos al iniciar la app
            @Override
            public void run() {
                if (folderList.getModel().getSize() > 0) {
                    folderList.setSelectedIndex(0);
                }
            }
        });
    }
    
    private void setupLayout() {
        this.setLayout(new MigLayout("fill, insets 20", "[50:150:200]10[grow]", "[][grow]"));
        this.add(cbbxTypeFilter, "cell 1 0, split 2, right, width 200!");
        this.add(btnUpload, "width 140!, height 35!");
        this.add(scrFolderList, "cell 0 1, grow");
        this.add(scrTableMedia, "cell 1 1, grow");
    }

    //Aplica estilos a los componentes
    private void applyStylesComponent() {
        SwingStyleUtils.panelsBorders(this, SwingStyleUtils.DARK_GREY_COLOR, 30);
        SwingStyleUtils.selectionColorList(folderList, scrFolderList);
        SwingStyleUtils.selectionColorTable(tblMedia, scrTableMedia);
        SwingStyleUtils.selectionColorComboBox(cbbxTypeFilter);
        SwingStyleUtils.styleRoundedScroll(scrFolderList, 20, SwingStyleUtils.LIGHT_GREY_COLOR);
        SwingStyleUtils.styleRoundedScroll(scrTableMedia, 20, SwingStyleUtils.LIGHT_GREY_COLOR);
        cbbxTypeFilter.setEditable(true);
        SwingStyleUtils.styleButtons(btnUpload,"/images/upload.png", "UPLOAD", "Upload file to API", SwingStyleUtils.LIGHT_PURPLE, SwingStyleUtils.DARK_GREY_COLOR, true);
    }

    //Añade los filtros a JComboBox
    private void configFilterOptions(JComboBox cbbxFilter) {
        cbbxFilter.removeAllItems();
        cbbxFilter.addItem("ALL");
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
    private void configDownloadList(JList<FolderItem> folderList) {

        folderList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateTableData();
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
        if (info == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Borrar " + info.getName() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            fileManager.deleteFile(info);
            refreshFiles();
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

    //Gestiona donde se guarad el archivo ha descargar
    public void downloadFile() {
        FileInformation info = getSelectedFile();
        if (info == null) {
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                fileManager.downloadFile(info, fc.getSelectedFile());
                refreshFiles();
                JOptionPane.showMessageDialog(this, "Download completed!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
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
        setMaximumSize(null);
        setPreferredSize(null);
        setLayout(new java.awt.BorderLayout());

        folderList.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        folderList.setPreferredSize(new java.awt.Dimension(40, 60));
        folderList.setSelectionBackground(new java.awt.Color(255, 204, 153));
        scrFolderList.setViewportView(folderList);

        add(scrFolderList, java.awt.BorderLayout.CENTER);

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
        tblMedia.setPreferredSize(null);
        tblMedia.setSelectionBackground(new java.awt.Color(255, 204, 153));
        scrTableMedia.setViewportView(tblMedia);

        add(scrTableMedia, java.awt.BorderLayout.PAGE_START);

        cbbxTypeFilter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbbxTypeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filter" }));
        cbbxTypeFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbxTypeFilterActionPerformed(evt);
            }
        });
        add(cbbxTypeFilter, java.awt.BorderLayout.PAGE_END);

        btnUpload.setMaximumSize(new java.awt.Dimension(120, 25));
        btnUpload.setMinimumSize(new java.awt.Dimension(120, 25));
        btnUpload.setPreferredSize(new java.awt.Dimension(120, 25));
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });
        add(btnUpload, java.awt.BorderLayout.LINE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void cbbxTypeFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxTypeFilterActionPerformed
        updateTableData();
    }//GEN-LAST:event_cbbxTypeFilterActionPerformed

    //Botón que inicia la descarga
    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                fileManager.uploadFile(fc.getSelectedFile());
                refreshFiles();
                JOptionPane.showMessageDialog(this, "Uplodad completed");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
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
    
    //Actualizar tabla según carpeta y filtro aplicado
    private void updateTableData() {
        FolderItem folder = folderList.getSelectedValue();
        if (folder == null) {
            return;
        }

        String selectedFilter = (String) cbbxTypeFilter.getSelectedItem();

        List<FileInformation> resultFiles;

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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUpload;
    private javax.swing.JComboBox<String> cbbxTypeFilter;
    private javax.swing.JList<FolderItem> folderList;
    private javax.swing.JScrollPane scrFolderList;
    private javax.swing.JScrollPane scrTableMedia;
    private javax.swing.JTable tblMedia;
    // End of variables declaration//GEN-END:variables
}