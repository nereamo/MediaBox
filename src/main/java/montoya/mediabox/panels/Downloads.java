package montoya.mediabox.panels;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import montoya.mediabox.MainFrame;
import montoya.mediabox.controller.TypeFilter;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.fileInformation.FolderItem;
import montoya.mediabox.styleConfig.StyleConfig;
import montoya.mediapollingcomponent.MediaPollingComponent;

/**
 * Panel que gestiona las descargas de medios en la aplicación MediaBox. 
 * @author Nerea
 */
public class Downloads extends javax.swing.JPanel {
    
    private MainFrame frame;
    private InfoMedia infoMedia;
    private DownloadManager downloadManager;
    private TypeFilter typeFilter;
    private FileTableModel tblModel;
    private final FileProperties fileProperties;
    private final MediaPollingComponent mediaPollingComponent;
    private final ButtonGroup btnGroup;
    private final Set<String> folderPaths;
    private List<FileInformation> allFiles = new ArrayList<>();
    private String folderPath = "";

    public Downloads(MainFrame frame, Set<String> folderPaths, DownloadManager downloadManager, MediaPollingComponent mediaPollingComponent) {

        initComponents();

        this.frame = frame;
        this.typeFilter = new TypeFilter();
        this.downloadManager = downloadManager;
        this.folderPaths = folderPaths;
        this.fileProperties = new FileProperties();
        this.mediaPollingComponent = mediaPollingComponent;

        btnGroup = new ButtonGroup();
        infoMedia = new InfoMedia(fileProperties, typeFilter, allFiles, folderPaths, mediaPollingComponent);
        downloadManager.setInfoMedia(infoMedia);
        tblModel = infoMedia.getTableModel();

        configPanels();
        qualityOptions(cbbxQualityFilter);//Aplica filtro de calidad
        //configComponents();
        styleComponents(); //Estilo de los componentes
        styleTxtUrl();
        configRadioButtons(btnGroup, radioMp4, radioMkv, radioWebm, radioMp3, radioWav, radioM4a);//Configura como se ven los botones

    }

    private void configPanels(){
        setBounds(0, 0, 1300, 770); //panel general
        setBackground(StyleConfig.DARK_BLUE_COLOR);
        downloadFilePnl.setBounds(40, 30, 840, 550); //Panel Download File
        infoMedia.setBounds(40, 590, 840, 460); //Panel InfoMedia
        this.add(infoMedia);
    }
    
    private void configComponents() {

    add(txtUrl, "cell 1 1, wmin 200, w 300, wmax 300, h 30!");
    add(btnFolder, "cell 2 1, h 30!");

    // Video + Audio en la misma fila
    add(pnlVideo, "cell 0 2, span 2, grow");
    add(pnlAudio, "cell 2 2, span 2, grow");

    // Botones
    add(btnDownload, "cell 0 3, span 2, split 2, alignx center");
    add(btnOpenLast, "alignx center");

    // Barra progreso
    add(progressBar, "cell 0 4, span 4, growx, h 12!");

    // Área info
    add(jScrollPane1, "cell 0 5, span 4, growx, h 150!");

    // Paneles grandes
    add(infoMedia, "cell 0 6, span 4, growx, h 350!");

    // Logo abajo derecha
    add(logoLabel, "cell 3 8, alignx right, aligny bottom");
}

    //Aplica estilos a los componentes
    private void styleComponents() {
        StyleConfig.createTitleBorder(downloadFilePnl, "DOWNLOAD FILE");
        StyleConfig.styleIconButton(btnFolder, "/images/folder.png", "Select destination folder");
        StyleConfig.createTitleBorder(pnlVideo, "VIDEO");
        StyleConfig.createTitleBorder(pnlAudio, "AUDIO");
        StyleConfig.styleButtonGroup("Select format", radioM4a, radioMkv, radioMp3, radioMp4, radioWav, radioWebm);
        StyleConfig.selectionColorComboBox(cbbxQualityFilter);
        StyleConfig.styleSimpleButton(btnDownload, "DOWNLOAD", "Download file", 120, 70, StyleConfig.LIGHT_BLUE_COLOR, StyleConfig.DARK_BLUE_COLOR);
        StyleConfig.styleSimpleButton(btnOpenLast, "OPEN LAST", "Reproduce last file", 120, 50, StyleConfig.GREY_COLOR, StyleConfig.DARK_BLUE_COLOR);
        
        progressBar.setBackground(StyleConfig.LIGHT_BLUE_COLOR);
        progressBar.setForeground(StyleConfig.DARK_BLUE_COLOR);
        
    }
    
    //Aplicar la calidad de video
    private void qualityOptions(JComboBox cbbxQuality){
        cbbxQuality.removeAllItems();
        cbbxQuality.addItem("1080");
        cbbxQuality.addItem("720");
        cbbxQuality.addItem("480");
    }
    
    //Configuración de ButtonGroup
    private void configRadioButtons(ButtonGroup bg, JRadioButton... buttons) {
        String[] commands = {"mp4", "mkv", "webm", "mp3", "wav", "m4a"};
        for (int i = 0; i < buttons.length; i++) {
            bg.add(buttons[i]);
            buttons[i].setActionCommand(commands[i]);
        }
        buttons[0].setSelected(true);
    }
    
    
    private void styleTxtUrl() {
        StyleConfig.addIconsTextField(txtUrl, "/images/url.png", "/images/delete_url.png", "Paste the URL of the file to download");

        txtUrl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int width = txtUrl.getWidth();

                if (e.getX() <= 30) {
                    pasteUrl();
                }else if (e.getX() >= width -30){
                    txtUrl.setText("");
                }
            }
        });

        txtUrl.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int width = txtUrl.getWidth();

                if (e.getX() <= 30 || e.getX() >= width -30) {
                    txtUrl.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    txtUrl.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
            }
        });
    }
        
    private void pasteUrl() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor df = DataFlavor.stringFlavor;

        if (cb.isDataFlavorAvailable(df)) {
            try {
                String clipboardContent = (String) cb.getData(df);
                txtUrl.setText(clipboardContent);
            } catch (UnsupportedFlavorException | IOException ex) {

                System.getLogger(MainFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoLabel = new javax.swing.JLabel();
        downloadFilePnl = new javax.swing.JPanel();
        txtUrl = new javax.swing.JTextField();
        lblUrl = new javax.swing.JLabel();
        btnFolder = new javax.swing.JButton();
        pnlVideo = new javax.swing.JPanel();
        radioMp4 = new javax.swing.JRadioButton();
        radioMkv = new javax.swing.JRadioButton();
        radioWebm = new javax.swing.JRadioButton();
        cbbxQualityFilter = new javax.swing.JComboBox<>();
        btnDownload = new javax.swing.JButton();
        btnOpenLast = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaInfo = new javax.swing.JTextArea();
        pnlAudio = new javax.swing.JPanel();
        radioWav = new javax.swing.JRadioButton();
        radioM4a = new javax.swing.JRadioButton();
        radioMp3 = new javax.swing.JRadioButton();
        lblInfoDownload = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1500, 770));
        setPreferredSize(new java.awt.Dimension(1300, 770));
        setLayout(null);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo3.png"))); // NOI18N
        add(logoLabel);
        logoLabel.setBounds(700, 1060, 180, 50);

        downloadFilePnl.setMaximumSize(new java.awt.Dimension(600, 670));
        downloadFilePnl.setMinimumSize(new java.awt.Dimension(600, 670));
        downloadFilePnl.setPreferredSize(new java.awt.Dimension(600, 670));
        downloadFilePnl.setLayout(null);

        txtUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        downloadFilePnl.add(txtUrl);
        txtUrl.setBounds(50, 40, 690, 23);

        lblUrl.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        downloadFilePnl.add(lblUrl);
        lblUrl.setBounds(20, 20, 340, 20);

        btnFolder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnFolder.setMaximumSize(new java.awt.Dimension(72, 50));
        btnFolder.setMinimumSize(new java.awt.Dimension(72, 50));
        btnFolder.setPreferredSize(new java.awt.Dimension(72, 50));
        btnFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolderActionPerformed(evt);
            }
        });
        downloadFilePnl.add(btnFolder);
        btnFolder.setBounds(750, 30, 72, 50);

        pnlVideo.setMaximumSize(new java.awt.Dimension(200, 210));
        pnlVideo.setLayout(null);

        radioMp4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp4.setText("MP4");
        pnlVideo.add(radioMp4);
        radioMp4.setBounds(20, 30, 70, 22);

        radioMkv.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMkv.setText("MKV");
        pnlVideo.add(radioMkv);
        radioMkv.setBounds(140, 30, 70, 22);

        radioWebm.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioWebm.setText("WEBM");
        pnlVideo.add(radioWebm);
        radioWebm.setBounds(260, 30, 90, 22);

        cbbxQualityFilter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbbxQualityFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quality" }));
        pnlVideo.add(cbbxQualityFilter);
        cbbxQualityFilter.setBounds(400, 30, 139, 23);

        downloadFilePnl.add(pnlVideo);
        pnlVideo.setBounds(50, 90, 560, 80);

        btnDownload.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnDownload.setMaximumSize(new java.awt.Dimension(120, 60));
        btnDownload.setMinimumSize(new java.awt.Dimension(120, 60));
        btnDownload.setPreferredSize(new java.awt.Dimension(120, 60));
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });
        downloadFilePnl.add(btnDownload);
        btnDownload.setBounds(630, 170, 150, 60);

        btnOpenLast.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnOpenLast.setText("Open Last");
        btnOpenLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenLastActionPerformed(evt);
            }
        });
        downloadFilePnl.add(btnOpenLast);
        btnOpenLast.setBounds(630, 240, 150, 24);

        progressBar.setBackground(new java.awt.Color(204, 204, 204));
        progressBar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        downloadFilePnl.add(progressBar);
        progressBar.setBounds(50, 300, 730, 10);

        areaInfo.setColumns(20);
        areaInfo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        areaInfo.setRows(5);
        jScrollPane1.setViewportView(areaInfo);

        downloadFilePnl.add(jScrollPane1);
        jScrollPane1.setBounds(50, 320, 730, 180);

        pnlAudio.setMaximumSize(new java.awt.Dimension(200, 210));
        pnlAudio.setLayout(null);

        radioWav.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioWav.setText("WAV");
        pnlAudio.add(radioWav);
        radioWav.setBounds(120, 30, 70, 22);

        radioM4a.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioM4a.setText("M4A");
        pnlAudio.add(radioM4a);
        radioM4a.setBounds(210, 30, 151, 22);

        radioMp3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp3.setText("MP3");
        pnlAudio.add(radioMp3);
        radioMp3.setBounds(30, 30, 80, 22);

        downloadFilePnl.add(pnlAudio);
        pnlAudio.setBounds(50, 190, 560, 80);

        lblInfoDownload.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        downloadFilePnl.add(lblInfoDownload);
        lblInfoDownload.setBounds(220, 510, 340, 20);

        add(downloadFilePnl);
        downloadFilePnl.setBounds(30, 30, 900, 700);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolderActionPerformed
        JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        directory.setDialogTitle("Select Download Destination");

        int result = directory.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = directory.getSelectedFile();

            if (selectedFolder != null && selectedFolder.isDirectory()) {

                this.folderPath = selectedFolder.getAbsolutePath();

                DefaultListModel<FolderItem> listModel = (DefaultListModel<FolderItem>) infoMedia.getListDirectories().getModel();

                boolean exists = false;
                for (int i = 0; i < listModel.size(); i++) {
                    if (listModel.getElementAt(i).getFullPath().equals(folderPath)) {
                        exists = true;
                        break;
                    }
                }

                // Si no existe, añadirlo
                if (!exists) {
                    listModel.addElement(new FolderItem(folderPath, false, false));
                }

                // Seleccionar automáticamente el directorio añadido
                FolderItem toSelect = new FolderItem(folderPath, false, false);
                infoMedia.getListDirectories().setSelectedValue(toSelect, true);
            }
        }
    }//GEN-LAST:event_btnFolderActionPerformed

    private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadActionPerformed
        String url = txtUrl.getText().trim();
        String folder = folderPath;
        String format = btnGroup.getSelection().getActionCommand();
        String quality = (String) cbbxQualityFilter.getSelectedItem();
        
        if (folderPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please, select a folder using the folder button first.", "No Folder Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        areaInfo.setText("");

        Thread th = new Thread() {
            @Override
            public void run() {
                downloadManager.download(url, folder, format, quality, areaInfo, progressBar, tblModel, folderPaths, lblInfoDownload);
            }
        };
        th.start();
    }//GEN-LAST:event_btnDownloadActionPerformed

    private void btnOpenLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenLastActionPerformed
        File lastFile = downloadManager.getLastDownloadedFile();
        if (lastFile != null && lastFile.exists()) {
            try {
                Desktop.getDesktop().open(lastFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Could not open file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No downloaded file found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnOpenLastActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaInfo;
    private javax.swing.JButton btnDownload;
    private javax.swing.JButton btnFolder;
    private javax.swing.JButton btnOpenLast;
    private javax.swing.JComboBox<String> cbbxQualityFilter;
    private javax.swing.JPanel downloadFilePnl;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInfoDownload;
    private javax.swing.JLabel lblUrl;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPanel pnlAudio;
    private javax.swing.JPanel pnlVideo;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JRadioButton radioM4a;
    private javax.swing.JRadioButton radioMkv;
    private javax.swing.JRadioButton radioMp3;
    private javax.swing.JRadioButton radioMp4;
    private javax.swing.JRadioButton radioWav;
    private javax.swing.JRadioButton radioWebm;
    private javax.swing.JTextField txtUrl;
    // End of variables declaration//GEN-END:variables
}
