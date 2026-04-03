package montoya.mediabox.panels;

import montoya.mediabox.utils.Logger;
import java.awt.Color;
import java.io.*;
import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.controller.CardManager;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.configUI.UIStyles;
import montoya.mediabox.fileInformation.FileProperties;
import net.miginfocom.swing.MigLayout;

/**
 * Gestiona las preferencias antes de realizar una descarga.
 * 
 * <p> Configura:
 * <ul>
 * <li> Directorio temporal </li>
 * <li> Creación de archivo .m3u </li>
 * <li> Velocidad máxima de descarga </li>
 * <li> Encontra archivo yt-dlp.exe automáticamente </li>
 * </ul>
 *
 * @author Nerea
 */
public class Preferences extends javax.swing.JPanel {

    /** {@link MainFrame} Ventana principal donde se muestra el panel */
    private final MainFrame frame;
    
    /** {@link DownloadManager} Gestor de descarga */
    private final DownloadManager downloadManager;
    
    /** {@link CardManager} Gestionar el intercambio de paneles. */
    private final CardManager cardManager;
    
    /** Ruta detectada del archivo yt-dlp.exe */
    private String detectedYtDlpPath = "";

    /**
     * Constructor que inicializa el panel Preferences
     * 
     * @param frame Ventana principal donde se añade el panel
     * @param downloadManager Gestor de descargas
     * @param cardManager Gestiona el intercambio de paneles.
     */
    public Preferences(MainFrame frame, DownloadManager downloadManager, CardManager cardManager) {
        initComponents();

        this.frame = frame;
        this.downloadManager = downloadManager;
        this.cardManager = cardManager;
        
        setupLayout();
        setupStyle();
        mbSpeedSpinner();
    }
    
    /**
     * Configura posición de los componentes
     */
    private void setupLayout() {
        //Layout principal del panel
        this.setLayout(new MigLayout("fill, insets 20, wrap 1", "[grow, center]", "[grow][]")); 

        //Panel interno
        pnlPref.setLayout(new MigLayout("insets 40 30 40 30, gapy 15",
                "[grow][right, 0:pref:n, shrink 110]15[grow, center, 0:pref:800, shrink 100]15[pref, 0:pref:n][grow]",
                "push[][][][][25!]push[]push[]"));
        this.add(pnlPref, "center, growx, w 0:700:n, h n:800:n");

        //Ruta temporal
        pnlPref.add(lblPath, "cell 1 0, alignx right, w 10:pref:n");
        pnlPref.add(txtPathTemp, "cell 2 0, growx, w 0:400:800, h 35, shrink 120");
        pnlPref.add(btnBrowseTemp, "cell 3 0, w 30:50:50, h 30:50:50");

        //Checkbox de creación de M3U
        pnlPref.add(lblM3u, "cell 1 1, alignx right, gaptop 10, w 10:pref:n");
        pnlPref.add(chkCreate, "cell 2 1, alignx left, gaptop 10");

        //Spinner de velocidad y etiqueta
        pnlPref.add(lblSpeed, "cell 1 2, alignx right, aligny center, gaptop 10, w 10:pref:n");
        pnlPref.add(spnSpeed, "cell 2 2, split 2, alignx left, gaptop 10, w 20:100:150, h 30:35:n");
        pnlPref.add(lblSpeedValue, "gapleft 20, alignx left, gaptop 10");

        //Botón para buscar yt-dlp y label de info
        pnlPref.add(lblYtDlp, "cell 1 3, alignx right, gaptop 10, w 10:pref:n");
        pnlPref.add(btnYtDlp, "cell 2 3, growx, alignx left, gaptop 10, w 20:400:800, h 35!");
        pnlPref.add(lblInfo, "cell 2 4, alignx center, h 25!");
        
        //Botones de guardar y cancelar
        pnlPref.add(btnSave, "cell 2 5, split 2, growx, sizegroup buttons, alignx center, gaptop 40:push, w 40:120:400, h 40!, gapright 5");
        pnlPref.add(btnReturn, "growx, sizegroup buttons, gaptop 40:push, w 40:120:400, h 40!, gapleft 20, gapright 40");
        
        //Botón para limpiar archivo "Downloads.json"
        pnlPref.add(btnClearCache, "cell 1 6, span 3, align center, gaptop 20, gapbottom 10, w 150:200:300, h 35!");

        
        //Logo al final del panel
        this.add(logoLabel, "align center, gaptop 10:40:push, gapbottom 10:40:push");
    }

    
    /** Configura el estilo de los componentes */
    private void setupStyle(){
        //Panel principal y panel interno
        this.setBackground(UIStyles.BLACK_COLOR);
        UIStyles.panelsBorders(pnlPref, UIStyles.DARK_GREY_COLOR, 30);
        
        //Ruta temporal
        UIStyles.styleFixLabel(lblPath, "Temp Path: ", null);
        UIStyles.styleField(txtPathTemp, null, " Select folder for temporary files...", null, null, false);
        UIStyles.styleButtons(btnBrowseTemp, "","/images/folder.png", UIStyles.LIGHT_PURPLE, new Color (0,0,0),true, "Select Folder", null);
        
        //Checkbox de creación de M3U
        UIStyles.styleFixLabel(lblM3u, "File .m3u: ", null);
        UIStyles.styleCheckBox(chkCreate,"Create", "Create files .M3U");
        
        //Spinner de velocidad y etiqueta
        UIStyles.styleFixLabel(lblSpeed, "Speed (MB/s): ", null);
        UIStyles.styleFixLabel(lblSpeedValue, "" + spnSpeed.getValue() + " MB/s", null);
        UIStyles.styleSpinner(spnSpeed);
        
        //Botón para buscar yt-dlp y label de info
        UIStyles.styleFixLabel(lblYtDlp, "Location yt-dlp: ", null);
        UIStyles.styleButtons(btnYtDlp, "Automatic search yt-dlp", "/images/search.png", UIStyles.LIGHT_PURPLE, UIStyles.DARK_GREY_COLOR, true, "Automatic search yt-dlp", null);
        
        //Botón para limpiar archivo "Downloads.json"
        UIStyles.styleButtons(btnClearCache, "Clear Cache", "/images/clearCache.png",UIStyles.MEDIUM_GREY_COLOR, UIStyles.LIGHT_GREY_COLOR, true,"Delete downloads.json history without deleting files", null);
        
        //Botones de guardar y cancelar
        UIStyles.styleButtons(btnSave, "Save", "/images/save.png", UIStyles.LIGHT_PURPLE, UIStyles.DARK_GREY_COLOR, true, "Save changes", null);
        UIStyles.styleButtons(btnReturn, "Return", "/images/return.png", UIStyles.LIGHT_GREY_COLOR, UIStyles.DARK_GREY_COLOR, true, "Discard changes", null);
        
    }
    
    /** Configura el spinner de velocidad de descarga entre 0 y 100 MB/s y actualiza el label con el valor.*/
    public void mbSpeedSpinner() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0.0, 0.0, 100.0, 5.0);
        spnSpeed.setModel(spinnerModel);

        JSpinner.NumberEditor numberFormat = new JSpinner.NumberEditor(spnSpeed, "0.0"); //formato decimal
        spnSpeed.setEditor(numberFormat);

        spnSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                lblSpeedValue.setText(spnSpeed.getValue().toString()+ " MB/s"); //Actualiza label con el valor
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
        btnReturn = new javax.swing.JButton();
        txtPathTemp = new javax.swing.JTextField();
        btnBrowseTemp = new javax.swing.JButton();
        lblPath = new javax.swing.JLabel();
        logoLabel = new javax.swing.JLabel();
        spnSpeed = new javax.swing.JSpinner();
        lblSpeedValue = new javax.swing.JLabel();
        pnlPref = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        btnClearCache = new javax.swing.JButton();

        lblM3u.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        add(lblM3u);

        chkCreate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCreate.setForeground(new java.awt.Color(255, 255, 255));
        chkCreate.setToolTipText("");
        chkCreate.setMaximumSize(null);
        chkCreate.setMinimumSize(null);
        chkCreate.setPreferredSize(null);
        add(chkCreate);

        lblSpeed.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblSpeed.setMaximumSize(null);
        lblSpeed.setMinimumSize(null);
        lblSpeed.setPreferredSize(null);
        add(lblSpeed);

        lblYtDlp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblYtDlp.setMaximumSize(null);
        lblYtDlp.setMinimumSize(null);
        lblYtDlp.setPreferredSize(null);
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
        btnSave.setMinimumSize(null);
        btnSave.setPreferredSize(null);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        add(btnSave);

        btnReturn.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnReturn.setToolTipText("");
        btnReturn.setMaximumSize(null);
        btnReturn.setMinimumSize(null);
        btnReturn.setPreferredSize(null);
        btnReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnActionPerformed(evt);
            }
        });
        add(btnReturn);

        txtPathTemp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPathTemp.setMaximumSize(null);
        txtPathTemp.setMinimumSize(null);
        txtPathTemp.setPreferredSize(null);
        add(txtPathTemp);

        btnBrowseTemp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnBrowseTemp.setToolTipText("");
        btnBrowseTemp.setMaximumSize(null);
        btnBrowseTemp.setMinimumSize(null);
        btnBrowseTemp.setPreferredSize(null);
        btnBrowseTemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseTempActionPerformed(evt);
            }
        });
        add(btnBrowseTemp);

        lblPath.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblPath.setMaximumSize(null);
        lblPath.setMinimumSize(null);
        lblPath.setPreferredSize(null);
        add(lblPath);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.png"))); // NOI18N
        add(logoLabel);

        spnSpeed.setMaximumSize(null);
        spnSpeed.setMinimumSize(null);
        spnSpeed.setPreferredSize(null);
        add(spnSpeed);

        lblSpeedValue.setMaximumSize(null);
        lblSpeedValue.setMinimumSize(null);
        lblSpeedValue.setPreferredSize(null);
        add(lblSpeedValue);

        pnlPref.setMaximumSize(null);
        pnlPref.setMinimumSize(null);
        pnlPref.setPreferredSize(null);
        add(pnlPref);

        lblInfo.setMaximumSize(null);
        lblInfo.setMinimumSize(null);
        lblInfo.setPreferredSize(null);
        add(lblInfo);

        btnClearCache.setText("jButton1");
        btnClearCache.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearCacheActionPerformed(evt);
            }
        });
        add(btnClearCache);
    }// </editor-fold>//GEN-END:initComponents

    /** Directorio para archivos temporales.*/
    private void btnBrowseTempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseTempActionPerformed
        JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (directory.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { //Actualiza el campo de texto con la ruta
            File selectedFolder = directory.getSelectedFile();
            txtPathTemp.setText(selectedFolder.getAbsolutePath());
            txtPathTemp.setForeground(UIStyles.WHITE_COLOR);
        }
    }//GEN-LAST:event_btnBrowseTempActionPerformed

    /** Búsqueda automática del archivo yt-dlp.exe.*/
    private void btnYtDlpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYtDlpActionPerformed
        try {
            ProcessBuilder pb = new ProcessBuilder("where", "yt-dlp.exe"); //Buscar la ruta del archivo yt-dlp.exe
            Process p = pb.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())); //Lee salida del proceso
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            if (p.waitFor() == 0) { //Obtenemos la ruta encontrada
                String path = sb.toString().trim();
                detectedYtDlpPath = path.split("\n")[0];
                UIStyles.showMessageInfo(lblInfo, "yt-dlp.exe found!"); //Mensaje de éxito
            } else {
                detectedYtDlpPath = "";
                UIStyles.showMessageInfo(lblInfo, "yt-dlp.exe not found!");
            }

        } catch (IOException | InterruptedException ex) {
            Logger.logError("Error searching for yt-dlp.exe", ex);
            UIStyles.showMessageInfo(lblInfo, "An error occurred while searching for yt-dlp.exe.");
        }
    }//GEN-LAST:event_btnYtDlpActionPerformed

    /** Guarda las preferencias para ser utilizadas por el gestor de descargas {@link DownloadManager} */
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        downloadManager.setTempPath(txtPathTemp.getText().trim());
        downloadManager.setYtDlpLocation(detectedYtDlpPath);
        downloadManager.setCreateM3u(chkCreate.isSelected());
        downloadManager.setMaxSpeed(((Number) spnSpeed.getValue()).doubleValue());

        JOptionPane.showMessageDialog(this, "Preferences saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        cardManager.showCard("downloads"); //Panel (vista) Downloads
    }//GEN-LAST:event_btnSaveActionPerformed

    /** Retorna al panel Downloads sin guardar las preferencias */
    private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Changes will not be saved. Do you want to continue?", "Cancel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            txtPathTemp.setText("");
            chkCreate.setSelected(false);
            spnSpeed.setValue(0.0);
            cardManager.showCard("downloads"); //Panel (vista) Downloads
            lblInfo.setText("");
        }
    }//GEN-LAST:event_btnReturnActionPerformed

    /**
     * Borra el historial de descargas (JSON) tras confirmación del usuario.
     */
    private void btnClearCacheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearCacheActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure? This will clear the history but NOT the downloaded files.",
                "Clear history", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) { //Si pulsamos "YES"
            FileProperties fp = new FileProperties();
            fp.clearCache(); //Borramos contenido del archivo "download.json"

            JOptionPane.showMessageDialog(null, "History successfully cleared!", "Cache Cleared", JOptionPane.INFORMATION_MESSAGE);

            //Actualizamos tabla
            if (downloadManager.getInfoMedia() != null) {
                downloadManager.getInfoMedia().reloadMediaData();
            }
        }
    }//GEN-LAST:event_btnClearCacheActionPerformed


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowseTemp;
    private javax.swing.JButton btnClearCache;
    private javax.swing.JButton btnReturn;
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