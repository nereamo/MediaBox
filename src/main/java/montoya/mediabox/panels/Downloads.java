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
import montoya.mediabox.configUI.UIStyles;
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

        setupLayout();
        qualityOptions(cbbxQualityFilter);
        applyStylesComponent();
        styleTxtUrl();
        configRadioButtons(btnGroup, radioMp4, radioMkv, radioWebm, radioMp3, radioWav, radioM4a);
        
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    //Posición d elos componentes del panel
    private void setupLayout() {
        this.setLayout(new MigLayout("fill, insets 30, wrap 1", "[grow, center]", "push[grow]50![grow]push"));
        this.add(downloadFilePnl, "grow, center, w 200:840:n, h 150:500:n, gaptop 40");
        this.add(infoMedia, "grow, center, w 200:840:n, h 150:430:n");
        this.add(logoLabel, "align center, gapbottom 40");

        downloadFilePnl.setLayout(new MigLayout("fillx, insets 20, wrap 1", "[grow]"));
        downloadFilePnl.add(txtUrl, "split 2, growx, h 35!, gapright 10");
        downloadFilePnl.add(btnFolder, "w 50!, h 50!");

        pnlVideo.setLayout(new MigLayout("fillx, insets 17", "[]push[]push[]push[]", "[]"));
        pnlVideo.add(radioMp4);
        pnlVideo.add(radioMkv);
        pnlVideo.add(radioWebm);
        pnlVideo.add(cbbxQualityFilter, "w 120!");
        downloadFilePnl.add(pnlVideo, "growx, h 70!, gaptop 30");

        pnlAudio.setLayout(new MigLayout("fillx, insets 17", "[]15[]15[]", "[]"));
        pnlAudio.add(radioMp3);
        pnlAudio.add(radioWav);
        pnlAudio.add(radioM4a);
        downloadFilePnl.add(pnlAudio, "growx, h 70!");

        downloadFilePnl.add(btnDownload, "split 2, w 180!, h 50!, gaptop 40, align center");
        downloadFilePnl.add(btnOpenLast, "w 180!, h 50!, gaptop 30, gapleft 20");

        downloadFilePnl.add(progressBar, "growx, h 25!, gaptop 50, gapbottom 10, pushy, aligny bottom");
    }

    //Aplica estilos a los componentes
    private void applyStylesComponent() {
        setBackground(UIStyles.BLACK_COLOR); //Color del panel
        UIStyles.panelsBorders(downloadFilePnl, UIStyles.DARK_GREY_COLOR, 30);
        UIStyles.styleButtons(btnFolder, "/images/folder.png","", "Select destination folder", UIStyles.LIGHT_PURPLE, new Color(0, 0, 0, 0), true);
        UIStyles.panelsBorders(pnlVideo, UIStyles.MEDIUM_GREY_COLOR, 15);
        UIStyles.panelsBorders(pnlAudio, UIStyles.MEDIUM_GREY_COLOR, 15);
        UIStyles.styleButtonGroup("Select format", radioM4a, radioMkv, radioMp3, radioMp4, radioWav, radioWebm);
        UIStyles.selectionColorComboBox(cbbxQualityFilter);
        UIStyles.styleButtons(btnDownload,"/images/download2.png", "DOWNLOAD", "Download file", UIStyles.LIGHT_PURPLE, UIStyles.DARK_GREY_COLOR, true);
        UIStyles.styleButtons(btnOpenLast,"/images/play2.png", "OPEN LAST","Reproduce last file",UIStyles.MEDIUM_GREY_COLOR,UIStyles.LIGHT_GREY_COLOR, false);
        UIStyles.styleProgressBar(progressBar);
    }
    
    //Botón visible cuando la descarga ha finalizado
    public void showOpenLastButton() {
        btnOpenLast.setEnabled(true); // Aquí recupera el color sólido automáticamente
        this.repaint();
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
        String[] options = {"mp4", "mkv", "webm", "mp3", "wav", "m4a"};
        for (int i = 0; i < buttons.length; i++) {
            bg.add(buttons[i]);
            buttons[i].setActionCommand(options[i]);
        }
        buttons[0].setSelected(true);
    }
    
    //Configura de JTextField de URL
    private void styleTxtUrl() {
        UIStyles.addIconsTextField(txtUrl, "/images/url.png", "/images/delete_url.png", "Paste the URL of the file to download");

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
    
    //Configuración de pegar URL a JTextField    
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
        btnOpenLast.setEnabled(false);
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
        pnlAudio = new javax.swing.JPanel();
        radioWav = new javax.swing.JRadioButton();
        radioM4a = new javax.swing.JRadioButton();
        radioMp3 = new javax.swing.JRadioButton();

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.png"))); // NOI18N
        add(logoLabel);

        downloadFilePnl.setMaximumSize(null);
        downloadFilePnl.setMinimumSize(new java.awt.Dimension(840, 580));
        downloadFilePnl.setPreferredSize(null);
        downloadFilePnl.setLayout(new java.awt.BorderLayout());

        txtUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtUrl.setMinimumSize(new java.awt.Dimension(70, 23));
        txtUrl.setPreferredSize(new java.awt.Dimension(70, 23));
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

        pnlVideo.setMaximumSize(null);

        radioMp4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp4.setText("MP4");
        pnlVideo.add(radioMp4);

        radioMkv.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMkv.setText("MKV");
        pnlVideo.add(radioMkv);

        radioWebm.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioWebm.setText("WEBM");
        pnlVideo.add(radioWebm);

        cbbxQualityFilter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbbxQualityFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quality" }));
        pnlVideo.add(cbbxQualityFilter);

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

        pnlAudio.setMaximumSize(null);

        radioWav.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioWav.setText("WAV");
        pnlAudio.add(radioWav);

        radioM4a.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioM4a.setText("M4A");
        pnlAudio.add(radioM4a);

        radioMp3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp3.setText("MP3");
        pnlAudio.add(radioMp3);

        downloadFilePnl.add(pnlAudio, java.awt.BorderLayout.CENTER);

        add(downloadFilePnl);
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

        Thread th = new Thread() {
            @Override
            public void run() {
                downloadManager.download(url, folder, format, quality, progressBar, tblModel, folderPaths, Downloads.this);
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
    private javax.swing.JButton btnDownload;
    private javax.swing.JButton btnFolder;
    private javax.swing.JButton btnOpenLast;
    private javax.swing.JComboBox<String> cbbxQualityFilter;
    private javax.swing.JPanel downloadFilePnl;
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