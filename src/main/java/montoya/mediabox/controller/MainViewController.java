package montoya.mediabox.controller;

import java.io.File;
import javax.swing.*;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private final JRadioButton radioMp4;
    private final JRadioButton radioMp3;
    private final JProgressBar barProgress;

    public MainViewController(MainFrame frame, JPanel mainPanel, Preferences preferences, JRadioButton radioMp4, JRadioButton radioMp3, JProgressBar barProgress) {
        this.frame = frame;
        this.mainPanel = mainPanel;
        this.preferences = preferences;
        this.radioMp4 = radioMp4;
        this.radioMp3 = radioMp3;
        this.barProgress = barProgress;
    }

    //Configuración de JFrame
    public void configFrame() {
        frame.setTitle("MediaBox");
        frame.setResizable(false);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(frame);
        frame.setLayout(null);

        mainPanel.setSize(1200, 700);
        frame.getContentPane().add(mainPanel);
    }

    //Configuración de JPanel Preferences
    public void configPreferencesPanel() {
        preferences.setBounds(0, 0, 1200, 700);
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

    public void configRadioButtons() {
        ButtonGroup bg = new ButtonGroup();
        bg.add(radioMp4);
        bg.add(radioMp3);

        radioMp4.setSelected(true);
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
        cbbxFilter.addItem("MP3");
        cbbxFilter.addItem("M3U");
    }
    
    //Muestra las descargas pertenecientes a un directorio y permite flitrarlas por tipo de archivo
    public void showFilteredDownloads(String folderPath, String selectedFilter, List<FileInformation> fileList, JTable tblInfo) {

        List<FileInformation> filtered = new ArrayList<>();

        for (FileInformation fi : fileList) {

            if (!fi.folderPath.equals(folderPath)) {
                continue;
            }

            if (selectedFilter == null || selectedFilter.equals("All")) {
                filtered.add(fi);
            } else if (selectedFilter.equals("MP4") && fi.type.contains("mp4")) {
                filtered.add(fi);
            } else if (selectedFilter.equals("MP3") && fi.type.contains("mpeg")) {
                filtered.add(fi);
            } else if (selectedFilter.equals("m3u") && fi.type.contains("m3u")) {
                filtered.add(fi);
            }
        }

        tblInfo.setModel(new FileTableModel(filtered));
    }

    //Configuracion de JList, al seleccionar un directorio muestra las descargas.
    public void configDownloadList(JList<?> listDirectories, JComboBox<String> cbbxFilter, List<FileInformation> fileList, JTable tblInfo) {
        listDirectories.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Object selected = listDirectories.getSelectedValue();
                    if (selected instanceof FolderItem folder) {

                        String filtro = (String) cbbxFilter.getSelectedItem();
                        showFilteredDownloads(folder.getFullPath(), filtro, fileList, tblInfo);
                    }
                }
            }
        });
    }

    //Borra la descarga tanto física como de la interfaz y actualiza los directorios mostrados
    public void deleteDownload(FileInformation fileInfo, List<FileInformation> fileList, Set<String> directoriosDescarga, FileProperties fp) {

        //Borra descarga fisica
        File f = new File(fileInfo.folderPath, fileInfo.name); 
        if (f.exists()) {
            f.delete();
        }

        fileList.remove(fileInfo);

        //Si la carpeta esta vacia, la quita de JList lstDownload
        boolean emptyFolder = true; 
        for (FileInformation fi : fileList) {
            if (fi.folderPath.equals(fileInfo.folderPath)) {
                emptyFolder = false;
                break;
            }
        }

        if (emptyFolder) {
            directoriosDescarga.remove(fileInfo.folderPath);
        }

        fp.guardarDatos(new DirectoryInformation(fileList, directoriosDescarga));
    }
}
