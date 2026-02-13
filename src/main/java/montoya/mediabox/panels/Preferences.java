package montoya.mediabox.panels;

import java.io.*;
import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.controller.CardManager;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.configUI.SwingStyleUtils;
import net.miginfocom.swing.MigLayout;

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

        this.frame = frame;
        this.downloadManager = downloadManager;
        this.cardManager = cardManager;
        
        setupLayout();
        applyStylesComponent();
        mbSpeedSpinner();
    }
    
    //Configuracion de la posición de los componentes
    private void setupLayout(){
        
        this.setLayout(new MigLayout("center, insets 20, gapy 20", "[grow][right][shrink 100, grow 0][pref][grow]", "push[][][][][]push[]"));
        this.setBackground(SwingStyleUtils.DARK_BLUE_COLOR);
        
        add(lblPath, "cell 0 0, alignx right");
        add(txtPathTemp, "cell 1 0, alignx left, growx, wmin 200, w 300, wmax 300");
        add(btnBrowseTemp, "cell 2 0");
        
        add(lblM3u, "cell 0 1, alignx right, gaptop 15"); 
        add(chkCreate, "cell 1 1, alignx left, gaptop 15");

        add(lblSpeed, "cell 0 2, alignx right, aligny center, gaptop 15");
        add(spnSpeed, "cell 1 2, split 2, alignx left, gaptop 15, w 70!");
        add(lblSpeedValue, "gapleft 20, alignx left, gaptop 15");
        
        add(lblYtDlp, "cell 0 3, alignx right, gaptop 15"); 
        add(txtYtDlp, "cell 1 3,alignx left, growx, wmin 200, w 300, wmax 300, gaptop 15"); 
        add(btnYtDlp, "cell 2 3, gaptop 20");
        
        add(btnSave, "cell 1 4, split 2, alignx center, gaptop 30"); 
        add(btnCancel, "gaptop 30");
        
        add(logoLabel, "cell 1 6, alignx center, aligny bottom"); 
    }
    
    //Configuracion del estilo de los componentes
    private void applyStylesComponent(){
        SwingStyleUtils.styleFixLabel(lblPath, "Temp Path: ");
        SwingStyleUtils.styleFixLabel(lblM3u, "File .m3u: ");
        SwingStyleUtils.styleFixLabel(lblSpeed, "Speed (MB/s): ");
        SwingStyleUtils.styleFixLabel(lblSpeedValue, "" + spnSpeed.getValue() + " MB/s");
        SwingStyleUtils.styleFixLabel(lblYtDlp, "Location yt-dlp: ");
        
        SwingStyleUtils.styleIconButton(btnBrowseTemp, "/images/folder.png", "Select Folder");
        SwingStyleUtils.styleIconButton(btnYtDlp, "/images/search.png", "Automatic search");
        SwingStyleUtils.styleCheckBox(chkCreate,"Create", "Create files .M3U");
        SwingStyleUtils.styleSpinner(spnSpeed);
        SwingStyleUtils.styleIconButton(btnSave, "/images/save.png", "Save changes");
        SwingStyleUtils.styleIconButton(btnCancel, "/images/return.png", "Discard changes");
    }
    
    //Velocidad de descarga hasta un max de 100MB/s
    public void mbSpeedSpinner() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, 0.0, 100.0, 5.0);
        spnSpeed.setModel(spinnerModel);

        JSpinner.NumberEditor numberFormat = new JSpinner.NumberEditor(spnSpeed, "0.0");
        spnSpeed.setEditor(numberFormat);

        spnSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                lblSpeedValue.setText(spnSpeed.getValue().toString()+ " MB/s");
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblM3u = new javax.swing.JLabel();
        chkCreate = new javax.swing.JCheckBox();
        lblSpeed = new javax.swing.JLabel();
        lblYtDlp = new javax.swing.JLabel();
        txtYtDlp = new javax.swing.JTextField();
        btnYtDlp = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtPathTemp = new javax.swing.JTextField();
        btnBrowseTemp = new javax.swing.JButton();
        lblPath = new javax.swing.JLabel();
        logoLabel = new javax.swing.JLabel();
        spnSpeed = new javax.swing.JSpinner();
        lblSpeedValue = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1300, 770));
        setPreferredSize(new java.awt.Dimension(1300, 770));
        setLayout(null);

        lblM3u.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(lblM3u);
        lblM3u.setBounds(80, 170, 90, 20);

        chkCreate.setBackground(new java.awt.Color(61, 61, 64));
        chkCreate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCreate.setForeground(new java.awt.Color(255, 255, 255));
        chkCreate.setToolTipText("");
        add(chkCreate);
        chkCreate.setBounds(180, 170, 80, 19);

        lblSpeed.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(lblSpeed);
        lblSpeed.setBounds(100, 240, 50, 20);

        lblYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
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
        btnSave.setPreferredSize(new java.awt.Dimension(100, 50));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        add(btnSave);
        btnSave.setBounds(250, 430, 100, 50);

        btnCancel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnCancel.setToolTipText("");
        btnCancel.setPreferredSize(new java.awt.Dimension(100, 50));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        add(btnCancel);
        btnCancel.setBounds(370, 430, 100, 50);

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
        add(lblPath);
        lblPath.setBounds(80, 90, 80, 20);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo2png.png"))); // NOI18N
        add(logoLabel);
        logoLabel.setBounds(310, 630, 240, 70);
        add(spnSpeed);
        spnSpeed.setBounds(280, 250, 64, 22);
        add(lblSpeedValue);
        lblSpeedValue.setBounds(390, 250, 0, 0);
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
            txtPathTemp.setText("");
            chkCreate.setSelected(false);
            spnSpeed.setValue(0.0);
            txtYtDlp.setText("");
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
    private javax.swing.JLabel lblPath;
    private javax.swing.JLabel lblSpeed;
    private javax.swing.JLabel lblSpeedValue;
    private javax.swing.JLabel lblYtDlp;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JSpinner spnSpeed;
    private javax.swing.JTextField txtPathTemp;
    private javax.swing.JTextField txtYtDlp;
    // End of variables declaration//GEN-END:variables
}