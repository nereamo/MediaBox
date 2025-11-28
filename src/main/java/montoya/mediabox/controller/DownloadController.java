package montoya.mediabox.controller;


import java.io.*;
import java.io.IOException;
import java.awt.Desktop;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import montoya.mediabox.MainFrame;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.fileInformation.*;

/**
 *
 * @author Nerea
 */
public class DownloadController {
    
    private final DataFilter dataFilter = new DataFilter();
    private FileInformation fileInfo;
    private final DownloadManager downloadManager;
    private MainFrame frame;
    private final JTextArea areaInfo;
    private final JProgressBar progressBar;
    private final Set<String> directories;
    private final JList<FolderItem> listDirectories;
    private FileTableModel tableModel;
    
    public DownloadController(DownloadManager downloadManager, JList<FolderItem> listDirectories, FileTableModel tableModel, JTextArea areaInfo,JProgressBar progressBar, Set<String> directories) {
        this.downloadManager = downloadManager;
        this.listDirectories = listDirectories;
        this.tableModel = tableModel;
        this.areaInfo = areaInfo;
        this.progressBar = progressBar;
        this.directories = directories;
    }
    
    //Ventana que permite seleccionar un directorio
    public void browseFolder(JTextField txtFolder, JFrame frame){
        JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = directory.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = directory.getSelectedFile();

            if (selectedFolder != null && selectedFolder.isDirectory()) {
                String folderPath = selectedFolder.getAbsolutePath();
                txtFolder.setText(selectedFolder.getAbsolutePath());

                // Añadir el directorio a la lista si no está ya
                if (directories.add(folderPath)) {
                    DefaultListModel<FolderItem> listModel = (DefaultListModel<FolderItem>) listDirectories.getModel();
                    listModel.addElement(new FolderItem(folderPath));
                }
            }
        }
    }
    
    //El boton download inicia la descarga
    public void initDowload(String url, String folder, String format, String quality){
        downloadManager.setTempPath(folder);
        areaInfo.setText("");

        Thread th = new Thread() {
            @Override
            public void run() {
                downloadManager.download(url, folder, format, quality, areaInfo, progressBar, tableModel, listDirectories, directories);

                if (directories.add(folder)) {
                    SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        DefaultListModel<FolderItem> listModel = (DefaultListModel<FolderItem>) listDirectories.getModel();
                        listModel.addElement(new FolderItem(folder));
                     }
                    });
                }
            }
        };
        th.start();
    }
    
    //Abre ultimo archivo descargado con el reproductor
    public void openLastMedia(){
        File lastFile = downloadManager.getLastDownloadedFile();
        if (lastFile != null && lastFile.exists()) {
            try {
                Desktop.getDesktop().open(lastFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Could not open file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No downloaded file found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void applyFilter(FileTableModel tableModel, JList<FolderItem> listDirectories, JComboBox<String> cbbxTypeFilter){
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
            List<FileInformation> filteredByDirectory = dataFilter.filterByDirectory(allFiles, folderPath);

            //Filtrar por tipo
            List<FileInformation> filteredByType = dataFilter.filterByType(filteredByDirectory, filtro);

            //Actualizar tabla con los elementos filtrados
            tableModel.setFileList(filteredByType);
            tableModel.fireTableDataChanged();
        }
    }
    
    public void deleteFile(FileInformation fileInfo, FileTableModel tableModel, JList<FolderItem> listDirectories, JComboBox<String> cbbxTypeFilter){

        //Carga lista de .json
        FileProperties fp = new FileProperties();
        DirectoryInformation data = fp.loadDownloads();
        List<FileInformation> allFiles = data.downloads;
        Set<String> allDirs = data.downloadFolders;

        //Borra archivo físico y de .json
        fp.deleteDownload(fileInfo, allFiles, allDirs);

        //Actualiza la tabla dependiento del directorio seleccionado
        Object selected = listDirectories.getSelectedValue();
        if (selected instanceof FolderItem folder) {
            String filtro = (String) cbbxTypeFilter.getSelectedItem();

            List<FileInformation> filteredByDirectory = dataFilter.filterByDirectory(allFiles, folder.getFullPath());
            List<FileInformation> filteredByType = dataFilter.filterByType(filteredByDirectory, filtro);

            tableModel.setFileList(filteredByType);
            tableModel.fireTableDataChanged();
        } else {
            tableModel.setFileList(allFiles);
            tableModel.fireTableDataChanged();
        }

        //Actualiza los directorios en el listado
        DefaultListModel<FolderItem> newModel = new DefaultListModel<>();
        for (String folderPath : allDirs) {
            newModel.addElement(new FolderItem(folderPath));
        }
        listDirectories.setModel(newModel);
    }
    
    public void playFile(FileInformation info){
        
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
        
    }
    
    //Configuración de JList, al seleccionar un directorio muestra las descargas.
    public void configDownloadList(JList<?> listDirectories, JComboBox<String> cbbxFilter, JTable tblInfo) {

        listDirectories.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {
                    Object selected = listDirectories.getSelectedValue();
                    if (selected instanceof FolderItem folder) {

                        String selectedFilter = (String) cbbxFilter.getSelectedItem();

                        FileProperties fp = new FileProperties();
                        DirectoryInformation allData = fp.loadDownloads(); // lee JSON

                        //Lista completa de todas las descargas
                        List<FileInformation> allFiles = allData.downloads;

                        //Filtra por directorio seleccionado en JList
                        List<FileInformation> filteredByDirectory = dataFilter.filterByDirectory(allFiles, folder.getFullPath());

                        //Filtra por tipo selccionado en comboBox
                        List<FileInformation> filteredByType = dataFilter.filterByType(filteredByDirectory, selectedFilter);

                        //Actualiza la tabla con los filtros aplicados
                        FileTableModel model = (FileTableModel) tblInfo.getModel();
                        model.setFileList(filteredByType);
                        model.fireTableDataChanged();
                    }
                }

            }
        });
    }  
}
