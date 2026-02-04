package montoya.mediabox.panels;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
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
import net.miginfocom.swing.MigLayout;

/**
 * Panel que gestiona las descargas de medios en la aplicación MediaBox. 
 * @author Nerea
 */
public class Downloads extends javax.swing.JPanel {
    
    private MainFrame frame;
    public InfoMedia infoMedia;
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
        setLayout(new MigLayout("fill, insets 30, wrap 1", "[grow,center]", "[]20[]push[]"));
        downloadFilePnl.setLayout(new MigLayout("fillx, insets 20, wrap 1", "[center, grow]"));
        
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
        configComponents();
        styleComponents(); //Estilo de los componentes
        styleTxtUrl();
        configRadioButtons(btnGroup, radioMp4, radioMkv, radioWebm, radioMp3, radioWav, radioM4a);//Configura como se ven los botones
    }

    private void configPanels() {
        setBounds(0, 0, 1300, 770); //panel general
        setBackground(StyleConfig.DARK_BLUE_COLOR); //Color del panel
        downloadFilePnl.setPreferredSize(new Dimension(840, 580)); //tamaño del panel Download
        downloadFilePnl.setMinimumSize(new Dimension(640, 580));
        infoMedia.setPreferredSize(new Dimension(840, 410)); //tamaño del panel InfoMedia
        infoMedia.setMinimumSize(new Dimension(640, 410));
        add(downloadFilePnl, "center, growx, shrinkx");
        add(infoMedia, "center, growx, shrinkx");
        add(logoLabel, "align center");
    }
    
    
    private void configComponents() {
        downloadFilePnl.add(txtUrl, "split 2, growx, height 30!, gapleft 30, gaptop -20");
        downloadFilePnl.add(btnFolder, "width 50!, height 50!, gapright 20");

        pnlVideo.setLayout(new MigLayout("fillx, insets 10", "[]push[]push[]push[]", "[]"));
        pnlVideo.add(radioMp4);
        pnlVideo.add(radioMkv);
        pnlVideo.add(radioWebm);
        pnlVideo.add(cbbxQualityFilter, "width 120!");
        downloadFilePnl.add(pnlVideo, "width 730!, height 80!, gaptop 5");

        pnlAudio.setLayout(new MigLayout("fillx, insets 10", "[]15[]15[]15[]", "[]"));
        pnlAudio.add(radioMp3);
        pnlAudio.add(radioWav);
        pnlAudio.add(radioM4a);
        downloadFilePnl.add(pnlAudio, "width 730!, height 80!");

        downloadFilePnl.add(btnDownload, "split 2, width 180!, height 50!, gaptop 10");
        downloadFilePnl.add(btnOpenLast, "width 180!, height 50!, gaptop 10, gapleft 20");

        downloadFilePnl.add(progressBar, "width 730!, height 15!, gapy 15");
        downloadFilePnl.add(jScrollPane1, "width 730!, height 160!, growy");
        downloadFilePnl.add(lblInfoDownload, "growx, height 20!");
    }

    //Aplica estilos a los componentes
    private void styleComponents() {
        StyleConfig.createTitleBorder(downloadFilePnl, "DOWNLOAD FILE");
        StyleConfig.styleIconButton(btnFolder, "/images/folder.png", "Select destination folder");
        StyleConfig.createTitleBorder(pnlVideo, "VIDEO");
        StyleConfig.createTitleBorder(pnlAudio, "AUDIO");
        StyleConfig.styleButtonGroup("Select format", radioM4a, radioMkv, radioMp3, radioMp4, radioWav, radioWebm);
        StyleConfig.selectionColorComboBox(cbbxQualityFilter);
        StyleConfig.styleSimpleButton(btnDownload, "DOWNLOAD", "Download file", 150, 70, StyleConfig.LIGHT_BLUE_COLOR, StyleConfig.DARK_BLUE_COLOR);
        StyleConfig.styleSimpleButton(btnOpenLast, "OPEN LAST", "Reproduce last file", 150, 30, StyleConfig.GREY_COLOR, StyleConfig.DARK_BLUE_COLOR);
        progressBar.setBackground(StyleConfig.LIGHT_BLUE_COLOR);
        progressBar.setForeground(StyleConfig.DARK_BLUE_COLOR);
        areaInfo.setBackground(StyleConfig.DARK_BLUE_COLOR);
        areaInfo.setForeground(StyleConfig.LIGHT_BLUE_COLOR);
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

        downloadFilePnl.setMaximumSize(new java.awt.Dimension(840, 580));
        downloadFilePnl.setMinimumSize(new java.awt.Dimension(840, 580));
        downloadFilePnl.setPreferredSize(new java.awt.Dimension(840, 580));
        downloadFilePnl.setLayout(new java.awt.BorderLayout());

        txtUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        downloadFilePnl.add(txtUrl, java.awt.BorderLayout.CENTER);

        btnFolder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnFolder.setMaximumSize(new java.awt.Dimension(72, 50));
        btnFolder.setMinimumSize(new java.awt.Dimension(72, 50));
        btnFolder.setPreferredSize(new java.awt.Dimension(72, 50));
        btnFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolderActionPerformed(evt);
            }
        });
        downloadFilePnl.add(btnFolder, java.awt.BorderLayout.PAGE_START);

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

        downloadFilePnl.add(pnlVideo, java.awt.BorderLayout.PAGE_END);

        btnDownload.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnDownload.setMaximumSize(new java.awt.Dimension(150, 70));
        btnDownload.setMinimumSize(new java.awt.Dimension(150, 70));
        btnDownload.setPreferredSize(new java.awt.Dimension(150, 70));
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });
        downloadFilePnl.add(btnDownload, java.awt.BorderLayout.LINE_END);

        btnOpenLast.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnOpenLast.setText("Open Last");
        btnOpenLast.setMaximumSize(new java.awt.Dimension(150, 30));
        btnOpenLast.setMinimumSize(new java.awt.Dimension(150, 30));
        btnOpenLast.setPreferredSize(new java.awt.Dimension(150, 30));
        btnOpenLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenLastActionPerformed(evt);
            }
        });
        downloadFilePnl.add(btnOpenLast, java.awt.BorderLayout.LINE_START);

        progressBar.setBackground(new java.awt.Color(204, 204, 204));
        progressBar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        downloadFilePnl.add(progressBar, java.awt.BorderLayout.CENTER);

        areaInfo.setColumns(20);
        areaInfo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        areaInfo.setRows(5);
        jScrollPane1.setViewportView(areaInfo);

        downloadFilePnl.add(jScrollPane1, java.awt.BorderLayout.CENTER);

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

        downloadFilePnl.add(pnlAudio, java.awt.BorderLayout.CENTER);

        lblInfoDownload.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        downloadFilePnl.add(lblInfoDownload, java.awt.BorderLayout.CENTER);

        add(downloadFilePnl);
        downloadFilePnl.setBounds(30, 30, 880, 600);
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