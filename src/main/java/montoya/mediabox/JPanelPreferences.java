package montoya.mediabox;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Nerea
 */
public class JPanelPreferences extends javax.swing.JPanel {
    
    private MainFrame mainFrame;
    private boolean createM3u = false;
   
    public JPanelPreferences(MainFrame mainFrame) {
        initComponents();
        this.mainFrame = mainFrame; 
    }
    
    public void clearTextFields(){
        pathField.setText("");
        m3uCheck.setSelected(false);
        speedSpinner.setValue(0);
        locationField.setText("");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pathField = new javax.swing.JTextField();
        pathLabel = new javax.swing.JLabel();
        tempButton = new javax.swing.JButton();
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

        setMinimumSize(new java.awt.Dimension(900, 700));
        setPreferredSize(new java.awt.Dimension(900, 700));
        setLayout(null);

        pathField.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(pathField);
        pathField.setBounds(320, 80, 240, 23);

        pathLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        pathLabel.setText("Temp Path:");
        add(pathLabel);
        pathLabel.setBounds(190, 80, 90, 20);

        tempButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tempButton.setText("Browse");
        tempButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempButtonActionPerformed(evt);
            }
        });
        add(tempButton);
        tempButton.setBounds(590, 80, 90, 24);

        m3uLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m3uLabel.setText("File .m3u: ");
        add(m3uLabel);
        m3uLabel.setBounds(200, 130, 70, 20);

        m3uCheck.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m3uCheck.setText("Create");
        m3uCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m3uCheckActionPerformed(evt);
            }
        });
        add(m3uCheck);
        m3uCheck.setBounds(320, 130, 80, 21);

        speedLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        speedLabel.setText("Speed: ");
        add(speedLabel);
        speedLabel.setBounds(220, 180, 50, 20);

        speedSpinner.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(speedSpinner);
        speedSpinner.setBounds(320, 180, 100, 23);

        mbLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mbLabel.setText("MB/s");
        add(mbLabel);
        mbLabel.setBounds(430, 180, 90, 20);

        locationLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        locationLabel.setText("Location yt-dlp:");
        add(locationLabel);
        locationLabel.setBounds(170, 230, 110, 20);

        locationField.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(locationField);
        locationField.setBounds(320, 230, 240, 23);

        locationButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        locationButton.setText("Browse");
        locationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationButtonActionPerformed(evt);
            }
        });
        add(locationButton);
        locationButton.setBounds(590, 230, 90, 24);

        saveButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        saveButton.setText("Save");
        add(saveButton);
        saveButton.setBounds(320, 310, 80, 23);

        cancelButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        add(cancelButton);
        cancelButton.setBounds(440, 310, 80, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if(JOptionPane.showConfirmDialog(null, "Changes will not be saved. Do you want to continue?", "Cancel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            clearTextFields();
            mainFrame.showMainPanel();
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    //Abre la ruta (directorio) donde se guardara los archivos temp
    private void tempButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempButtonActionPerformed
        JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = directory.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = directory.getSelectedFile();
            pathField.setText(selectedFolder.getAbsolutePath());
        } 
    }//GEN-LAST:event_tempButtonActionPerformed

    private void locationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationButtonActionPerformed
        JFileChooser fileYt = new JFileChooser();
        fileYt.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fileYt.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileYt.getSelectedFile();
            locationField.setText(selectedFolder.getAbsolutePath());
        } 
    }//GEN-LAST:event_locationButtonActionPerformed

    //CheckBox para indicar si queremos crear el archivo .m3u
    private void m3uCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m3uCheckActionPerformed
        this.createM3u = m3uCheck.isSelected();
        System.out.println("Crear archivo m3u: " + this.createM3u); //Mensage por consola
    }//GEN-LAST:event_m3uCheckActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton locationButton;
    private javax.swing.JTextField locationField;
    private javax.swing.JLabel locationLabel;
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
