package montoya.mediabox.controller;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import montoya.mediabox.MainFrame;
import montoya.mediabox.Preferences;

/**
 * Gestiona la configuración de elementos visuales de la interfaz
 * @author Nerea
 */
public class MainViewController {
    
    private final MainFrame frame;
    private final JPanel mainPanel;
    private final Preferences preferences;
    private final JProgressBar barProgress;
    
    
    public MainViewController(MainFrame frame, JPanel mainPanel, Preferences preferences, JProgressBar barProgress){
        this.frame = frame;
        this.mainPanel = mainPanel;
        this.preferences = preferences;
        this.barProgress = barProgress;
    }
    
    //Configuración de JFrame
    public void configFrame(){
        frame.setTitle("MediaBox");
        frame.setResizable(false);
        frame.setSize(1100,700);
        frame.setLocationRelativeTo(frame);
        frame.setLayout(null);
        
        mainPanel.setSize(1100,700);
        frame.getContentPane().add(mainPanel);
    }
    
    //Configuración de JPanel Preferences
    public void configPreferencesPanel(){
        preferences.setBounds(0, 0, 1100, 700);
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
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                barProgress.setValue(value);
            }
        });
    } 
}
