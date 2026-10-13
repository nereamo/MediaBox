package montoya.mediabox.panels;

import montoya.mediabox.utils.Logger;
import montoya.mediabox.fileInformation.TableActions;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import montoya.mediabox.controller.*;
import montoya.mediabox.fileInformation.*;
import montoya.mediabox.configUI.UIStyles;
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
//    private final MediaPollingComponent mediaPollingComponent;
    
    /** {@link FileProperties} Gestiona las propiedades de los archivos descargados. */
    private final FileProperties fileProperties;
    
    /** {@link DirectoryInformation} Proporciona información del directorio y sus descargas. */
    private DirectoryInformation allData;
    
    /** Lista {@link FileInformation} que contiene todas las descargas.*/
    private Set<FileInformation> allFiles;
    
    /** Colección que contiene todas las rutas de los directorios.*/
    private Set<String> folderPaths = new HashSet<>();

    /**
     * Constructor que inicializa el panel InfoMedia
     * 
     * @param fileProperties Gestiona las propiedades de los archivos descargados
     * @param typeFilter Aplicar el filtro por tipo de archivo y por directorio
     * @param allFiles Todos los archivos alamcenados en JSON
     * @param folderPaths Todas la rutas donde se realizan las descargas
     */
//    public InfoMedia(FileProperties fileProperties, TypeFilter typeFilter, Set<FileInformation> allFiles, Set<String> folderPaths, MediaPollingComponent mediaPollingComponent) {
    public InfoMedia(FileProperties fileProperties, TypeFilter typeFilter, Set<FileInformation> allFiles, Set<String> folderPaths) {
        initComponents();
        
        this.typeFilter = typeFilter;
        this.fileProperties = fileProperties;
        
        this.allData = fileProperties.loadDownloads(); //Carga la información guradada en JSON
        this.fileManager = new FileManager(allData, typeFilter, fileProperties); //Inicializa el gestor de archivos

        this.allFiles = allData.getFileList(); //Archivos locales
        folderPaths.addAll(allData.getFolderPaths()); //Añade las rutas de los directorios

        tblModel = new FileTableModel(new ArrayList<>(allFiles)); //Modelo de tabla
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
    
    /**
     * Configura posición de los componentes
     */
    private void setupLayout() {

        this.setLayout(new MigLayout("fill, insets 20","[40:150:n]10[grow]","[][grow]"));

        // Filtro de formato
        this.add(cbbxTypeFilter,"cell 1 0, right, width 100:200:200");

        // Lista de directorios
        this.add(scrFolderList,"cell 0 1, grow, w 40:150:n");

        // Tabla de archivos
        this.add(scrTableMedia,"cell 1 1, grow, w 0:n:n, pushx");
    }

    /** Configura el estilo de los componentes */
    private void setupStyle() {
        UIStyles.panelsBorders(this, UIStyles.DARK_GREY_COLOR, 30); //Panel principal
        
        UIStyles.styleScrollComponent(folderList, scrFolderList); //JScroll de la lista
        UIStyles.styleScrollComponent(tblMedia, scrTableMedia); //JScroll de la tabla
        
        UIStyles.styleComboBox(cbbxTypeFilter); //JComboBox para filtrar por tipo
        cbbxTypeFilter.setEditable(true); //Permite escribir en comboBox
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

        folderPaths.clear(); //Reconstruye rutas locales
        for (FileInformation fi : allFiles) {
            folderPaths.add(fi.getFolderPath());
        }

        for (String folder : folderPaths) { //Añade los directorios
            listModel.addElement(new FolderItem(folder));
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
        Set<FileInformation> resultFiles;

        resultFiles = fileManager.getLocalFiles(folder.getFullPath(), selectedFilter);

        tblModel.setFileList(new ArrayList<>(resultFiles));
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
        tblModel.setFileList(new ArrayList<>(allFiles));
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
            Logger.logError("Error opening local file: " + info.getName(), ex);
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

    /** Obtiene archivo seleccionado en la tabla. 
     * 
     * @return Archivo seleccionado o si es {@code null} si no hay selección
     */
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

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Downloads", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        setFocusCycleRoot(true);
        setLayout(new java.awt.BorderLayout());

        folderList.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        folderList.setPreferredSize(new java.awt.Dimension(40, 60));
        folderList.setSelectionBackground(new java.awt.Color(255, 204, 153));
        scrFolderList.setViewportView(folderList);

        add(scrFolderList, java.awt.BorderLayout.CENTER);

        scrTableMedia.setMinimumSize(null);

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
        tblMedia.setMinimumSize(null);
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
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Actualiza la tabla con el filtro aplicado en el JComboBox
     * 
     * @param evt Evento de acción del JComboBox
     */
    private void cbbxTypeFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxTypeFilterActionPerformed
        updateTableData();
    }//GEN-LAST:event_cbbxTypeFilterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbbxTypeFilter;
    private javax.swing.JList<FolderItem> folderList;
    private javax.swing.JScrollPane scrFolderList;
    private javax.swing.JScrollPane scrTableMedia;
    private javax.swing.JTable tblMedia;
    // End of variables declaration//GEN-END:variables
}