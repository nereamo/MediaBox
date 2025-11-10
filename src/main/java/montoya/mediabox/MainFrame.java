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
    private final Set<String> downloadDirectories = new HashSet<>();
    private FileTableModel tblModel;
    private final FileProperties fp;
    private final MainViewController mvc;
    private ButtonGroup bg;

    public MainFrame() {
        initComponents();
        fp = new FileProperties();
        bg = new ButtonGroup();
        downloader = new DownloadManager(fp);
        preferences = new Preferences(this, downloader);
        mvc = new MainViewController(this, mainPanel, preferences, barProgress);

        preferences.setMainController(mvc);

        //Métodos de clase MainViewController
        mvc.configFrame();
        mvc.configPreferencesPanel();
        
        radioButtons();

        //Carga datos guardados en archivo .json
        DirectoryInformation data = fp.cargarDatos();
        fileList = data.downloads;
        downloadDirectories.addAll(data.downloadFolders);

        //AbstractTableMode
        tblModel = new FileTableModel(fileList);
        tblInfo.setModel(tblModel);

        //Lista de objeto 'descarga'
        DefaultListModel listModel = new DefaultListModel();
        for (String folder : downloadDirectories) {
            listModel.addElement(new FolderItem(folder));
        }
        listDirectories.setModel(listModel);
        
        //Muestra las descargas pertenecientes a un directorio
        mvc.configDownloadList(listDirectories, cbbxFilter, fileList, tblInfo);
        
        mvc.applyFilters(cbbxFilter);
    }
    
    //Añadir los radioButtons a ButtonGroup
    private void radioButtons() {
        bg.add(radioMp4);
        bg.add(radioMkv);
        bg.add(radioWebm);
        bg.add(radioMp3);
        bg.add(radioWav);
        bg.add(radioM4a);

        radioMp4.setActionCommand("mp4");
        radioMkv.setActionCommand("mkv");
        radioWebm.setActionCommand("webm");
        radioMp3.setActionCommand("mp3");
        radioWav.setActionCommand("wav");
        radioM4a.setActionCommand("m4a");

        radioMp4.setSelected(true); //Dejar seleccionado por defecto MP4
    }

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
        cbbxFilter = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        listDirectories = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblInfo = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        radioWav = new javax.swing.JRadioButton();
        radioM4a = new javax.swing.JRadioButton();
        radioMkv = new javax.swing.JRadioButton();
        radioWebm = new javax.swing.JRadioButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
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
        mainPanel.setMinimumSize(new java.awt.Dimension(1200, 800));
        mainPanel.setPreferredSize(new java.awt.Dimension(1200, 770));
        mainPanel.setLayout(null);

        lblUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblUrl.setText("URL:");
        mainPanel.add(lblUrl);
        lblUrl.setBounds(50, 20, 37, 20);

        txtUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mainPanel.add(txtUrl);
        txtUrl.setBounds(150, 50, 330, 23);

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
        btnClear.setBounds(490, 50, 100, 24);

        lblFolder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblFolder.setText("Folder:");
        mainPanel.add(lblFolder);
        lblFolder.setBounds(700, 20, 50, 20);

        txtFolder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mainPanel.add(txtFolder);
        txtFolder.setBounds(800, 50, 330, 23);

        btnBrowse.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        mainPanel.add(btnBrowse);
        btnBrowse.setBounds(700, 50, 90, 24);

        lblFormat.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblFormat.setText("Video");
        mainPanel.add(lblFormat);
        lblFormat.setBounds(50, 140, 70, 17);

        radioMp4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp4.setText("MP4");
        mainPanel.add(radioMp4);
        radioMp4.setBounds(50, 170, 110, 22);

        radioMp3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp3.setText("MP3 (Audio)");
        mainPanel.add(radioMp3);
        radioMp3.setBounds(380, 170, 110, 22);

        btnDownload.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnDownload.setText("Download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });
        mainPanel.add(btnDownload);
        btnDownload.setBounds(90, 410, 120, 24);

        btnOpenLast.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnOpenLast.setText("Open Last");
        btnOpenLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenLastActionPerformed(evt);
            }
        });
        mainPanel.add(btnOpenLast);
        btnOpenLast.setBounds(230, 410, 120, 24);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/largelogoSmall3.png"))); // NOI18N
        mainPanel.add(logoLabel);
        logoLabel.setBounds(1000, 670, 180, 50);

        areaInfo.setColumns(20);
        areaInfo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        areaInfo.setRows(5);
        jScrollPane1.setViewportView(areaInfo);

        mainPanel.add(jScrollPane1);
        jScrollPane1.setBounds(50, 490, 340, 170);

        barProgress.setBackground(new java.awt.Color(204, 204, 204));
        barProgress.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        barProgress.setForeground(new java.awt.Color(255, 153, 51));
        mainPanel.add(barProgress);
        barProgress.setBounds(50, 450, 340, 20);

        cbbxFilter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbbxFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filter" }));
        cbbxFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbxFilterActionPerformed(evt);
            }
        });
        mainPanel.add(cbbxFilter);
        cbbxFilter.setBounds(800, 390, 230, 23);

        listDirectories.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listDirectories.setPreferredSize(new java.awt.Dimension(40, 60));
        jScrollPane2.setViewportView(listDirectories);

        mainPanel.add(jScrollPane2);
        jScrollPane2.setBounds(570, 420, 120, 240);

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
        jScrollPane3.setBounds(690, 420, 450, 240);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Directories");
        mainPanel.add(jLabel1);
        jLabel1.setBounds(570, 400, 70, 17);

        btnDelete.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        mainPanel.add(btnDelete);
        btnDelete.setBounds(1050, 390, 90, 24);

        radioWav.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioWav.setText("WAV (Audio)");
        mainPanel.add(radioWav);
        radioWav.setBounds(380, 200, 110, 22);

        radioM4a.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioM4a.setText("M4A (Audio)");
        mainPanel.add(radioM4a);
        radioM4a.setBounds(380, 230, 110, 22);

        radioMkv.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMkv.setText("MKV");
        mainPanel.add(radioMkv);
        radioMkv.setBounds(50, 200, 110, 22);

        radioWebm.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioWebm.setText("WEBM");
        mainPanel.add(radioWebm);
        radioWebm.setBounds(50, 230, 130, 22);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quality" }));
        mainPanel.add(jComboBox1);
        jComboBox1.setBounds(50, 270, 180, 30);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Audio");
        mainPanel.add(jLabel2);
        jLabel2.setBounds(380, 140, 70, 17);

        getContentPane().add(mainPanel);
        mainPanel.setBounds(0, 0, 1200, 770);

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
                if (downloadDirectories.add(folderPath)) {
                    DefaultListModel listModel = (DefaultListModel) listDirectories.getModel();
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
        String format = bg.getSelection().getActionCommand();

        downloader.setTempPath(folder);
        areaInfo.setText("");

        Thread th = new Thread() {
            @Override
            public void run() {
                downloader.download(url, folder, format, areaInfo, barProgress, tblModel, listDirectories, downloadDirectories);

                if (downloadDirectories.add(folder)) {
                    DefaultListModel listModel = (DefaultListModel) listDirectories.getModel();
                    listModel.addElement(new FolderItem(folder));
                }
                fp.guardarDatos(new DirectoryInformation(fileList, downloadDirectories));
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

    //Elimina archivo seleccionado en la JTable
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int row = tblInfo.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a file to delete.");
            return;
        }

        FileInformation info = tblModel.getFileAt(row);

        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to delete this file? - " + info.name, "Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            //Borra archivo fisico y de interfaz
            mvc.deleteDownload(info, fileList, downloadDirectories, fp);

            //Actualiza tabla
            Object selected = listDirectories.getSelectedValue();
            if (selected instanceof FolderItem folder) {
                String filtro = (String) cbbxFilter.getSelectedItem(); //obtener filtro actual
                mvc.showFilteredDownloads(folder.getFullPath(), filtro, fileList, tblInfo);
            } else {
                tblInfo.setModel(new FileTableModel(fileList));
            }

            //Refresca directorios de lista
            DefaultListModel<FolderItem> newModel = new DefaultListModel<>();
            for (String folderPath : downloadDirectories) {
                newModel.addElement(new FolderItem(folderPath));
            }
            listDirectories.setModel(newModel);

            fp.guardarDatos(new DirectoryInformation(fileList, downloadDirectories));
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    //Permite filtra por type los elementos de una tabla
    private void cbbxFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxFilterActionPerformed
        Object selected = listDirectories.getSelectedValue(); //Obtiene objeto seleccionado de JList
        
        if (selected instanceof FolderItem) {
            String folderPath = ((FolderItem) selected).getFullPath(); //Obtiene ruta
            String filtro = (String) cbbxFilter.getSelectedItem(); //Lee filtro
            mvc.showFilteredDownloads(folderPath, filtro, fileList, tblInfo);
        }
    }//GEN-LAST:event_cbbxFilterActionPerformed

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
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDownload;
    private javax.swing.JButton btnOpenLast;
    private javax.swing.JButton btnPaste;
    private javax.swing.JComboBox<String> cbbxFilter;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblFolder;
    private javax.swing.JLabel lblFormat;
    private javax.swing.JLabel lblUrl;
    private javax.swing.JList<FolderItem> listDirectories;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem mnuAbout;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuItem mnuPreferences;
    private javax.swing.JRadioButton radioM4a;
    private javax.swing.JRadioButton radioMkv;
    private javax.swing.JRadioButton radioMp3;
    private javax.swing.JRadioButton radioMp4;
    private javax.swing.JRadioButton radioWav;
    private javax.swing.JRadioButton radioWebm;
    private javax.swing.JTable tblInfo;
    private javax.swing.JTextField txtFolder;
    private javax.swing.JTextField txtUrl;
    // End of variables declaration//GEN-END:variables
}
