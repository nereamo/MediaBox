package montoya.mediabox;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.logging.Logger;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.dialogs.DialogAbout;
import montoya.mediabox.controller.MainViewController;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.DirectoryInformation;
import montoya.mediabox.fileInformation.FolderItem;

/**
 * Class principal
 *
 * @author Nerea
 */
public class MainFrame extends JFrame {

    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());
    private DownloadManager downloader;
    private Preferences preferences;
    List<FileInformation> fileList = new ArrayList<>();
    private final Set<String> directoriosDescarga = new HashSet<>();
    private FileTableModel model;
    private FileProperties fp;
    private MainViewController mvc;

    public MainFrame() {
        initComponents();
        fp = new FileProperties();
        downloader = new DownloadManager(fp);
        preferences = new Preferences(this, downloader);
        mvc = new MainViewController(this, mainPanel, preferences, radioMp4, radioMp3, barProgress);

        preferences.setMainController(mvc);

        //Métodos de clase MainViewController
        mvc.configFrame();
        mvc.configPreferencesPanel();
        mvc.configRadioButtons();

        DirectoryInformation data = fp.cargarDatos();
        fileList = data.downloads;
        directoriosDescarga.addAll(data.downloadFolders);

        //AbstractTableMode
        model = new FileTableModel(fileList);
        tblInfo.setModel(model);

        //Lista de objeto 'descarga'
        DefaultListModel listModel = new DefaultListModel(); // sin <FolderItem>
        for (String folder : directoriosDescarga) {
            listModel.addElement(new FolderItem(folder)); // guardamos FolderItem igual
        }
        lstDownloads.setModel(listModel);

        lstDownloads.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Object selected = lstDownloads.getSelectedValue();
                if (selected instanceof FolderItem) {
                    FolderItem folder = (FolderItem) selected;
                    mvc.mostrarDescargasPorDirectorio(folder.getFullPath(), fileList, tblInfo);
                }
            }
        });

    }

//    private void mostrarDescargasPorDirectorio(String folderPath) {
//        List<FileInformation> filtrados = new ArrayList<>();
//
//        for (FileInformation info : fileList) {
//            if (info.folderPath.equals(folderPath)) {
//                filtrados.add(info);
//            }
//        }
//
//        tblInfo.setModel(new FileTableModel(filtrados));
//    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        mainPanel = new javax.swing.JPanel();
        lblUrl = new javax.swing.JLabel();
        txtUrl = new javax.swing.JTextField();
        btnPaste = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblFolder = new javax.swing.JLabel();
        txtFolder = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        lblFormat = new javax.swing.JLabel();
        radioMp4 = new javax.swing.JRadioButton();
        radioMp3 = new javax.swing.JRadioButton();
        btnDownload = new javax.swing.JButton();
        btnOpenLast = new javax.swing.JButton();
        logoLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaInfo = new javax.swing.JTextArea();
        barProgress = new javax.swing.JProgressBar();
        cbxFilter = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstDownloads = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblInfo = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuExit = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();
        mnuPreferences = new javax.swing.JMenuItem();
        mnuHelp = new javax.swing.JMenu();
        mnuAbout = new javax.swing.JMenuItem();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("mainFrame"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(null);

        mainPanel.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        mainPanel.setMinimumSize(new java.awt.Dimension(1100, 700));
        mainPanel.setPreferredSize(new java.awt.Dimension(1100, 670));
        mainPanel.setLayout(null);

        lblUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblUrl.setText("URL:");
        mainPanel.add(lblUrl);
        lblUrl.setBounds(50, 20, 37, 20);

        txtUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mainPanel.add(txtUrl);
        txtUrl.setBounds(150, 50, 250, 23);

        btnPaste.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnPaste.setText("Paste");
        btnPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasteActionPerformed(evt);
            }
        });
        mainPanel.add(btnPaste);
        btnPaste.setBounds(50, 50, 90, 24);

        btnClear.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        mainPanel.add(btnClear);
        btnClear.setBounds(410, 50, 100, 24);

        lblFolder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblFolder.setText("Folder:");
        mainPanel.add(lblFolder);
        lblFolder.setBounds(50, 110, 50, 20);

        txtFolder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mainPanel.add(txtFolder);
        txtFolder.setBounds(150, 140, 250, 23);

        btnBrowse.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        mainPanel.add(btnBrowse);
        btnBrowse.setBounds(50, 140, 90, 24);

        lblFormat.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblFormat.setText("Format: ");
        mainPanel.add(lblFormat);
        lblFormat.setBounds(50, 200, 53, 17);

        radioMp4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp4.setText("MP4 (Video)");
        mainPanel.add(radioMp4);
        radioMp4.setBounds(50, 230, 110, 22);

        radioMp3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp3.setText("MP3 (Audio)");
        mainPanel.add(radioMp3);
        radioMp3.setBounds(180, 230, 110, 22);

        btnDownload.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnDownload.setText("Download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });
        mainPanel.add(btnDownload);
        btnDownload.setBounds(50, 300, 120, 24);

        btnOpenLast.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnOpenLast.setText("Open Last");
        btnOpenLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenLastActionPerformed(evt);
            }
        });
        mainPanel.add(btnOpenLast);
        btnOpenLast.setBounds(190, 300, 120, 24);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/largelogoSmall3.png"))); // NOI18N
        mainPanel.add(logoLabel);
        logoLabel.setBounds(900, 570, 180, 50);

        areaInfo.setColumns(20);
        areaInfo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        areaInfo.setRows(5);
        jScrollPane1.setViewportView(areaInfo);

        mainPanel.add(jScrollPane1);
        jScrollPane1.setBounds(50, 390, 340, 170);

        barProgress.setBackground(new java.awt.Color(204, 204, 204));
        barProgress.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        barProgress.setForeground(new java.awt.Color(255, 153, 51));
        mainPanel.add(barProgress);
        barProgress.setBounds(50, 360, 340, 20);

        cbxFilter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbxFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filter" }));
        mainPanel.add(cbxFilter);
        cbxFilter.setBounds(820, 280, 230, 23);

        lstDownloads.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lstDownloads.setPreferredSize(new java.awt.Dimension(40, 60));
        jScrollPane2.setViewportView(lstDownloads);

        mainPanel.add(jScrollPane2);
        jScrollPane2.setBounds(550, 320, 140, 240);

        tblInfo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Size", "Type", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblInfo);

        mainPanel.add(jScrollPane3);
        jScrollPane3.setBounds(690, 320, 360, 240);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Directories");
        mainPanel.add(jLabel1);
        jLabel1.setBounds(550, 290, 70, 17);

        getContentPane().add(mainPanel);
        mainPanel.setBounds(0, 0, 1100, 670);

        menuBar.setBackground(new java.awt.Color(255, 102, 0));
        menuBar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 102, 0)));
        menuBar.setToolTipText("");
        menuBar.setMinimumSize(new java.awt.Dimension(70, 35));
        menuBar.setPreferredSize(new java.awt.Dimension(70, 35));

        mnuFile.setBackground(new java.awt.Color(255, 102, 0));
        mnuFile.setBorder(null);
        mnuFile.setForeground(new java.awt.Color(255, 255, 255));
        mnuFile.setText("File");
        mnuFile.setToolTipText("File");
        mnuFile.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        mnuFile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mnuFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mnuFile.setPreferredSize(new java.awt.Dimension(40, 40));

        mnuExit.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mnuExit.setText("Exit");
        mnuExit.setToolTipText("Exit");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        mnuFile.add(mnuExit);

        menuBar.add(mnuFile);

        mnuEdit.setBorder(null);
        mnuEdit.setForeground(new java.awt.Color(255, 255, 255));
        mnuEdit.setText("Edit");
        mnuEdit.setToolTipText("Edit");
        mnuEdit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        mnuEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mnuEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mnuEdit.setMinimumSize(new java.awt.Dimension(40, 40));
        mnuEdit.setPreferredSize(new java.awt.Dimension(40, 40));

        mnuPreferences.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mnuPreferences.setText("Preferences");
        mnuPreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPreferencesActionPerformed(evt);
            }
        });
        mnuEdit.add(mnuPreferences);

        menuBar.add(mnuEdit);

        mnuHelp.setBorder(null);
        mnuHelp.setForeground(new java.awt.Color(255, 255, 255));
        mnuHelp.setText("Help");
        mnuHelp.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        mnuHelp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mnuHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mnuHelp.setMinimumSize(new java.awt.Dimension(40, 40));
        mnuHelp.setPreferredSize(new java.awt.Dimension(40, 40));

        mnuAbout.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mnuAbout.setText("About");
        mnuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAboutActionPerformed(evt);
            }
        });
        mnuHelp.add(mnuAbout);

        menuBar.add(mnuHelp);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Preferences
    private void mnuPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPreferencesActionPerformed
        mvc.showPreferencesPanel();
    }//GEN-LAST:event_mnuPreferencesActionPerformed

    //About
    private void mnuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAboutActionPerformed
        DialogAbout dialogAbout = new DialogAbout(this, true);
        dialogAbout.setVisible(true);
    }//GEN-LAST:event_mnuAboutActionPerformed

    //Exit
    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Do you want to exit the application?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_mnuExitActionPerformed

    //Directorio para guardar archivo descargado
    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = directory.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = directory.getSelectedFile();

            if (selectedFolder != null && selectedFolder.isDirectory()) {
                String folderPath = selectedFolder.getAbsolutePath();
                txtFolder.setText(selectedFolder.getAbsolutePath());

                // Añadir el directorio a la JList si no está ya
                if (directoriosDescarga.add(folderPath)) {
                    DefaultListModel listModel = (DefaultListModel) lstDownloads.getModel();
                    listModel.addElement(new FolderItem(folderPath));
                }
            }
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    //Copia del portapapeles a JTextField "urlField"
    private void btnPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasteActionPerformed
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor df = DataFlavor.stringFlavor;
        if (cb.isDataFlavorAvailable(df)) {
            try {
                String clipboardContent = (String) cb.getData(df);
                txtUrl.setText(clipboardContent);
            } catch (UnsupportedFlavorException ex) {
                System.getLogger(MainFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (IOException ex) {
                System.getLogger(MainFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }//GEN-LAST:event_btnPasteActionPerformed

    //Ejecuta la descarga
    private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadActionPerformed
        //new Thread(() -> downloader.download(url, folder, format, areaInfo, barProgress)).start(); --> Expresion lambda

        String url = txtUrl.getText().trim();
        String folder = txtFolder.getText().trim();
        String format = radioMp3.isSelected() ? "mp3" : "mp4";

        downloader.setTempPath(folder);
        areaInfo.setText("");

        Thread th = new Thread() {
            @Override
            public void run() {
                downloader.download(url, folder, format, areaInfo, barProgress, model, lstDownloads, directoriosDescarga);

                if (directoriosDescarga.add(folder)) {
                    DefaultListModel listModel = (DefaultListModel) lstDownloads.getModel();
                    listModel.addElement(new FolderItem(folder));
                }
                fp.guardarDatos(new DirectoryInformation(fileList, directoriosDescarga));
            }
        };
        th.start();
    }//GEN-LAST:event_btnDownloadActionPerformed

    //Borra contenido de JTextField "urlField"
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtUrl.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    //Abre el ultimo archivo descargado
    private void btnOpenLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenLastActionPerformed

        File lastFile = downloader.getLastDownloadedFile();
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

    public static void main(String args[]) {
        /* Set the Metal look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaInfo;
    private javax.swing.JProgressBar barProgress;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDownload;
    private javax.swing.JButton btnOpenLast;
    private javax.swing.JButton btnPaste;
    private javax.swing.JComboBox<String> cbxFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblFolder;
    private javax.swing.JLabel lblFormat;
    private javax.swing.JLabel lblUrl;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JList<String> lstDownloads;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem mnuAbout;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuItem mnuPreferences;
    private javax.swing.JRadioButton radioMp3;
    private javax.swing.JRadioButton radioMp4;
    private javax.swing.JTable tblInfo;
    private javax.swing.JTextField txtFolder;
    private javax.swing.JTextField txtUrl;
    // End of variables declaration//GEN-END:variables
}
