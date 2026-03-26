 package montoya.mediabox.panels;

import montoya.mediaBox.utils.Logger;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
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
 * <p> Permite:
 * <ul>
 * <li> Pegar la URL del archivo a descargar </li>
 * <li> Elegir el directorio de destiono </li>
 * <li> Seleccionar el formato y la calidad </li>
 * <li> Reproducir el último archivo descargado </li>
 * <li> Visualizar las descargas </li>
 * </ul>
 * 
 * Se usa junto con {@link InfoMedia} para visualizar los directorios y archivos descargados.
 * 
 * @author Nerea
 */
public class Downloads extends javax.swing.JPanel {
    
    /** {@link MainFrame} Ventana principal donde se muestra el panel */
    private MainFrame frame;
    
    /** {@link FileProperties} Panel (vista) que muestra la información d elas descargas. */
    public InfoMedia infoMedia;
    
    /** {@link DownloadManager} Gestor de descarga */
    private DownloadManager downloadManager;
    
    /** {@link TypeFilter} Permite aplicar el filtro por tipo de archivo y por directorio. */
    private TypeFilter typeFilter;
    
    /** {@link FileTableModel} Modelo de tabla que muestra los archivos. */
    private FileTableModel tblModel;
    
    /** {@link FileProperties} Gestiona las propiedades de los archivos descargados. */
    private final FileProperties fileProperties;
    
    /** {@link MediaPollingComponent} Listener que notifica nuevos medios en la API. */
    private final MediaPollingComponent mediaPollingComponent;
    
    /** Grupo de botones para seleccionar formato */
    private final ButtonGroup btnGroup;
    
    /** Rutas de los directorios que almacenan descargas */
    private final Set<String> folderPaths;
    
    /** Lista de archivos descargados en un directorio */
    private Set<FileInformation> allFiles = new HashSet<>();
    
    /** Ruta completa del directorio */
    private String folderPath = "";

    /**
     * Constructor que inicializa el panel Downloads
     * 
     * @param frame Ventana principal donde se añade el panel
     * @param folderPaths Todas la rutas donde se realizan las descargas
     * @param downloadManager Gestor de descargas
     * @param mediaPollingComponent Listener que notifica nuevos medios en la API
     */
    public Downloads(MainFrame frame, Set<String> folderPaths, DownloadManager downloadManager, MediaPollingComponent mediaPollingComponent) {
       
        initComponents();

        this.frame = frame;
        this.typeFilter = new TypeFilter();
        this.downloadManager = downloadManager;
        this.folderPaths = folderPaths;
        this.fileProperties = new FileProperties();
        this.mediaPollingComponent = mediaPollingComponent;

        btnGroup = new ButtonGroup(); //Inicializa el grupo de radio buttons
        infoMedia = new InfoMedia(fileProperties, typeFilter, allFiles, folderPaths, mediaPollingComponent); //Inicializa el panel InfoMedia
        downloadManager.setInfoMedia(infoMedia);
        tblModel = infoMedia.getTableModel();

        setupLayout();
        applyStylesComponent();
        configRadioButtons(btnGroup, radioMp4, radioMkv, radioWebm, radioMp3, radioWav, radioM4a);
        
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * Configura posición de los componentes
     */
    private void setupLayout() {
        //Panel principal, panel interno, panel InfoMedia y logo
        this.setLayout(new MigLayout("fill, insets 20, wrap 1", "[grow, center]", "push[grow]10:50:push[grow]push"));
        this.add(downloadFilePnl, "grow, center, w 50:840:n, h 280:500:n, gaptop 5:40:push");
        this.add(infoMedia, "grow, center, w 50:840:n, h 50:430:n");
        this.add(logoLabel, "align center, gapbottom 10:40:push");

        //Panel interno
        downloadFilePnl.setLayout(new MigLayout("fillx, insets 10 20 10 20, wrap 8",
                "[grow][grow][grow][grow][grow][grow][grow][grow]",
                "10:30:n[]10:50:n[]15![]20:40:push[]20:40:push[]10!"
        ));
        
       
        downloadFilePnl.add(txtUrl, "span 8, split 2, growx, w 100:n:n, h 30:35:n, gapright 25, aligny center");
        downloadFilePnl.add(btnFolder, "right, w 50!, h 40:50:n");

        //Panel video
        pnlVideo.setLayout(new MigLayout("fill, insets 5", "[grow, center][grow, center][grow, center][grow, center]", "[]"));
        pnlVideo.add(radioMp4, "w 0:n:n");
        pnlVideo.add(radioMkv, "w 0:n:n");
        pnlVideo.add(radioWebm, "w 0:n:n");
        pnlVideo.add(cbbxQualityFilter, "w 40:70:150, h 20:35:n, growx, pushx");
        downloadFilePnl.add(pnlVideo, "span 8, growx, h 30:60:n");

        //Panel audio
        pnlAudio.setLayout(new MigLayout("fill, insets 5 17 5 17",
                "[grow, center][grow, center][grow, center]",
                "[center]"
        ));
        pnlAudio.add(radioMp3, "w 0:n:n");
        pnlAudio.add(radioWav, "w 0:n:n");
        pnlAudio.add(radioM4a, "w 0:n:n");
        downloadFilePnl.add(pnlAudio, "span 8, growx, h 30:60:n");

        // Botones centrados
        downloadFilePnl.add(btnDownload, "span 8, split 2, align center, sg, w 50:180:n, h 25:45:n");
        downloadFilePnl.add(btnOpenLast, "sg, w 50:180:n, h 25:45:n, gapleft 10");

        // ProgressBar
        downloadFilePnl.add(progressBar, "span 8, growx, h 22!");
    }

    /** Configura el estilo de los componentes */
    private void applyStylesComponent() {
        //Panel principal y panel interno
        this.setBackground(UIStyles.BLACK_COLOR); //Color del panel
        UIStyles.panelsBorders(downloadFilePnl, UIStyles.DARK_GREY_COLOR, 30);
        
        //Campo de texto URL
        UIStyles.styleField(txtUrl, "/images/url.png", " Paste the URL of the file to download", "/images/delete_url.png", null, true);
        
        //Panel video y audio
        UIStyles.panelsBorders(pnlVideo, UIStyles.MEDIUM_GREY_COLOR, 15);
        UIStyles.panelsBorders(pnlAudio, UIStyles.MEDIUM_GREY_COLOR, 15);
        UIStyles.styleButtonGroup("Select format", radioM4a, radioMkv, radioMp3, radioMp4, radioWav, radioWebm);
        UIStyles.itemsInCombobox(cbbxQualityFilter, "1080,720,480");
        
        //Botón folder, download y open last
        UIStyles.styleButtons(btnFolder, "", "/images/folder.png", UIStyles.LIGHT_PURPLE, new Color(0, 0, 0, 0), true, "Select destination folder", null);
        UIStyles.styleButtons(btnDownload, "DOWNLOAD", "/images/download2.png", UIStyles.LIGHT_PURPLE, UIStyles.DARK_GREY_COLOR, true, "Download file", null);
        UIStyles.styleButtons(btnOpenLast, "OPEN LAST", "/images/play2.png",UIStyles.MEDIUM_GREY_COLOR,UIStyles.LIGHT_GREY_COLOR, false, "Reproduce last file", "/images/play2_black.png");

        //Barra de progreso
        UIStyles.styleProgressBar(progressBar);
    }
    
    /** Visibilidad del botón "Open last". */
    public void showOpenLastButton() {
        btnOpenLast.setEnabled(true); //Habilitar botón
        this.repaint();
    }
    
    /** Configuración de radio Buttons. */
    private void configRadioButtons(ButtonGroup bg, JRadioButton... buttons) {
        for (JRadioButton btn : buttons) {
            bg.add(btn);
            btn.setActionCommand(btn.getText().toLowerCase());
        }
        buttons[0].setSelected(true);
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

        downloadFilePnl.setMinimumSize(new java.awt.Dimension(840, 580));
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
        cbbxQualityFilter.setMaximumSize(null);
        cbbxQualityFilter.setMinimumSize(null);
        cbbxQualityFilter.setPreferredSize(null);
        pnlVideo.add(cbbxQualityFilter);

        downloadFilePnl.add(pnlVideo, java.awt.BorderLayout.PAGE_END);

        btnDownload.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnDownload.setMaximumSize(null);
        btnDownload.setMinimumSize(null);
        btnDownload.setPreferredSize(null);
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });
        downloadFilePnl.add(btnDownload, java.awt.BorderLayout.LINE_END);

        btnOpenLast.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnOpenLast.setText("Open Last");
        btnOpenLast.setMaximumSize(null);
        btnOpenLast.setMinimumSize(null);
        btnOpenLast.setPreferredSize(null);
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

    /**
     * Muestra un dialogo para seleccionar la crpeta de destino de la descarga.
     * Si el directorios es válido, lo añade a la lista de directorios.
     * 
     * @param evt Evento generado al pulsar el botón "Folder"
     */
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
                for (FolderItem item : Collections.list(listModel.elements())) {
                    if (item.getFullPath().equals(folderPath)) {
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

    /**
     * Incia la descarga si todos los datos parametros necesários han sido establecidos. 
     * La descarga se inicia en un hilo secundario para no bloquear la interfaz.
     * 
     * @param evt Evento generado al pulsar el botón "Download"
     */
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
                downloadManager.download(url, folder, format, quality, progressBar, tblModel, Downloads.this);
            }
        };
        th.start();
    }//GEN-LAST:event_btnDownloadActionPerformed

    /**
     * Reproduce el último archivo descargado.
     * Solo está habilitado cuando la descarga finalizó correctamente.
     * 
     * @param evt Evento generado al pulsar el botón "Open Last"
     */
    private void btnOpenLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenLastActionPerformed
        File lastFile = downloadManager.getLastDownloadedFile();
        if (lastFile != null && lastFile.exists()) {
            try {
                Desktop.getDesktop().open(lastFile);
            } catch (IOException ex) {
                Logger.logError("Could not open last downloaded file: " + lastFile.getAbsolutePath(), ex);
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