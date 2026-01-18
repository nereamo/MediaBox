package montoya.mediabox.panels;

import java.io.*;
import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.controller.CardManager;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.styleConfig.StyleConfig;

/**
 * Panel que contiene las propiedades y metodos de JPanel Preferences
 *
 * @author Nerea
 */
public class Preferences extends javax.swing.JPanel {

    private final MainFrame frame;
    private final DownloadManager downloadManager;
    private final CardManager cardManager;
    
    public static final String CARD_DOWN = "downloads";

    public Preferences(MainFrame frame, DownloadManager downloadManager, CardManager cardManager) {
        initComponents();
        
        this.setBounds(0, 0, 1300, 770);
        this.setBackground(StyleConfig.PANEL_COLOR);
        
        this.frame = frame;
        this.downloadManager = downloadManager;
        this.cardManager = cardManager;
        
        StyleConfig.styleCheckBox(chkCreate, "Create files .M3U");
        
        
        
        //layoutComponents();
        configIconButtons();
        mbSpeedSpinner();
    }
    
    //Iconos de los botones
    public void configIconButtons(){
        StyleConfig.styleButtons(btnBrowseTemp, "/images/folder.png", "Select Folder");
        StyleConfig.styleButtons(btnYtDlp, "/images/folder.png", "Automatic search");
        StyleConfig.styleButtons(btnSave, "/images/save.png", "Save changes");
        StyleConfig.styleButtons(btnCancel, "/images/cancel.png", "Discard changes");  
    }

    //Limpiar entradas de JTextFields
    public void clearTextFields() {
        txtPathTemp.setText("");
        chkCreate.setSelected(false);
        spnSpeed.setValue(0);
        txtYtDlp.setText("");
    }

    //Velocidad de descarga hasta un max de 100MB/s
    public void mbSpeedSpinner() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, 0.0, 100.0, 10.0);
        spnSpeed.setModel(spinnerModel);

        JSpinner.NumberEditor numberFormat = new JSpinner.NumberEditor(spnSpeed, "0.0");
        spnSpeed.setEditor(numberFormat);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblM3u = new javax.swing.JLabel();
        chkCreate = new javax.swing.JCheckBox();
        lblSpeed = new javax.swing.JLabel();
        spnSpeed = new javax.swing.JSpinner();
        lblMbs = new javax.swing.JLabel();
        lblYtDlp = new javax.swing.JLabel();
        txtYtDlp = new javax.swing.JTextField();
        btnYtDlp = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtPathTemp = new javax.swing.JTextField();
        btnBrowseTemp = new javax.swing.JButton();
        lblPath = new javax.swing.JLabel();
        logoLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1300, 770));
        setPreferredSize(new java.awt.Dimension(1300, 770));
        setLayout(null);

        lblM3u.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblM3u.setForeground(new java.awt.Color(255, 255, 255));
        lblM3u.setText("File .m3u: ");
        add(lblM3u);
        lblM3u.setBounds(90, 170, 66, 20);

        chkCreate.setBackground(new java.awt.Color(61, 61, 64));
        chkCreate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCreate.setForeground(new java.awt.Color(255, 255, 255));
        chkCreate.setToolTipText("");
        add(chkCreate);
        chkCreate.setBounds(180, 170, 80, 19);

        lblSpeed.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblSpeed.setForeground(new java.awt.Color(255, 255, 255));
        lblSpeed.setText("Speed: ");
        add(lblSpeed);
        lblSpeed.setBounds(100, 240, 50, 20);

        spnSpeed.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        spnSpeed.setToolTipText("MB/s");
        add(spnSpeed);
        spnSpeed.setBounds(180, 240, 100, 23);

        lblMbs.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblMbs.setForeground(new java.awt.Color(255, 255, 255));
        lblMbs.setText("MB/s");
        add(lblMbs);
        lblMbs.setBounds(290, 240, 50, 20);

        lblYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblYtDlp.setForeground(new java.awt.Color(255, 255, 255));
        lblYtDlp.setText("Location yt-dlp:");
        add(lblYtDlp);
        lblYtDlp.setBounds(60, 320, 110, 20);

        txtYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(txtYtDlp);
        txtYtDlp.setBounds(270, 320, 330, 23);

        btnYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnYtDlp.setToolTipText("");
        btnYtDlp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYtDlpActionPerformed(evt);
            }
        });
        add(btnYtDlp);
        btnYtDlp.setBounds(170, 320, 90, 20);

        btnSave.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnSave.setToolTipText("");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        add(btnSave);
        btnSave.setBounds(290, 430, 80, 30);

        btnCancel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnCancel.setToolTipText("");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        add(btnCancel);
        btnCancel.setBounds(380, 430, 80, 30);

        txtPathTemp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(txtPathTemp);
        txtPathTemp.setBounds(270, 90, 330, 23);

        btnBrowseTemp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnBrowseTemp.setToolTipText("");
        btnBrowseTemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseTempActionPerformed(evt);
            }
        });
        add(btnBrowseTemp);
        btnBrowseTemp.setBounds(170, 90, 90, 20);

        lblPath.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblPath.setForeground(new java.awt.Color(255, 255, 255));
        lblPath.setText("Temp Path:");
        add(lblPath);
        lblPath.setBounds(80, 90, 80, 20);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/largelogoSmall3.png"))); // NOI18N
        add(logoLabel);
        logoLabel.setBounds(1100, 670, 180, 50);
    }// </editor-fold>//GEN-END:initComponents

    //Directorio para archivos temporales
    private void btnBrowseTempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseTempActionPerformed
        JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = directory.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = directory.getSelectedFile();
            txtPathTemp.setText(selectedFolder.getAbsolutePath());
        }
    }//GEN-LAST:event_btnBrowseTempActionPerformed

    //Buscar archivo yt-dlp.exe
    private void btnYtDlpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYtDlpActionPerformed
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

                txtYtDlp.setText(firstPath);
                JOptionPane.showMessageDialog(this, "yt-dlp.exe found!", "Found", JOptionPane.INFORMATION_MESSAGE);

            } else {
                txtYtDlp.setText("");
                JOptionPane.showMessageDialog(this, "yt-dlp.exe not found!", "Not Found", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            System.getLogger(Preferences.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (InterruptedException ex) {
            System.getLogger(Preferences.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_btnYtDlpActionPerformed

    //Boton save llama a metodo save
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        downloadManager.setTempPath(txtPathTemp.getText().trim());
        downloadManager.setYtDlpLocation(txtYtDlp.getText().trim());
        downloadManager.setCreateM3u(chkCreate.isSelected());
        downloadManager.setMaxSpeed(((Number) spnSpeed.getValue()).doubleValue());

        JOptionPane.showMessageDialog(this, "Preferences saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        cardManager.showCard(CARD_DOWN);
    }//GEN-LAST:event_btnSaveActionPerformed

    //Cancelar preferencias
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Changes will not be saved. Do you want to continue?", "Cancel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clearTextFields();
            cardManager.showCard(CARD_DOWN);
        }
    }//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowseTemp;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnYtDlp;
    private javax.swing.JCheckBox chkCreate;
    private javax.swing.JLabel lblM3u;
    private javax.swing.JLabel lblMbs;
    private javax.swing.JLabel lblPath;
    private javax.swing.JLabel lblSpeed;
    private javax.swing.JLabel lblYtDlp;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JSpinner spnSpeed;
    private javax.swing.JTextField txtPathTemp;
    private javax.swing.JTextField txtYtDlp;
    // End of variables declaration//GEN-END:variables
}
