package montoya.mediabox;

import java.io.*;
import javax.swing.*;
import montoya.mediabox.controller.MainViewController;
import montoya.mediabox.download.DownloadManager;

/**
 * Contiene las propiedades y metodos de JPanel Preferences
 *
 * @author Nerea
 */
public class Preferences extends javax.swing.JPanel {

    private MainFrame mainFrame;
    private MainViewController mvc;
    private final DownloadManager download;

    public Preferences(MainFrame mainFrame, DownloadManager download) {
        initComponents();
        this.mainFrame = mainFrame;
        this.download = download;
        mbSpeedSpinner();
    }

    public void setMainController(MainViewController mvc) {
        this.mvc = mvc;
    }

    //Limpiar entradas de JTextFields
    public void clearTextFields() {
        txtPathTemp.setText("");
        chkCreate.setSelected(false);
        spnSpeed.setValue(0);
        txtYtDlp.setText("");
    }

    //Guarda los valores de preferences
    public void savePreferences() {
        download.setTempPath(txtPathTemp.getText().trim());
        download.setYtDlpLocation(txtYtDlp.getText().trim());
        download.setCreateM3u(chkCreate.isSelected());
        download.setMaxSpeed(((Number) spnSpeed.getValue()).doubleValue());

        JOptionPane.showMessageDialog(this, "Preferences saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        mvc.showMainFramePanel();
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

        setMinimumSize(new java.awt.Dimension(1100, 670));
        setPreferredSize(new java.awt.Dimension(1100, 670));
        setLayout(null);

        lblM3u.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblM3u.setText("File .m3u: ");
        add(lblM3u);
        lblM3u.setBounds(60, 160, 70, 20);

        chkCreate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCreate.setText("Create");
        add(chkCreate);
        chkCreate.setBounds(60, 190, 80, 21);

        lblSpeed.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblSpeed.setText("Speed: ");
        add(lblSpeed);
        lblSpeed.setBounds(60, 240, 50, 20);

        spnSpeed.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(spnSpeed);
        spnSpeed.setBounds(60, 270, 100, 23);

        lblMbs.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblMbs.setText("MB/s");
        add(lblMbs);
        lblMbs.setBounds(170, 270, 50, 20);

        lblYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblYtDlp.setText("Location yt-dlp:");
        add(lblYtDlp);
        lblYtDlp.setBounds(60, 320, 110, 20);

        txtYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(txtYtDlp);
        txtYtDlp.setBounds(160, 350, 240, 23);

        btnYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnYtDlp.setText("Browse");
        btnYtDlp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYtDlpActionPerformed(evt);
            }
        });
        add(btnYtDlp);
        btnYtDlp.setBounds(60, 350, 90, 24);

        btnSave.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        add(btnSave);
        btnSave.setBounds(60, 470, 80, 23);

        btnCancel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        add(btnCancel);
        btnCancel.setBounds(150, 470, 80, 23);

        txtPathTemp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(txtPathTemp);
        txtPathTemp.setBounds(150, 90, 240, 23);

        btnBrowseTemp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnBrowseTemp.setText("Browse");
        btnBrowseTemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseTempActionPerformed(evt);
            }
        });
        add(btnBrowseTemp);
        btnBrowseTemp.setBounds(50, 90, 90, 24);

        lblPath.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblPath.setText("Temp Path:");
        add(lblPath);
        lblPath.setBounds(50, 60, 90, 20);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/largelogoSmall3.png"))); // NOI18N
        add(logoLabel);
        logoLabel.setBounds(900, 570, 180, 50);
    }// </editor-fold>//GEN-END:initComponents

    //Cancelar preferencias
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Changes will not be saved. Do you want to continue?", "Cancel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clearTextFields();
            mvc.showMainFramePanel();
        }
    }//GEN-LAST:event_btnCancelActionPerformed

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

    //Boton save llama a metodos save y clear
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        savePreferences();
        clearTextFields();
    }//GEN-LAST:event_btnSaveActionPerformed


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
