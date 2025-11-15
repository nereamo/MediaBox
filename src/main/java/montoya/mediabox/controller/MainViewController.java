package montoya.mediabox.controller;

import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import montoya.mediabox.MainFrame;
import montoya.mediabox.Preferences;
import montoya.mediabox.fileInformation.FolderItem;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.DirectoryInformation;

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
    public void applyFilters(JComboBox cbbxFilter) {
        cbbxFilter.removeAllItems();
        cbbxFilter.addItem("All");
        cbbxFilter.addItem("MP4");
        cbbxFilter.addItem("MKV");
        cbbxFilter.addItem("WEBM");
        cbbxFilter.addItem("MP3");
        cbbxFilter.addItem("WAV");
        cbbxFilter.addItem("M4A");
    }

    //Filtramos por directorio seleccionado en la lista
    public List<FileInformation> filterByDirectory(List<FileInformation> allFiles, String folderPath) {
        
        List<FileInformation> filter = new ArrayList<>();

        for (FileInformation fi : allFiles) {
            
            if (fi.folderPath.equals(folderPath)) {
                filter.add(fi);
            }
        }

        return filter;
    }

    //Filtramos por tipo de archivo en el combobox
    public List<FileInformation> filterByType(List<FileInformation> files, String selectedType) {

        List<FileInformation> filter = new ArrayList<>();
        
        for (FileInformation fi : files) {

            if (selectedType == null || selectedType.equals("All")) {
                filter.add(fi);
            } else {
                switch (selectedType) {
                    case "MP4":
                        if (fi.type.contains("mp4")) {
                            filter.add(fi);
                        }
                        break;
                    case "MKV":
                        if (fi.type.contains("x-matroska")) {
                            filter.add(fi);
                        }
                        break;
                    case "WEBM":
                        if (fi.type.contains("webm")) {
                            filter.add(fi);
                        }
                        break;
                    case "MP3":
                        if (fi.type.contains("mpeg")) {
                            filter.add(fi);
                        }
                        break;
                    case "WAV":
                        if (fi.type.contains("wav")) {
                            filter.add(fi);
                        }
                        break;
                    case "M4A":
                        if (fi.type.contains("m4a")) {
                            filter.add(fi);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return filter;
    }

    //Configuración de JList, al seleccionar un directorio muestra las descargas.
    public void configDownloadList(JList<?> listDirectories, JComboBox<String> cbbxFilter, JTable tblInfo) {
        
        listDirectories.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e){
                
                if (!e.getValueIsAdjusting()) {
                Object selected = listDirectories.getSelectedValue();
                if (selected instanceof FolderItem folder) {

                    String selectedFilter = (String) cbbxFilter.getSelectedItem();

                    FileProperties fp = new FileProperties();
                    DirectoryInformation allData = fp.loadDownloads(); // lee JSON

                    //Lista completa de todas las descargas
                    List<FileInformation> allFiles = allData.downloads;

                    //Filtra por directorio seleccionado en JList
                    List<FileInformation> filteredByDirectory = filterByDirectory(allFiles, folder.getFullPath());

                    //Filtra por tipo selccionado en comboBox
                    List<FileInformation> filteredByType = filterByType(filteredByDirectory, selectedFilter);

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
