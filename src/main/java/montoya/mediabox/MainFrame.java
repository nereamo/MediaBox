package montoya.mediabox;

import java.util.*;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.CardLayout;
import java.util.logging.Logger;
import montoya.mediabox.panels.*;
import montoya.mediabox.controller.*;
import montoya.mediabox.dialogs.DialogAbout;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediapollingcomponent.MediaEvent;
import montoya.mediapollingcomponent.MediaListener;
import montoya.mediapollingcomponent.MediaPollingComponent;

/**
 * Class principal
 *
 * @author Nerea
 */
public class MainFrame extends JFrame {

    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());
    
    private Downloads pnlDownload;
    private Preferences pnlPreferences;
    private Login pnlLogin;
    private DownloadManager downloadManager;
    
    private CardManager cardManager;
    private CardLayout layout;
    private JPanel container;
    
    private final Set<String> folderPaths = new HashSet<>();
    private boolean isLoggedIn = false;

    public MainFrame() {
        initComponents();
        
        configurationFrame();
        
        layout = new CardLayout();
        container = new JPanel(layout);
        cardManager = new CardManager(container, layout);
        mediaPollingComponent = new MediaPollingComponent();
        
        downloadManager = new DownloadManager(new FileProperties());
        pnlLogin = new Login(this, cardManager, mediaPollingComponent);
        pnlDownload = new Downloads(this, folderPaths, downloadManager, mediaPollingComponent);
        pnlPreferences = new Preferences(this, downloadManager, cardManager);

        cardManager.initCards(pnlLogin, pnlDownload, pnlPreferences);
        cardManager.showCard("login");
        pnlLogin.autoLogin();
        
        this.setContentPane(container);
        this.setVisible(true);
    }

    //Configuración del frame
    private void configurationFrame() {
        this.setTitle("MediaBox");
        this.setResizable(false);
        this.setSize(1300, 800);
        this.setLocationRelativeTo(this);
    }

    //Método que inicializa el componente
    public void initializePolling(String token) {

        mediaPollingComponent.setToken(token);
        mediaPollingComponent.setApiUrl("https://difreenet9.azurewebsites.net");
        mediaPollingComponent.setPollingInterval(5);

        mediaPollingComponent.addMediaListener(new MediaListener() {
            @Override
            public void newMediaFound(MediaEvent me) {
                System.out.println("Nuevos medios encontrados: " + me.getMediaList().size() + " " + me.getMediaList());
            }
        });

        mediaPollingComponent.setRunning(true);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        mediaPollingComponent = new montoya.mediapollingcomponent.MediaPollingComponent();
        menuBar = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuLogout = new javax.swing.JMenuItem();
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
        getContentPane().add(mediaPollingComponent);
        mediaPollingComponent.setBounds(1210, 680, 35, 35);

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

        mnuLogout.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mnuLogout.setText("Logout");
        mnuLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogoutActionPerformed(evt);
            }
        });
        mnuFile.add(mnuLogout);

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
        cardManager.showCard("preferences");
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

    private void mnuLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogoutActionPerformed
        
            this.isLoggedIn = false;
            cardManager.showCard("login");
            pnlLogin.resetFields();
            JOptionPane.showMessageDialog(this, "Closed sesion.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_mnuLogoutActionPerformed

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
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private montoya.mediapollingcomponent.MediaPollingComponent mediaPollingComponent;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem mnuAbout;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuItem mnuLogout;
    private javax.swing.JMenuItem mnuPreferences;
    // End of variables declaration//GEN-END:variables
}
