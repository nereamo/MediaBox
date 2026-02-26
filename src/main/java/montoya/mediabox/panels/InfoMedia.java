package montoya.mediabox.panels;

import montoya.mediabox.fileInformation.TableActions;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import montoya.mediabox.controller.*;
import montoya.mediabox.fileInformation.*;
import montoya.mediabox.configUI.UIStyles;
import montoya.mediapollingcomponent.MediaPollingComponent;
import net.miginfocom.swing.MigLayout;

/**
 * Panel que muestra las descargas realizadas y su información
 * <p> Permitie:
 * <ul>
 * <li> Visualizar archivos descargados, de red y mixtos </li>
 * <li> Filtrar archivos por directorio y tipo (formato) </li>
 * <li> Reproducir archivos locales </li>
 * <li> Eliminar archivos locales </li>
 * <li> Descargar archivos de la API </li>
 * <li> Subir archivos locales a la API </li>
 * </ul>
 *
 * @author Nerea
 */
public class InfoMedia extends javax.swing.JPanel {

    /** {@link FileTableModel} Modelo de la tabla que mustra las descargas. */
    private FileTableModel tblModel;
    
    /** {@link FileManager} Gestiona operaciones sobre los archivos mostrados en la aplicación. */
    private FileManager fileManager;
    
    /** {@link TypeFilter} Permite aplicar el filtro por tipo de archivo y por directorio. */
    private final TypeFilter typeFilter;
    
    /** {@link MediaPollingComponent} Listener que notifica nuevos medios en la API. */
    private final MediaPollingComponent mediaPollingComponent;
    
    /** {@link FileProperties} Gestiona las propiedades de los archivos descargados. */
    private final FileProperties fileProperties;
    
    /** {@link DirectoryInformation} Proporciona información del directorio y sus descargas. */
    private DirectoryInformation allData;
    
    /** Lista {@link FileInformation} que contiene todas las descargas.*/
    private List<FileInformation> allFiles;
    
    /** Colección que contiene todas las rutas de los directorios.*/
    private Set<String> folderPaths = new HashSet<>();

    /**
     * Constructor que inicializa el panel InfoMedia
     * 
     * @param fileProperties Gestiona las propiedades de los archivos descargados
     * @param typeFilter Aplicar el filtro por tipo de archivo y por directorio
     * @param allFiles Todos los archivos alamcenados en JSON
     * @param folderPaths Todas la rutas donde se realizan las descargas
     * @param mediaPollingComponent Listener que notifica nuevos medios en la API
     */
    public InfoMedia(FileProperties fileProperties, TypeFilter typeFilter, List<FileInformation> allFiles, Set<String> folderPaths, MediaPollingComponent mediaPollingComponent) {
        initComponents();
        
        this.typeFilter = typeFilter;
        this.fileProperties = fileProperties;
        this.mediaPollingComponent = mediaPollingComponent;
        
        this.allData = fileProperties.loadDownloads(); //Carga la información guradada en JSON
        this.fileManager = new FileManager(allData, typeFilter, mediaPollingComponent, fileProperties); //Inicializa el gestor de archivos

        this.allFiles = allData.getFileList(); //Archivos locales
        folderPaths.addAll(allData.getFolderPaths()); //Añade las rutas de los directorios

        tblModel = new FileTableModel(allFiles); //Modelo de tabla
        tblMedia.setModel(tblModel);
        tblMedia.setRowHeight(25);
        ToolTipManager.sharedInstance().registerComponent(tblMedia); //Tooltips

        new TableActions(tblMedia, 4, this); //Botones de la columna "Actions"
        
        setupLayout();
        setupStyle();

        configFilterOptions(cbbxTypeFilter);
        initDirectoryList();
        configDownloadList(folderList);
        
        //Selecciona el primer directorio por defecto
        SwingUtilities.invokeLater(new Runnable() { //Mostrar archivos al iniciar la app
            @Override
            public void run() {
                if (folderList.getModel().getSize() > 0) {
                    folderList.setSelectedIndex(0);
                }
            }
        });
    }
    
    /** Configura posición de los componentes */
    private void setupLayout() {
        this.setLayout(new MigLayout("fill, insets 20", "[50:150:200]10[grow]", "[][grow]")); //Panel principal
        
        this.add(cbbxTypeFilter, "cell 1 0, split 2, right, width 200!"); //JComboBox para filtrar por tipo
        
        this.add(btnUpload, "width 140!, height 35!"); //Botón Upload
        
        this.add(scrFolderList, "cell 0 1, grow"); //JScroll de la lista
        
        this.add(scrTableMedia, "cell 1 1, grow"); //JScroll de la tabla
    }

    /** Configura el estilo de los componentes */
    private void setupStyle() {
        UIStyles.panelsBorders(this, UIStyles.DARK_GREY_COLOR, 30); //Panel principal
        
        UIStyles.styleScrollComponent(folderList, scrFolderList); //JScroll de la lista
        UIStyles.styleScrollComponent(tblMedia, scrTableMedia); //JScroll de la tabla
        
        UIStyles.styleComboBox(cbbxTypeFilter); //JComboBox para filtrar por tipo
        cbbxTypeFilter.setEditable(true); //Permite escribir en comboBox
        
        UIStyles.styleButtons(btnUpload, "UPLOAD", "/images/upload.png", UIStyles.LIGHT_PURPLE, UIStyles.DARK_GREY_COLOR, true, "Upload file to API", null); //Botón Upload
    }

    /** Configura los items (filtros) mostrados en el JComboBox */
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

    /** Inicializa la lista de directorios alamcenados en JSON. */
    private void initDirectoryList() {

        DefaultListModel<FolderItem> listModel = new DefaultListModel();
        listModel.addElement(new FolderItem("API FILES", true, false)); //directorio Api files (muestra archivos de api)
        listModel.addElement(new FolderItem("BOTH", false, true)); //directorio Both (muestra archivos que esten en ambos lados)

        folderPaths.clear(); //Reconstruye rutas locales
        for (FileInformation fi : allFiles) {
            folderPaths.add(fi.getFolderPath());
        }

        for (String folder : folderPaths) { //Añade los directorios
            listModel.addElement(new FolderItem(folder, false, false));
        }
        folderList.setModel(listModel);
    }

    /** Configura el listener para mostrar las descargas pertenecientes a un directorio. */
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
    
    /** Actualizar tabla según directorio y filtro seleccionado. */
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
    
    /** Actualiza la tabla después de descargar, subir o eliminar archivos. */
    public void reloadMediaData() {

        //Guarda directorio seleccionado
        FolderItem selectedFolder = folderList.getSelectedValue();
        String selectedFolderPath = (selectedFolder != null) ? selectedFolder.getFullPath() : null;

        //Actualiza lista de archivos
        fileManager.reloadDirectoryInfo();
        allFiles = fileProperties.loadDownloads().getFileList();
        tblModel.setFileList(allFiles);
        tblModel.fireTableDataChanged();

        //Re-inicializar directorios
        initDirectoryList();

        //Restaurar directorio seleccionado
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

        tblMedia.clearSelection();
    }
    
    /** Reproduce el archivo al pulsar el boton "Play" de la columna "Actions. */
    public void playSelectedFile() {
        FileInformation info = getSelectedFile();
        if (info == null) {
            JOptionPane.showMessageDialog(this, "Please select a file.");
            return;
        }

        try {
            fileManager.openLocalFile(info);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening file.");
        }
    }
    
    /** Borra el archivo al pulsar el boton "Delete" de la columna "Actions. */
    public void deleteSelectedFile() {
        FileInformation info = getSelectedFile();
        if (info == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Borrar " + info.getName() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            fileManager.deleteLocalFile(info);
            reloadMediaData(); //Recarga los datos (directorios y archivos)
        }
    }

    /** Descarga el archivo seleccionado desde la API. */
    public void downloadFile() {
        FileInformation info = getSelectedFile();
        if (info == null) {
            return;
        }

        JFileChooser fc = new JFileChooser(); //Elegir directorio
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                fileManager.downloadFileFromApi(info, fc.getSelectedFile());
                reloadMediaData(); //Actualiza archivos
                JOptionPane.showMessageDialog(this, "Download completed!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    /** Obtiene archivo seleccionado en la tabla. */
    public FileInformation getSelectedFile() {
        int row = tblMedia.getSelectedRow();
        if (row < 0 || row >= tblMedia.getRowCount()) {
            return null; //Protección índices inválidos
        }
        try {
            int modelRow = tblMedia.convertRowIndexToModel(row);
            if (modelRow < 0 || modelRow >= tblModel.getRowCount()) {
                return null;
            }
            return tblModel.getFileAt(modelRow);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /** Indica si el directorio seleccionado es de la API. */
    public boolean isNetworkFileSelected() {
        FolderItem folder = folderList.getSelectedValue();
        return folder != null && folder.isIsNetwork();
    }

    /** @return Lista de directorios */
    public JList<FolderItem> getListDirectories() {
        return folderList;
    }

    /** @return Tabla de archivos */
    public JTable getTable() {
        return tblMedia;
    }

    /** @return Modelo de tabla */
    public FileTableModel getTableModel() {
        return tblModel;
    }

    /** @return Filtro aplicado en JComboBox */
    public JComboBox<String> getTypeFilter() {
        return cbbxTypeFilter;
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

    /**
     * Actualiza la tabla con el filtro aplicado en el JComboBox
     * 
     * @param evt Evento de acción del JComboBox
     */
    private void cbbxTypeFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxTypeFilterActionPerformed
        updateTableData();
    }//GEN-LAST:event_cbbxTypeFilterActionPerformed

    /**
     * Gestiona la subida de un archivo local a la API.
     * 
     * @param evt Evento de acción del JButon Upload
     */
    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                fileManager.uploadFileToApi(fc.getSelectedFile()); //Subir archivo seleccionado
                reloadMediaData(); //Recarga los datos (directorios y archivos)
                JOptionPane.showMessageDialog(this, "Uplodad completed");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnUploadActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUpload;
    private javax.swing.JComboBox<String> cbbxTypeFilter;
    private javax.swing.JList<FolderItem> folderList;
    private javax.swing.JScrollPane scrFolderList;
    private javax.swing.JScrollPane scrTableMedia;
    private javax.swing.JTable tblMedia;
    // End of variables declaration//GEN-END:variables
}