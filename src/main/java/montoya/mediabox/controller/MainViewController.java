package montoya.mediabox.controller;

import java.io.File;
import javax.swing.*;
import java.util.List;
import java.util.Set;
import montoya.mediabox.MainFrame;
import montoya.mediabox.Preferences;
import montoya.mediabox.fileInformation.DirectoryInformation;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.fileInformation.FolderItem;

/**
 * Gestiona la configuración de elementos visuales de la interfaz
 *
 * @author Nerea
 */
public class MainViewController {

    private final MainFrame frame;
    private final JPanel mainPanel;
    private final Preferences preferences;
    private final JProgressBar barProgress;

    public MainViewController(MainFrame frame, JPanel mainPanel, Preferences preferences, JProgressBar barProgress) {
        this.frame = frame;
        this.mainPanel = mainPanel;
        this.preferences = preferences;
        this.barProgress = barProgress;
    }

    //Configuración de JFrame
    public void configFrame() {
        frame.setTitle("MediaBox");
        frame.setResizable(false);
        frame.setSize(1300, 800);
        frame.setLocationRelativeTo(frame);
        frame.setLayout(null);

        mainPanel.setSize(1300, 800);
        frame.getContentPane().add(mainPanel);
    }

    //Configuración de JPanel Preferences
    public void configPreferencesPanel() {
        preferences.setBounds(0, 0, 1300, 800);
        preferences.setVisible(false);
        frame.getContentPane().add(preferences);
    }

    //Mostrar el JPanel principal del JFrame
    public void showMainFramePanel() {
        preferences.setVisible(false);
        mainPanel.setVisible(true);
    }

    //Mostrar el JPanel Preferences 
    public void showPreferencesPanel() {
        mainPanel.setVisible(false);
        preferences.setVisible(true);
    }

    //Actualiza el progreso de JProgressBar
    public void updateProgressBar(int value) {
        //SwingUtilities.invokeLater(() -> barProgress.setValue(value)); --> lambda

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                barProgress.setValue(value);
            }
        });
    }
    
    //Añade los filtros a JComboBox
    public void applyFilters(JComboBox cbbxFilter){
        cbbxFilter.removeAllItems();
        cbbxFilter.addItem("All");
        cbbxFilter.addItem("MP4");
        cbbxFilter.addItem("MKV");
        cbbxFilter.addItem("WEBM");
        cbbxFilter.addItem("MP3");
        cbbxFilter.addItem("WAV");
        cbbxFilter.addItem("M4A");
    }
    
//    //Muestra las descargas pertenecientes a un directorio y permite flitrarlas por tipo de archivo
//    public void showTypeFiltered(String folderPath, String selectedFilter, List<FileInformation> fileList, JTable tblInfo) {
//
//        List<FileInformation> filtered = new ArrayList<>();
//
//        for (FileInformation fi : fileList) {
//
//            if (!fi.folderPath.equals(folderPath)) {
//                continue;
//            }
//
//            if (selectedFilter == null || selectedFilter.equals("All")) {
//                filtered.add(fi);
//            } else if (selectedFilter.equals("MP4") && fi.type.contains("mp4")) {
//                filtered.add(fi);
//            } else if (selectedFilter.equals("MKV") && fi.type.contains("x-matroska")) {
//                filtered.add(fi);
//            } else if (selectedFilter.equals("WEBM") && fi.type.contains("webm")) {
//                filtered.add(fi);
//            } else if (selectedFilter.equals("MP3") && fi.type.contains("mpeg")) {
//                filtered.add(fi);
//            } else if (selectedFilter.equals("WAV") && fi.type.contains("wav")) {
//                filtered.add(fi);
//            } else if (selectedFilter.equals("M4A") && fi.type.contains("m4a")) {
//                filtered.add(fi);
//            }
//        }
//
//        // 🔹 Actualiza la tabla en lugar de crear un nuevo modelo
//        FileTableModel model = (FileTableModel) tblInfo.getModel();
//        model.setFileList(filtered);
//    }
    
    public List<FileInformation> filterByDirectory(List<FileInformation> allFiles, String folderPath) {
    return allFiles.stream()
            .filter(f -> f.folderPath.equals(folderPath))
            .toList(); // o .collect(Collectors.toList()) si Java 8
}
    
    public List<FileInformation> filterByType(List<FileInformation> files, String selectedType) {
    if (selectedType == null || selectedType.equals("All")) {
        return files;
    }

    return files.stream().filter(f -> {
        return switch (selectedType) {
            case "MP4" -> f.type.contains("mp4");
            case "MKV" -> f.type.contains("x-matroska");
            case "WEBM" -> f.type.contains("webm");
            case "MP3" -> f.type.contains("mpeg");
            case "WAV" -> f.type.contains("wav");
            case "M4A" -> f.type.contains("m4a");
            default -> false;
        };
    }).toList();
}
    
    // Configuración de JList, al seleccionar un directorio muestra las descargas.
public void configDownloadList(JList<?> listDirectories, JComboBox<String> cbbxFilter, JTable tblInfo) {
    listDirectories.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            Object selected = listDirectories.getSelectedValue();
            if (selected instanceof FolderItem folder) {

                String selectedFilter = (String) cbbxFilter.getSelectedItem();
                
                FileProperties fp = new FileProperties();
DirectoryInformation allData = fp.cargarDatos(); // lee JSON

                // Lista maestra de todos los archivos descargados
                List<FileInformation> allFiles = allData.downloads;

                // 1️⃣ Filtrar por directorio
                List<FileInformation> filteredByDirectory = filterByDirectory(allFiles, folder.getFullPath());

                // 2️⃣ Filtrar por tipo
                List<FileInformation> filteredByType = filterByType(filteredByDirectory, selectedFilter);

                // 3️⃣ Actualizar el modelo de la tabla con los archivos filtrados
                FileTableModel model = (FileTableModel) tblInfo.getModel();
                model.setFileList(filteredByType);
                model.fireTableDataChanged();
            }
        }
    });
}

//    //Configuracion de JList, al seleccionar un directorio muestra las descargas.
//    public void configDownloadList(JList<?> listDirectories, JComboBox<String> cbbxFilter, List<FileInformation> fileList, JTable tblInfo) {
//        listDirectories.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    Object selected = listDirectories.getSelectedValue();
//                    if (selected instanceof FolderItem folder) {
//
//                        String filtro = (String) cbbxFilter.getSelectedItem();
//                        
//                        // Lista maestra de todos los archivos
//List<FileInformation> allFiles = cargarTodosLosArchivos();
//                        
//                        // 🔹 Filtrar solo los archivos que pertenecen al directorio seleccionado
//                    List<FileInformation> filteredFiles = fileList.stream()
//                            .filter(f -> f.folderPath.equals(folder.getFullPath()))
//                            .toList(); // si usas Java 8: .collect(Collectors.toList())
//
//                    // 🔹 Actualizar el modelo de la tabla con los archivos filtrados
//                    FileTableModel model = (FileTableModel) tblInfo.getModel();
//                    model.setFileList(filteredFiles);
//                    model.fireTableDataChanged();
//                        showTypeFiltered(folder.getFullPath(), filtro, fileList, tblInfo);
//                    }
//                }
//            }
//        });
//    }

//    //Borra la descarga tanto física como de la interfaz y actualiza los directorios mostrados
//    public void deleteDownload(FileInformation fileInfo, List<FileInformation> fileList, Set<String> directoriosDescarga, FileProperties fp) {
//
//        //Borra descarga fisica
//        File f = new File(fileInfo.folderPath, fileInfo.name); 
//        if (f.exists()) {
//            f.delete();
//        }
//
//        fileList.remove(fileInfo);
//
//        //Si la carpeta esta vacia, la quita de JList lstDownload
//        boolean emptyFolder = true; 
//        for (FileInformation fi : fileList) {
//            if (fi.folderPath.equals(fileInfo.folderPath)) {
//                emptyFolder = false;
//                break;
//            }
//        }
//
//        if (emptyFolder) {
//            directoriosDescarga.remove(fileInfo.folderPath);
//        }
//        
//        fp.guardarTodo(new DirectoryInformation(fileList, directoriosDescarga));
//
//        //fp.guardarDatos(new DirectoryInformation(fileList, directoriosDescarga));
//    }

public void deleteDownload(FileInformation fileInfo, List<FileInformation> allFiles, Set<String> allDirs, FileProperties fp) {

    // 1️⃣ Borra archivo físico
    File f = new File(fileInfo.folderPath, fileInfo.name);
    if (f.exists()) {
        f.delete();
    }

    // 2️⃣ Quita el archivo de la lista maestra
    allFiles.removeIf(fi -> fi.name.equals(fileInfo.name) && fi.folderPath.equals(fileInfo.folderPath));

    // 3️⃣ Si la carpeta quedó vacía, quítala de los directorios
    boolean emptyFolder = allFiles.stream().noneMatch(fi -> fi.folderPath.equals(fileInfo.folderPath));
    if (emptyFolder) {
        allDirs.remove(fileInfo.folderPath);
    }

    // 4️⃣ Guarda la lista maestra actualizada en JSON
    fp.guardarTodo(new DirectoryInformation(allFiles, allDirs));
}
}
