package montoya.mediabox.controller;

import java.awt.CardLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import montoya.mediabox.MainFrame;
import montoya.mediabox.panels.Preferences;
import montoya.mediabox.fileInformation.DirectoryInformation;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.fileInformation.FolderItem;
import montoya.mediabox.panels.DownloadsPanel;
import montoya.mediabox.panels.LoginPanel;

/**
 * Gestiona la configuración de elementos visuales de la interfaz
 *
 * @author Nerea
 */
public class MainViewController {

    private final MainFrame frame;
    private final JPanel pnlMain;
    private final Preferences preferences;
    private final JProgressBar barProgress;
    private final DataFilter df;

    public MainViewController(MainFrame frame, JPanel pnlMain, Preferences preferences, JProgressBar barProgress) {
        this.frame = frame;
        this.pnlMain = pnlMain;
        this.preferences = preferences;
        this.barProgress = barProgress;
        this.df = new DataFilter();
    }

    //Configuración de JFrame
    public void configFrame() {
        frame.setTitle("MediaBox");
        frame.setResizable(false);
        frame.setSize(1300, 800);
        frame.setLocationRelativeTo(frame);
        pnlMain.setSize(1300, 800);
    }
    
    public JPanel showCards(LoginPanel lp, JPanel mainPanel, Preferences p) {
        JPanel container = new JPanel(new CardLayout());
        container.add(lp, MainFrame.CARD_LOGIN);
        container.add(mainPanel, MainFrame.CARD_MAIN);
        container.add(p, MainFrame.CARD_PREF);
        
         CardLayout cl = (CardLayout) container.getLayout();
    cl.show(container, MainFrame.CARD_LOGIN);
    
        return container;
        
    }

    //Configuración de JPanel Preferences
    public void configPreferencesPanel() {
        preferences.setBounds(0, 0, 1300, 800);
    }

    //Mostrar el JPanel principal del JFrame
    public void showMainFramePanel() {
        frame.showCard(MainFrame.CARD_MAIN);
    }

    //Mostrar el JPanel Preferences 
    public void showPreferencesPanel() {
        frame.showCard(MainFrame.CARD_PREF);
    }
    
    public void configRadioButtons(ButtonGroup bg, JRadioButton... buttons) {
        String[] commands = {"mp4", "mkv", "webm", "mp3", "wav", "m4a"};
        for (int i = 0; i < buttons.length; i++) {
            bg.add(buttons[i]);
            buttons[i].setActionCommand(commands[i]);
        }
        buttons[0].setSelected(true);
    }
    
    public void applyQuality(JComboBox cbbxQuality){
        cbbxQuality.removeAllItems();
        cbbxQuality.addItem("1080");
        cbbxQuality.addItem("720");
        cbbxQuality.addItem("480");
    }

    //Actualiza el progreso de JProgressBar
    public void updateProgressBar(int value) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                barProgress.setValue(value);
            }
        });
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
                        List<FileInformation> filteredByDirectory = df.filterByDirectory(allFiles, folder.getFullPath());

                        //Filtra por tipo selccionado en comboBox
                        List<FileInformation> filteredByType = df.filterByType(filteredByDirectory, selectedFilter);

                        //Actualiza la tabla con los filtros aplicados
                        FileTableModel model = (FileTableModel) tblInfo.getModel();
                        model.setFileList(filteredByType);
                        model.fireTableDataChanged();
                    }
                }

            }
        });
    }
    
    public void configDwonloadsPanel(JPanel pnlMain, DownloadsPanel dp) {
        dp.setBounds(630, 40, 630, 400);
        pnlMain.add(dp);
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
}
