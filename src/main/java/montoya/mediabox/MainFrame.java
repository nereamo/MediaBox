package montoya.mediabox;

import montoya.mediabox.download.Downloader;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import montoya.mediabox.JDialogs.JDialogAbout;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Nerea
 */
public class MainFrame extends JFrame {
    
    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());
    private Downloader downloader;
    private Preferences preferences;
   
    public MainFrame() {
        initComponents();
        downloader = new Downloader();
        framePanel();
        preferencesPanel();
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(radioMp4);
        bg.add(radioMp3);
        
        radioMp4.setSelected(true);
    }
    
    //Propiedades de JFrame
    private void framePanel(){
        setTitle("MediaBox");
        setResizable(false);
        setSize(900,700);
        setLocationRelativeTo(this);
        setLayout(null);
        
        mainPanel.setSize(900,700);
        getContentPane().add(mainPanel);
    }
    
    //Propiedades de JPanel Preferences
    private void preferencesPanel(){
        preferences = new Preferences(this, downloader);
        preferences.setBounds(0, 0, 900, 700);
        preferences.setVisible(false);
        getContentPane().add(preferences);
    }
    
    //Mostrar el JPanel principal del JFrame
    public void showMainPanel() {
        preferences.setVisible(false);
        mainPanel.setVisible(true);
    }
    
    //Mostrar el JPanel Preferences 
    public void showPreferencesPanel() {
        mainPanel.setVisible(false);
        preferences.setVisible(true);
    }
    
    public void updateProgress(int value) {
        //SwingUtilities.invokeLater(() -> barProgress.setValue(value)); --> lambda
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                barProgress.setValue(value);
            }
        });
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        menuBar = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuExit = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();
        mnuPreferences = new javax.swing.JMenuItem();
        mnuHelp = new javax.swing.JMenu();
        mnuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("mainFrame"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(null);

        mainPanel.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        mainPanel.setMinimumSize(new java.awt.Dimension(900, 700));
        mainPanel.setPreferredSize(new java.awt.Dimension(900, 670));
        mainPanel.setLayout(null);

        lblUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblUrl.setText("URL:");
        mainPanel.add(lblUrl);
        lblUrl.setBounds(60, 30, 37, 20);

        txtUrl.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mainPanel.add(txtUrl);
        txtUrl.setBounds(160, 60, 280, 23);

        btnPaste.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnPaste.setText("Paste");
        btnPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasteActionPerformed(evt);
            }
        });
        mainPanel.add(btnPaste);
        btnPaste.setBounds(60, 60, 90, 24);

        btnClear.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        mainPanel.add(btnClear);
        btnClear.setBounds(450, 60, 100, 24);

        lblFolder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblFolder.setText("Folder:");
        mainPanel.add(lblFolder);
        lblFolder.setBounds(60, 120, 50, 20);

        txtFolder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mainPanel.add(txtFolder);
        txtFolder.setBounds(160, 150, 280, 23);

        btnBrowse.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        mainPanel.add(btnBrowse);
        btnBrowse.setBounds(60, 150, 90, 24);

        lblFormat.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblFormat.setText("Format: ");
        mainPanel.add(lblFormat);
        lblFormat.setBounds(60, 210, 53, 17);

        radioMp4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp4.setText("MP4");
        mainPanel.add(radioMp4);
        radioMp4.setBounds(60, 240, 60, 22);

        radioMp3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        radioMp3.setText("MP3");
        mainPanel.add(radioMp3);
        radioMp3.setBounds(130, 240, 70, 22);

        btnDownload.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnDownload.setText("Download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });
        mainPanel.add(btnDownload);
        btnDownload.setBounds(60, 320, 120, 24);

        btnOpenLast.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnOpenLast.setText("Open Last");
        btnOpenLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenLastActionPerformed(evt);
            }
        });
        mainPanel.add(btnOpenLast);
        btnOpenLast.setBounds(200, 320, 120, 24);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/largelogoSmall3.png"))); // NOI18N
        mainPanel.add(logoLabel);
        logoLabel.setBounds(700, 570, 180, 50);

        areaInfo.setColumns(20);
        areaInfo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        areaInfo.setRows(5);
        jScrollPane1.setViewportView(areaInfo);

        mainPanel.add(jScrollPane1);
        jScrollPane1.setBounds(60, 410, 380, 170);

        barProgress.setBackground(new java.awt.Color(204, 204, 204));
        barProgress.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        barProgress.setForeground(new java.awt.Color(255, 153, 51));
        mainPanel.add(barProgress);
        barProgress.setBounds(60, 370, 380, 20);

        getContentPane().add(mainPanel);
        mainPanel.setBounds(0, 0, 900, 670);

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
        showPreferencesPanel();
    }//GEN-LAST:event_mnuPreferencesActionPerformed

    //About
    private void mnuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAboutActionPerformed
        JDialogAbout dialogAbout = new JDialogAbout(this,true);
        dialogAbout.setVisible(true);
    }//GEN-LAST:event_mnuAboutActionPerformed

    //Exit
    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        if(JOptionPane.showConfirmDialog(null, "Do you want to exit the application?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
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
            txtFolder.setText(selectedFolder.getAbsolutePath());
        } 
    }//GEN-LAST:event_btnBrowseActionPerformed

    //Copia del portapapeles a JTextField "urlField"
    private void btnPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasteActionPerformed
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor df = DataFlavor.stringFlavor;
        if(cb.isDataFlavorAvailable(df)){
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
        
        Thread th = new Thread(){
            @Override
            public void run(){
                downloader.download(url, folder, format, areaInfo, barProgress);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFolder;
    private javax.swing.JLabel lblFormat;
    private javax.swing.JLabel lblUrl;
    private javax.swing.JLabel logoLabel;
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
    private javax.swing.JTextField txtFolder;
    private javax.swing.JTextField txtUrl;
    // End of variables declaration//GEN-END:variables
}
