package montoya.mediabox.controller;

import java.io.File;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    public void mostrarDescargasPorDirectorio(String folderPath, List<FileInformation> fileList, JTable tblInfo) {
        List<FileInformation> filter = new ArrayList<>();

        for (FileInformation info : fileList) {
            if (info.folderPath.equals(folderPath)) {
                filter.add(info);
            }
        }

        tblInfo.setModel(new FileTableModel(filter));

    }

    //Configuracion de JList, al seleccionar un directorio muestra las descargas.
    public void configDownloadList(JList<?> lstDownloads, List<FileInformation> fileList, JTable tblInfo) {
        lstDownloads.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Object selected = lstDownloads.getSelectedValue();
                    if (selected instanceof FolderItem folder) {
                        mostrarDescargasPorDirectorio(folder.getFullPath(), fileList, tblInfo);
                    }
                }
            }
        });
    }

    //Borra la descarga tanto física como de la interfaz y actualiza los directorios mostrados
    public void deleteDownload(FileInformation info, List<FileInformation> fileList, Set<String> directoriosDescarga, FileProperties fp) {

        //Borra descarga fisica
        File f = new File(info.folderPath, info.name); 
        if (f.exists()) {
            f.delete();
        }

        fileList.remove(info);

        //Si la carpeta esta vacia, la quita de JList lstDownload
        boolean emptyFolder = true; 
        for (FileInformation fi : fileList) {
            if (fi.folderPath.equals(info.folderPath)) {
                emptyFolder = false;
                break;
            }
        }

        if (emptyFolder) {
            directoriosDescarga.remove(info.folderPath);
        }

        fp.guardarDatos(new DirectoryInformation(fileList, directoriosDescarga));
    }
}
