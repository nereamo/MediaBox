package montoya.mediabox;

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
    private JPanelPreferences panelPreferences;
    private String tempPath;
    private String ytDlpLocation;
    private boolean createM3u;
    private double maxSpeed;
    
   
    public MainFrame() {
        initComponents();
        framePanel();
        preferencesPanel();
    }
    
    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getYtDlpLocation() {
        return ytDlpLocation;
    }

    public void setYtDlpLocation(String ytDlpLocation) {
        this.ytDlpLocation = ytDlpLocation;
    }

    public boolean isCreateM3u() {
        return createM3u;
    }

    public void setCreateM3u(boolean createM3u) {
        this.createM3u = createM3u;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    
    //Método que contiene las propiedades de JFrame
    private void framePanel(){
        setTitle("MediaBox");
        setResizable(false);
        setSize(900,700);
        setLocationRelativeTo(this);
        setLayout(null);
        
        mainPanel.setSize(900,700);
        getContentPane().add(mainPanel);
    }
    
    //Método que contiene las propiedades de JPanel Preferences
    private void preferencesPanel(){
        panelPreferences = new JPanelPreferences(this);
        panelPreferences.setBounds(0, 0, 900, 700);
        panelPreferences.setVisible(false);
        getContentPane().add(panelPreferences);
    }
    
    //Metodo que permite mostrar el JPanel principal del JFrame
    public void showMainPanel() {
        panelPreferences.setVisible(false);
        mainPanel.setVisible(true);
    }
    
    //Metodo quepermite mostrar el JPanel Preferences 
    public void showPreferencesPanel() {
        mainPanel.setVisible(false);
        panelPreferences.setVisible(true);
    }
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        urlLabel = new javax.swing.JLabel();
        urlField = new javax.swing.JTextField();
        pasteButton = new javax.swing.JButton();
        folderLabel = new javax.swing.JLabel();
        folderField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        formatLabel = new javax.swing.JLabel();
        mp4Radio = new javax.swing.JRadioButton();
        mp3Radio = new javax.swing.JRadioButton();
        flvButton = new javax.swing.JRadioButton();
        downloadButton = new javax.swing.JButton();
        openVideoButton = new javax.swing.JButton();
        logoLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        itemExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        itemPreferences = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        itemAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("mainFrame"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(null);

        mainPanel.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        mainPanel.setMinimumSize(new java.awt.Dimension(900, 700));
        mainPanel.setPreferredSize(new java.awt.Dimension(900, 670));
        mainPanel.setLayout(null);

        urlLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        urlLabel.setText("URL:");
        mainPanel.add(urlLabel);
        urlLabel.setBounds(60, 60, 37, 20);

        urlField.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mainPanel.add(urlField);
        urlField.setBounds(160, 90, 280, 23);

        pasteButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        pasteButton.setText("Paste");
        pasteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteButtonActionPerformed(evt);
            }
        });
        mainPanel.add(pasteButton);
        pasteButton.setBounds(60, 90, 90, 24);

        folderLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        folderLabel.setText("Folder:");
        mainPanel.add(folderLabel);
        folderLabel.setBounds(60, 160, 50, 20);

        folderField.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mainPanel.add(folderField);
        folderField.setBounds(160, 190, 280, 23);

        browseButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        mainPanel.add(browseButton);
        browseButton.setBounds(60, 190, 90, 24);

        formatLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        formatLabel.setText("Format: ");
        mainPanel.add(formatLabel);
        formatLabel.setBounds(60, 260, 53, 17);

        mp4Radio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mp4Radio.setText("MP4");
        mainPanel.add(mp4Radio);
        mp4Radio.setBounds(60, 290, 60, 22);

        mp3Radio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mp3Radio.setText("MP3");
        mainPanel.add(mp3Radio);
        mp3Radio.setBounds(150, 290, 70, 22);

        flvButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        flvButton.setText("FLV");
        mainPanel.add(flvButton);
        flvButton.setBounds(240, 290, 70, 22);

        downloadButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        downloadButton.setText("Download");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });
        mainPanel.add(downloadButton);
        downloadButton.setBounds(70, 410, 140, 24);

        openVideoButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        openVideoButton.setText("Open Last Video");
        mainPanel.add(openVideoButton);
        openVideoButton.setBounds(380, 410, 150, 24);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/largelogoSmall3.png"))); // NOI18N
        mainPanel.add(logoLabel);
        logoLabel.setBounds(700, 570, 180, 50);

        jButton1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton1.setText("Cancel");
        mainPanel.add(jButton1);
        jButton1.setBounds(230, 410, 140, 24);

        jButton2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton2.setText("Delete");
        mainPanel.add(jButton2);
        jButton2.setBounds(450, 90, 72, 24);

        getContentPane().add(mainPanel);
        mainPanel.setBounds(0, 0, 900, 670);

        menuBar.setBackground(new java.awt.Color(255, 102, 0));
        menuBar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 102, 0)));
        menuBar.setToolTipText("");
        menuBar.setMinimumSize(new java.awt.Dimension(70, 35));
        menuBar.setPreferredSize(new java.awt.Dimension(70, 35));

        menuFile.setBackground(new java.awt.Color(255, 102, 0));
        menuFile.setBorder(null);
        menuFile.setForeground(new java.awt.Color(255, 255, 255));
        menuFile.setText("File");
        menuFile.setToolTipText("File");
        menuFile.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuFile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        menuFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menuFile.setPreferredSize(new java.awt.Dimension(40, 40));

        itemExit.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        itemExit.setText("Exit");
        itemExit.setToolTipText("Exit");
        itemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemExitActionPerformed(evt);
            }
        });
        menuFile.add(itemExit);

        menuBar.add(menuFile);

        menuEdit.setBorder(null);
        menuEdit.setForeground(new java.awt.Color(255, 255, 255));
        menuEdit.setText("Edit");
        menuEdit.setToolTipText("Edit");
        menuEdit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        menuEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menuEdit.setMinimumSize(new java.awt.Dimension(40, 40));
        menuEdit.setPreferredSize(new java.awt.Dimension(40, 40));

        itemPreferences.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        itemPreferences.setText("Preferences");
        itemPreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemPreferencesActionPerformed(evt);
            }
        });
        menuEdit.add(itemPreferences);

        menuBar.add(menuEdit);

        menuHelp.setBorder(null);
        menuHelp.setForeground(new java.awt.Color(255, 255, 255));
        menuHelp.setText("Help");
        menuHelp.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuHelp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        menuHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menuHelp.setMinimumSize(new java.awt.Dimension(40, 40));
        menuHelp.setPreferredSize(new java.awt.Dimension(40, 40));

        itemAbout.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        itemAbout.setText("About");
        itemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAboutActionPerformed(evt);
            }
        });
        menuHelp.add(itemAbout);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Boton Preferences
    private void itemPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemPreferencesActionPerformed
        showPreferencesPanel();
    }//GEN-LAST:event_itemPreferencesActionPerformed

    //Boton que abre el JDialog About
    private void itemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAboutActionPerformed
        JDialogAbout dialogAbout = new JDialogAbout(this,true);
        dialogAbout.setVisible(true);
    }//GEN-LAST:event_itemAboutActionPerformed

    //Boton que permite salir de la aplicación
    private void itemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemExitActionPerformed
        if(JOptionPane.showConfirmDialog(null, "Do you want to exit the application?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_itemExitActionPerformed

    //Abre la ruta (directorio) donde se guardara el archivo descargado
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
       JFileChooser directory = new JFileChooser();
        directory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = directory.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = directory.getSelectedFile();
            folderField.setText(selectedFolder.getAbsolutePath());
        } 
    }//GEN-LAST:event_browseButtonActionPerformed

    //Botón "Paste" que permite copiar del portapapeles a JTextField urlField
    private void pasteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteButtonActionPerformed
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor dataFlavor = DataFlavor.stringFlavor;
        if(systemClipboard.isDataFlavorAvailable(dataFlavor)){
            try {
                String clipboardContent = (String) systemClipboard.getData(dataFlavor);
                urlField.setText(clipboardContent);
            } catch (UnsupportedFlavorException ex) {
                System.getLogger(MainFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (IOException ex) {
                System.getLogger(MainFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }//GEN-LAST:event_pasteButtonActionPerformed

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
        String url = urlField.getText().trim();
    String folder = folderField.getText().trim();

    if (url.isEmpty() || folder.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a URL and select a folder.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Determinar formato
    String format;
    if (mp3Radio.isSelected()) {
        format = "mp3";
    } else if (flvButton.isSelected()) {
        format = "flv";
    } else {
        format = "mp4";
    }

    // Verificar ubicación de yt-dlp
    if (ytDlpLocation == null || ytDlpLocation.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please configure the yt-dlp location in Preferences.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Construir ProcessBuilder
    ProcessBuilder pb;
    if ("mp3".equals(format)) {
        pb = new ProcessBuilder(
                ytDlpLocation,
                "-x", "--audio-format", "mp3",
                "--add-header", "User-Agent: Mozilla/5.0",
                "-o", folder + File.separator + "%(title)s.%(ext)s",
                url
        );
    } else if ("flv".equals(format)) {
        pb = new ProcessBuilder(
                ytDlpLocation,
                "-f", "flv/best",
                "--add-header", "User-Agent: Mozilla/5.0",
                "-o", folder + File.separator + "%(title)s.%(ext)s",
                url
        );
    } else {
        pb = new ProcessBuilder(
                ytDlpLocation,
                "-f", "bestvideo+bestaudio/best",
                "--add-header", "User-Agent: Mozilla/5.0",
                "-o", folder + File.separator + "%(title)s.%(ext)s",
                url
        );
    }

    pb.redirectErrorStream(true);

    try {
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line); // salida en consola
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            JOptionPane.showMessageDialog(this, "Download completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "An error occurred during the download. Exit code: " + exitCode, "Error", JOptionPane.ERROR_MESSAGE);
        }

        reader.close();
        process.destroy();

    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error executing yt-dlp:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (InterruptedException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Download interrupted.", "Error", JOptionPane.ERROR_MESSAGE);
        Thread.currentThread().interrupt();
    }
    }//GEN-LAST:event_downloadButtonActionPerformed

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
    private javax.swing.JButton browseButton;
    private javax.swing.JButton downloadButton;
    private javax.swing.JRadioButton flvButton;
    private javax.swing.JTextField folderField;
    private javax.swing.JLabel folderLabel;
    private javax.swing.JLabel formatLabel;
    private javax.swing.JMenuItem itemAbout;
    private javax.swing.JMenuItem itemExit;
    private javax.swing.JMenuItem itemPreferences;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JRadioButton mp3Radio;
    private javax.swing.JRadioButton mp4Radio;
    private javax.swing.JButton openVideoButton;
    private javax.swing.JButton pasteButton;
    private javax.swing.JTextField urlField;
    private javax.swing.JLabel urlLabel;
    // End of variables declaration//GEN-END:variables
}
