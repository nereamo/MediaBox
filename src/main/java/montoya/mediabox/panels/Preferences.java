package montoya.mediabox.panels;

import java.awt.Color;
import java.io.*;
import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.controller.CardManager;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.configUI.UIStyles;
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
    private String detectedYtDlpPath = "";

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
        this.setLayout(new MigLayout("fill, insets 40, wrap 1", "[grow, center]", "[grow, center]"));
        
        pnlPref.setLayout(new MigLayout("insets 30 15 30 15, gapy 15, align center center", "[right][left][pref]", "push[][][][][]push"));
        this.add(pnlPref, "grow, center, w 200:740:n, h 300:880:n");
        
        pnlPref.add(lblPath, "cell 0 0, alignx right");
        pnlPref.add(txtPathTemp, "cell 1 0, alignx left, growx, w 30:400:800, h 30!");
        pnlPref.add(btnBrowseTemp, "cell 2 0, w 50!, h 50!");
        
        pnlPref.add(lblM3u, "cell 0 1, alignx right, gaptop 15"); 
        pnlPref.add(chkCreate, "cell 1 1, alignx left, gaptop 15");

        pnlPref.add(lblSpeed, "cell 0 2, alignx right, aligny center, gaptop 15");
        pnlPref.add(spnSpeed, "cell 1 2, split 2, alignx left, gaptop 15, w 70!, h 30!");
        pnlPref.add(lblSpeedValue, "gapleft 20, alignx left, gaptop 15");
        
        pnlPref.add(lblYtDlp, "cell 0 3, alignx right, gaptop 15"); 
        pnlPref.add(btnYtDlp, "cell 1 3, alignx left, gaptop 20, w 50:250:300, h 35!");
        pnlPref.add(lblInfo, "cell 0 4, span, align center, gaptop 10, hidemode 3");
        
        pnlPref.add(btnSave, "cell 1 5, split 2, alignx center, gaptop 30, w 100:120:200, h 40!"); 
        pnlPref.add(btnCancel, "gaptop 30, gapleft 20, w 100:120:200, h 40!");
        
        this.add(logoLabel, "align center, shrink"); 
    }
    
    //Configuracion del estilo de los componentes
    private void applyStylesComponent(){
        this.setBackground(UIStyles.BLACK_COLOR);
        UIStyles.panelsBorders(pnlPref, UIStyles.DARK_GREY_COLOR, 30);
        
        UIStyles.styleFixLabel(lblPath, "Temp Path: ", "");
        UIStyles.addIconsTextField(txtPathTemp,"","", "Select folder for temporary files...");
        UIStyles.styleButtons(btnBrowseTemp, "/images/folder.png","", "Select Folder", UIStyles.DARK_GREY_COLOR, new Color (0,0,0), true);
        
        UIStyles.styleFixLabel(lblM3u, "File .m3u: ", "");
        UIStyles.styleCheckBox(chkCreate,"Create", "Create files .M3U");
        
        UIStyles.styleFixLabel(lblSpeed, "Speed (MB/s): ", "");
        UIStyles.styleFixLabel(lblSpeedValue, "" + spnSpeed.getValue() + " MB/s", "");
        UIStyles.styleSpinner(spnSpeed);
        
        UIStyles.styleFixLabel(lblYtDlp, "Location yt-dlp: ", "");
        UIStyles.styleButtons(btnYtDlp,"/images/search.png", "Automatic search yt-dlp", "Automatic search yt-dlp", UIStyles.LIGHT_PURPLE, UIStyles.DARK_GREY_COLOR, true);
        
        UIStyles.styleButtons(btnSave, "/images/save.png", "Save", "Save changes", UIStyles.LIGHT_PURPLE, UIStyles.DARK_GREY_COLOR, true);
        UIStyles.styleButtons(btnCancel, "/images/return.png", "Return", "Discard changes", UIStyles.LIGHT_GREY_COLOR, UIStyles.DARK_GREY_COLOR, true);
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
        btnYtDlp = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtPathTemp = new javax.swing.JTextField();
        btnBrowseTemp = new javax.swing.JButton();
        lblPath = new javax.swing.JLabel();
        logoLabel = new javax.swing.JLabel();
        spnSpeed = new javax.swing.JSpinner();
        lblSpeedValue = new javax.swing.JLabel();
        pnlPref = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();

        lblM3u.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(lblM3u);

        chkCreate.setBackground(new java.awt.Color(61, 61, 64));
        chkCreate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCreate.setForeground(new java.awt.Color(255, 255, 255));
        chkCreate.setToolTipText("");
        add(chkCreate);

        lblSpeed.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(lblSpeed);

        lblYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(lblYtDlp);

        btnYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnYtDlp.setToolTipText("");
        btnYtDlp.setMaximumSize(null);
        btnYtDlp.setMinimumSize(new java.awt.Dimension(120, 25));
        btnYtDlp.setPreferredSize(null);
        btnYtDlp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYtDlpActionPerformed(evt);
            }
        });
        add(btnYtDlp);

        btnSave.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnSave.setToolTipText("");
        btnSave.setMaximumSize(null);
        btnSave.setPreferredSize(null);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        add(btnSave);

        btnCancel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnCancel.setToolTipText("");
        btnCancel.setMaximumSize(null);
        btnCancel.setPreferredSize(null);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        add(btnCancel);

        txtPathTemp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPathTemp.setMinimumSize(null);
        txtPathTemp.setPreferredSize(null);
        add(txtPathTemp);

        btnBrowseTemp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnBrowseTemp.setToolTipText("");
        btnBrowseTemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseTempActionPerformed(evt);
            }
        });
        add(btnBrowseTemp);

        lblPath.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(lblPath);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.png"))); // NOI18N
        add(logoLabel);
        add(spnSpeed);
        add(lblSpeedValue);

        pnlPref.setMaximumSize(null);
        pnlPref.setMinimumSize(new java.awt.Dimension(200, 300));
        pnlPref.setPreferredSize(null);
        add(pnlPref);
        add(lblInfo);
    }// </editor-fold>//GEN-END:initComponents

    //Directorio para archivos temporales
    private void btnBrowseTempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseTempActionPerformed
        JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = directory.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = directory.getSelectedFile();
            txtPathTemp.setText(selectedFolder.getAbsolutePath());
            txtPathTemp.setForeground(UIStyles.WHITE_COLOR);
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
                detectedYtDlpPath = path.split("\n")[0];

                UIStyles.showMessageInfo(lblInfo, "yt-dlp.exe found!");
            } else {
                detectedYtDlpPath = "";
                UIStyles.showMessageInfo(lblInfo, "yt-dlp.exe not found!");
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
        downloadManager.setYtDlpLocation(detectedYtDlpPath);
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
            cardManager.showCard(CARD_DOWN);
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowseTemp;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnYtDlp;
    private javax.swing.JCheckBox chkCreate;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblM3u;
    private javax.swing.JLabel lblPath;
    private javax.swing.JLabel lblSpeed;
    private javax.swing.JLabel lblSpeedValue;
    private javax.swing.JLabel lblYtDlp;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPanel pnlPref;
    private javax.swing.JSpinner spnSpeed;
    private javax.swing.JTextField txtPathTemp;
    // End of variables declaration//GEN-END:variables
}