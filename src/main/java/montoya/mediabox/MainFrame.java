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
   
    public MainFrame() {
        initComponents();
        framePanel();
        preferencesPanel();
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
        downloadButton = new javax.swing.JButton();
        openVideoButton = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        logoLabel = new javax.swing.JLabel();
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

        downloadButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        downloadButton.setText("Download");
        mainPanel.add(downloadButton);
        downloadButton.setBounds(70, 410, 140, 24);

        openVideoButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        openVideoButton.setText("Open Last Video");
        mainPanel.add(openVideoButton);
        openVideoButton.setBounds(240, 410, 150, 24);

        jRadioButton1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jRadioButton1.setText("FLV");
        mainPanel.add(jRadioButton1);
        jRadioButton1.setBounds(240, 290, 70, 22);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/largelogoSmall3.png"))); // NOI18N
        mainPanel.add(logoLabel);
        logoLabel.setBounds(700, 570, 180, 50);

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
    private javax.swing.JTextField folderField;
    private javax.swing.JLabel folderLabel;
    private javax.swing.JLabel formatLabel;
    private javax.swing.JMenuItem itemAbout;
    private javax.swing.JMenuItem itemExit;
    private javax.swing.JMenuItem itemPreferences;
    private javax.swing.JRadioButton jRadioButton1;
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
