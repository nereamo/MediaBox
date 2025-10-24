package montoya.mediabox;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Nerea
 */
public class Preferences extends javax.swing.JPanel {
    
    private MainFrame mainFrame;
    private final Downloader downloader;
   
    public Preferences(MainFrame mainFrame, Downloader downloader) {
        initComponents();
        this.mainFrame = mainFrame; 
        this.downloader = downloader;
        mbSpeedSpinner();
    }
    
    //Limpiar entradas
    public void clearTextFields(){
        pathField.setText("");
        m3uCheck.setSelected(false);
        speedSpinner.setValue(0);
        locationField.setText("");
    }
    
    //Guardar valores de preferences
    public void savePreferences() {
        downloader.setTempPath(pathField.getText().trim());
        downloader.setYtDlpLocation(locationField.getText().trim());
        downloader.setCreateM3u(m3uCheck.isSelected());
        downloader.setMaxSpeed(((Number) speedSpinner.getValue()).doubleValue());

        JOptionPane.showMessageDialog(this, "Preferences saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.showMainPanel();
    }
    
    
    //Velocidad de descarga hasta un max de 100MB/s
    public void mbSpeedSpinner(){
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, 0.0, 100.0,10.0);
        speedSpinner.setModel(spinnerModel);
        
        JSpinner.NumberEditor numberFormat = new JSpinner.NumberEditor(speedSpinner, "0.0");
        speedSpinner.setEditor(numberFormat);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m3uLabel = new javax.swing.JLabel();
        m3uCheck = new javax.swing.JCheckBox();
        speedLabel = new javax.swing.JLabel();
        speedSpinner = new javax.swing.JSpinner();
        mbLabel = new javax.swing.JLabel();
        locationLabel = new javax.swing.JLabel();
        locationField = new javax.swing.JTextField();
        locationButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        pathField = new javax.swing.JTextField();
        tempButton = new javax.swing.JButton();
        pathLabel = new javax.swing.JLabel();
        logoLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(900, 670));
        setPreferredSize(new java.awt.Dimension(900, 670));
        setLayout(null);

        m3uLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m3uLabel.setText("File .m3u: ");
        add(m3uLabel);
        m3uLabel.setBounds(60, 160, 70, 20);

        m3uCheck.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m3uCheck.setText("Create");
        add(m3uCheck);
        m3uCheck.setBounds(60, 190, 80, 21);

        speedLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        speedLabel.setText("Speed: ");
        add(speedLabel);
        speedLabel.setBounds(60, 240, 50, 20);

        speedSpinner.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(speedSpinner);
        speedSpinner.setBounds(60, 270, 100, 23);

        mbLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mbLabel.setText("MB/s");
        add(mbLabel);
        mbLabel.setBounds(170, 270, 50, 20);

        locationLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        locationLabel.setText("Location yt-dlp:");
        add(locationLabel);
        locationLabel.setBounds(60, 320, 110, 20);

        locationField.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(locationField);
        locationField.setBounds(160, 350, 240, 23);

        locationButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        locationButton.setText("Browse");
        locationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationButtonActionPerformed(evt);
            }
        });
        add(locationButton);
        locationButton.setBounds(60, 350, 90, 24);

        saveButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        add(saveButton);
        saveButton.setBounds(60, 470, 80, 23);

        cancelButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        add(cancelButton);
        cancelButton.setBounds(150, 470, 80, 23);

        pathField.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(pathField);
        pathField.setBounds(150, 90, 240, 23);

        tempButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tempButton.setText("Browse");
        tempButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempButtonActionPerformed(evt);
            }
        });
        add(tempButton);
        tempButton.setBounds(50, 90, 90, 24);

        pathLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        pathLabel.setText("Temp Path:");
        add(pathLabel);
        pathLabel.setBounds(50, 60, 90, 20);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/largelogoSmall3.png"))); // NOI18N
        add(logoLabel);
        logoLabel.setBounds(700, 570, 180, 50);
    }// </editor-fold>//GEN-END:initComponents

    //Cancelar preferencias
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if(JOptionPane.showConfirmDialog(null, "Changes will not be saved. Do you want to continue?", "Cancel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            clearTextFields();
            mainFrame.showMainPanel();
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    //Directorio para archivos temporales
    private void tempButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempButtonActionPerformed
        JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = directory.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = directory.getSelectedFile();
            pathField.setText(selectedFolder.getAbsolutePath());
        } 
    }//GEN-LAST:event_tempButtonActionPerformed

    //Buscar archivo yt-dlp.exe
    private void locationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationButtonActionPerformed
        try {
            ProcessBuilder pb = new ProcessBuilder("where", "yt-dlp.exe");
            Process p = pb.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            if (p.waitFor() == 0) {
                
                String path = sb.toString().trim();
                String firstPath = path.split("\n")[0];

                locationField.setText(firstPath);
                JOptionPane.showMessageDialog(this,"Ruta de yt-dlp.exe encontrada.","Encontrado",JOptionPane.INFORMATION_MESSAGE);

            } else {
                locationField.setText("");
                JOptionPane.showMessageDialog(this,"No se pudo encontrar 'yt-dlp.exe'", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            System.getLogger(Preferences.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (InterruptedException ex) {
            System.getLogger(Preferences.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_locationButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        savePreferences();
        clearTextFields();
    }//GEN-LAST:event_saveButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton locationButton;
    private javax.swing.JTextField locationField;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JCheckBox m3uCheck;
    private javax.swing.JLabel m3uLabel;
    private javax.swing.JLabel mbLabel;
    private javax.swing.JTextField pathField;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JSpinner speedSpinner;
    private javax.swing.JButton tempButton;
    // End of variables declaration//GEN-END:variables
}
