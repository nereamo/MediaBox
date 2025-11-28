package montoya.mediabox.controller;

import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.panels.*;

/**
 * Gestiona la configuración de elementos visuales de la interfaz
 *
 * @author Nerea
 */
public class View {

    private final MainFrame frame;
    private final JPanel pnlMain;
    private final Preferences preferences;
    private final JProgressBar barProgress;
    private final DataFilter dataFilter;

    public View(MainFrame frame, JPanel pnlMain, Preferences preferences, JProgressBar barProgress) {
        this.frame = frame;
        this.pnlMain = pnlMain;
        this.preferences = preferences;
        this.barProgress = barProgress;
        this.dataFilter = new DataFilter();
    }
    
    // --- Views configuration ---
    public void configPanels(MainFrame frame, Login login, Preferences preferences, JPanel pnlMain, JPanel pnlMediaInfo) {
        //Frame
        frame.setTitle("MediaBox");
        frame.setResizable(false);
        frame.setSize(1300, 800);
        frame.setLocationRelativeTo(frame);
        pnlMain.setSize(1300, 800);
        
        //Login y preferences
        login.setBounds(0, 0, 1300, 800);
        preferences.setBounds(0, 0, 1300, 800);
        
        //infoMediaPanel
        pnlMediaInfo.setBounds(630, 40, 630, 400);
    }
    
    // --- UI Controls ---
    
    //Configuración de ButtonGroup
    public void configRadioButtons(ButtonGroup bg, JRadioButton... buttons) {
        String[] commands = {"mp4", "mkv", "webm", "mp3", "wav", "m4a"};
        for (int i = 0; i < buttons.length; i++) {
            bg.add(buttons[i]);
            buttons[i].setActionCommand(commands[i]);
        }
        buttons[0].setSelected(true);
    }

    //Aplicar la calidad de video
    public void qualityOptions(JComboBox cbbxQuality){
        cbbxQuality.removeAllItems();
        cbbxQuality.addItem("1080");
        cbbxQuality.addItem("720");
        cbbxQuality.addItem("480");
    }
    
    //Añade los filtros a JComboBox
    public void filterOptions(JComboBox cbbxFilter) {
        cbbxFilter.removeAllItems();
        cbbxFilter.addItem("All");
        cbbxFilter.addItem("MP4");
        cbbxFilter.addItem("MKV");
        cbbxFilter.addItem("WEBM");
        cbbxFilter.addItem("MP3");
        cbbxFilter.addItem("WAV");
        cbbxFilter.addItem("M4A");
    }

    
    // --- Downloads configuration ---

    //Actualiza el progreso de JProgressBar
    public void updateProgressBar(int value) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                barProgress.setValue(value);
            }
        });
    }
}
