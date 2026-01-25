package montoya.mediabox.panels;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
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
import net.miginfocom.swing.MigLayout;

/**
 * Panel que gestiona las descargas de medios en la aplicación MediaBox. 
 * @author Nerea
 */
public class Downloads extends javax.swing.JPanel {
    
    private MainFrame frame;
    private InfoMedia infoMedia;
    private ApiFiles apiPnl;
    private DownloadManager downloadManager;
    private TypeFilter typeFilter;
    private FileTableModel tblModel;
    private final FileProperties fileProperties;
    private final MediaPollingComponent mediaPollingComponent;
    private final ButtonGroup btnGroup;
    private final Set<String> folderPaths;
    private JList<FolderItem> foldersList;
    private List<FileInformation> allFiles = new ArrayList<>();
    private String folderPath = "";

    public Downloads(MainFrame frame, Set<String> folderPaths, DownloadManager downloadManager, MediaPollingComponent mediaPollingComponent) {

        initComponents();
        configPanel();

        this.frame = frame;
        this.typeFilter = new TypeFilter();
        this.downloadManager = downloadManager;
        this.folderPaths = folderPaths;
        this.fileProperties = new FileProperties();
        this.mediaPollingComponent = mediaPollingComponent;

        foldersList = new JList<>(new DefaultListModel<>());
        btnGroup = new ButtonGroup();
        infoMedia = new InfoMedia(fileProperties, typeFilter, allFiles, folderPaths, mediaPollingComponent);
        tblModel = infoMedia.getTableModel();
        apiPnl = new ApiFiles(infoMedia, mediaPollingComponent, fileProperties);

        qualityOptions(cbbxQualityFilter);//Aplica filtro de calidad
        //configComponents();
        styleComponents(); //Estilo de los componentes
        styleTxtUrl();
        configRadioButtons(btnGroup, radioMp4, radioMkv, radioWebm, radioMp3, radioWav, radioM4a);//Configura como se ven los botones
        
        downloadFilePnl.setBounds(60, 40, 630, 670);

        //Panels InfoMedia y ApiFiles añadidos
        infoMedia.setBounds(780, 40, 630, 400);
        this.add(infoMedia);

        apiPnl.setBounds(780, 500, 630, 100);
        this.add(apiPnl);
    }

    private void configPanel(){
        setBounds(0, 0, 1300, 770);
        setBackground(StyleConfig.DARK_BLUE_COLOR);  
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
    add(apiPnl, "cell 0 7, span 4, growx, h 120!");

    // Logo abajo derecha
    add(logoLabel, "cell 3 8, alignx right, aligny bottom");
}

    //Aplica estilos a los componentes
    private void styleComponents() {
        StyleConfig.createTitleBorder(downloadFilePnl, "DOWNLOAD FILE");
        StyleConfig.styleButton(btnFolder, "/images/folder.png", "Select destination folder");
        StyleConfig.createTitleBorder(pnlVideo, "VIDEO");
        StyleConfig.createTitleBorder(pnlAudio, "AUDIO");
        StyleConfig.styleButtonGroup("Select format", radioM4a, radioMkv, radioMp3, radioMp4, radioWav, radioWebm);
        StyleConfig.renderComboBox(cbbxQualityFilter);
        btnDownload.setBackground(StyleConfig.LIGHT_BLUE_COLOR);
        btnDownload.setForeground(StyleConfig.DARK_BLUE_COLOR);
        btnDownload.setText("DOWNLOAD");
        btnDownload.setPreferredSize(new Dimension(120, 70));
        
        btnOpenLast.setBackground(StyleConfig.GREY_COLOR);
        btnOpenLast.setForeground(StyleConfig.DARK_BLUE_COLOR);
        btnOpenLast.setText("OPEN LAST");
        btnOpenLast.setPreferredSize(new Dimension(120, 50));
        
        progressBar.setBackground(StyleConfig.LIGHT_BLUE_COLOR);
        progressBar.setForeground(StyleConfig.GREY_COLOR);
        
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
        pnlAudio = new javax.swing.JPanel();
        radioMp3 = new javax.swing.JRadioButton();
        radioWav = new javax.swing.JRadioButton();
        radioM4a = new javax.swing.JRadioButton();
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

        setMinimumSize(new java.awt.Dimension(1500, 770));
        setPreferredSize(new java.awt.Dimension(1300, 770));
        setLayout(null);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_2_2.png"))); // NOI18N
        add(logoLabel);
        logoLabel.setBounds(1120, 630, 81, 80);

        downloadFilePnl.setMaximumSize(new java.awt.Dimension(630, 670));
        downloadFilePnl.setMinimumSize(new java.awt.Dimension(630, 670));
        downloadFilePnl.setPreferredSize(new java.awt.Dimension(630, 670));
        downloadFilePnl.setLayout(null);

        txtUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        downloadFilePnl.add(txtUrl);
        txtUrl.setBounds(50, 50, 440, 23);

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
        btnFolder.setBounds(500, 40, 72, 50);

        pnlAudio.setMaximumSize(new java.awt.Dimension(200, 210));

        radioMp3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp3.setText("MP3");

        radioWav.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioWav.setText("WAV");

        radioM4a.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioM4a.setText("M4A");

        javax.swing.GroupLayout pnlAudioLayout = new javax.swing.GroupLayout(pnlAudio);
        pnlAudio.setLayout(pnlAudioLayout);
        pnlAudioLayout.setHorizontalGroup(
            pnlAudioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAudioLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnlAudioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(radioWav, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addComponent(radioM4a, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(radioMp3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        pnlAudioLayout.setVerticalGroup(
            pnlAudioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAudioLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(radioMp3)
                .addGap(18, 18, 18)
                .addComponent(radioWav)
                .addGap(18, 18, 18)
                .addComponent(radioM4a)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        downloadFilePnl.add(pnlAudio);
        pnlAudio.setBounds(260, 100, 180, 210);

        pnlVideo.setMaximumSize(new java.awt.Dimension(200, 210));

        radioMp4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp4.setText("MP4");

        radioMkv.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMkv.setText("MKV");

        radioWebm.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioWebm.setText("WEBM");

        cbbxQualityFilter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbbxQualityFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quality" }));

        javax.swing.GroupLayout pnlVideoLayout = new javax.swing.GroupLayout(pnlVideo);
        pnlVideo.setLayout(pnlVideoLayout);
        pnlVideoLayout.setHorizontalGroup(
            pnlVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVideoLayout.createSequentialGroup()
                .addGroup(pnlVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVideoLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(pnlVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioWebm, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(radioMkv, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(radioMp4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlVideoLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(cbbxQualityFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        pnlVideoLayout.setVerticalGroup(
            pnlVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVideoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(radioMp4)
                .addGap(18, 18, 18)
                .addComponent(radioMkv)
                .addGap(18, 18, 18)
                .addComponent(radioWebm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(cbbxQualityFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        downloadFilePnl.add(pnlVideo);
        pnlVideo.setBounds(50, 100, 180, 210);

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
        btnDownload.setBounds(460, 210, 120, 60);

        btnOpenLast.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnOpenLast.setText("Open Last");
        btnOpenLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenLastActionPerformed(evt);
            }
        });
        downloadFilePnl.add(btnOpenLast);
        btnOpenLast.setBounds(460, 280, 120, 24);

        progressBar.setBackground(new java.awt.Color(204, 204, 204));
        progressBar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        downloadFilePnl.add(progressBar);
        progressBar.setBounds(80, 480, 450, 10);

        areaInfo.setColumns(20);
        areaInfo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        areaInfo.setRows(5);
        jScrollPane1.setViewportView(areaInfo);

        downloadFilePnl.add(jScrollPane1);
        jScrollPane1.setBounds(80, 500, 450, 140);

        add(downloadFilePnl);
        downloadFilePnl.setBounds(30, 30, 630, 670);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolderActionPerformed
        JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        directory.setDialogTitle("Select Download Destination");

        int result = directory.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = directory.getSelectedFile();

            if (selectedFolder != null && selectedFolder.isDirectory()) {
                //String folderPath = selectedFolder.getAbsolutePath();
                //txtFolder.setText(selectedFolder.getAbsolutePath());
                
                this.folderPath = selectedFolder.getAbsolutePath();

                // Añadir el directorio a la lista si no está ya
                if (folderPaths.add(folderPath)) {
                    DefaultListModel<FolderItem> listModel = (DefaultListModel<FolderItem>) infoMedia.getListDirectories().getModel();
                    listModel.addElement(new FolderItem(folderPath, false, false));
                }
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

        downloadManager.setTempPath(folder);
        areaInfo.setText("");

        Thread th = new Thread() {
            @Override
            public void run() {
                downloadManager.download(url, folder, format, quality, areaInfo, progressBar, tblModel, foldersList, folderPaths);

                if (folderPaths.add(folder)) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            tblModel.fireTableDataChanged();
                            DefaultListModel<FolderItem> listModel = (DefaultListModel<FolderItem>) foldersList.getModel();
                            listModel.addElement(new FolderItem(folder, false, false));
                        }
                    });
                }
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
