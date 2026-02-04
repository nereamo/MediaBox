package montoya.mediabox;

import java.util.*;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.CardLayout;
import java.awt.Image;
import java.util.logging.Logger;
import montoya.mediabox.panels.*;
import montoya.mediabox.controller.*;
import montoya.mediabox.dialogs.DialogAbout;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.styleConfig.StyleConfig;
import montoya.mediapollingcomponent.MediaEvent;
import montoya.mediapollingcomponent.MediaListener;

/**
 * Class principal
 *
 * @author Nerea
 */
public class MainFrame extends JFrame {

    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());
    
    public Downloads pnlDownload;
    private Preferences pnlPreferences;
    private Login pnlLogin;
    private DownloadManager downloadManager;
    
    private CardManager cardManager;
    private CardLayout layout;
    private JPanel container;
    public JLabel lblMessage = new JLabel();
    private final Set<String> folderPaths = new HashSet<>();
    private boolean isLoggedIn = false;

    public MainFrame() {
        initComponents();
        
        configurationFrame();
        configIconMenu();
        configIconMenuItems();
        setMenuVisible(false);
        
        Image icon = new ImageIcon(getClass().getResource("/images/small_logo.png")).getImage();
        this.setIconImage(icon);
        
        layout = new CardLayout();
        container = new JPanel(layout);
        cardManager = new CardManager(container, layout);
        
        downloadManager = new DownloadManager(new FileProperties());
        pnlLogin = new Login(this, cardManager, mediaPollingComponent);
        pnlDownload = new Downloads(this, folderPaths, downloadManager, mediaPollingComponent);
        pnlPreferences = new Preferences(this, downloadManager, cardManager);

        cardManager.initCards(pnlLogin, pnlDownload, pnlPreferences);
        cardManager.showCard("login");
        pnlLogin.autoLogin();

        this.setContentPane(container);
        this.setResizable(true);
        this.setVisible(true);
    }

    //Configuración del frame
    private void configurationFrame() {
        this.setTitle("MediaBox");
        this.setResizable(false);
        this.setSize(940, 1200);
        this.setLocationRelativeTo(this);
    }
    
    //Iconos de los botones
    private void configIconMenu(){
        StyleConfig.styleMenuItems(mnuEdit, "/images/edit.png",null, "Settings");
        StyleConfig.styleMenuItems(mnuFile, "/images/logout_exit.png",null, "Logout or Exit");
        StyleConfig.styleMenuItems(mnuHelp, "/images/help.png",null, "Information MediaBox");
    }
    
    //Iconos de los botones
    private void configIconMenuItems(){
        StyleConfig.styleMenuItems(itemExit, "/images/exit.png", "Exit", "Close App");
        StyleConfig.styleMenuItems(itemLogout, "/images/logout.png", "Logout", "Return to login");
        StyleConfig.styleMenuItems(itemPreferences, "/images/settings.png", "Settings", "Edit settings");
        StyleConfig.styleMenuItems(itemAbout, "/images/information.png", "About", "Information MediaBox");
    }
    
    //Visibilidad del JMenu
    public void setMenuVisible(boolean visible) {
        menuBar.setBackground(StyleConfig.LIGHT_BLUE_COLOR);
        menuBar.setVisible(visible);
        menuBar.add(Box.createHorizontalGlue());
        lblMessage.setForeground(StyleConfig.DARK_BLUE_COLOR);
        lblMessage.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); 
        menuBar.add(lblMessage);
        
        this.revalidate();
        this.repaint();
    }

    //Método que inicializa el componente
    public void initializePolling(String token) {

        mediaPollingComponent.setToken(token);
        mediaPollingComponent.addMediaListener(new MediaListener() {
            @Override
            public void newMediaFound(MediaEvent me) {
                System.out.println("Files found uploaded successfully");
            }
        });
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mediaPollingComponent = new montoya.mediapollingcomponent.MediaPollingComponent();
        menuBar = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        itemLogout = new javax.swing.JMenuItem();
        itemExit = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();
        itemPreferences = new javax.swing.JMenuItem();
        mnuHelp = new javax.swing.JMenu();
        itemAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("mainFrame"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(null);

        mediaPollingComponent.setApiUrl("https://difreenet9.azurewebsites.net");
        mediaPollingComponent.setPollingInterval(10);
        mediaPollingComponent.setRunning(true);
        getContentPane().add(mediaPollingComponent);
        mediaPollingComponent.setBounds(1230, 680, 35, 35);

        menuBar.setBackground(new java.awt.Color(0, 0, 0));
        menuBar.setBorder(new javax.swing.border.MatteBorder(null));
        menuBar.setForeground(new java.awt.Color(255, 255, 255));
        menuBar.setToolTipText("");
        menuBar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        menuBar.setMinimumSize(new java.awt.Dimension(70, 35));
        menuBar.setPreferredSize(new java.awt.Dimension(70, 35));

        mnuFile.setBackground(new java.awt.Color(255, 102, 0));
        mnuFile.setBorder(null);
        mnuFile.setForeground(new java.awt.Color(255, 255, 255));
        mnuFile.setToolTipText("");
        mnuFile.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        mnuFile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mnuFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mnuFile.setPreferredSize(new java.awt.Dimension(40, 40));

        itemLogout.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        itemLogout.setToolTipText("");
        itemLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemLogoutActionPerformed(evt);
            }
        });
        mnuFile.add(itemLogout);

        itemExit.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        itemExit.setToolTipText("");
        itemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemExitActionPerformed(evt);
            }
        });
        mnuFile.add(itemExit);

        menuBar.add(mnuFile);

        mnuEdit.setBorder(null);
        mnuEdit.setForeground(new java.awt.Color(255, 255, 255));
        mnuEdit.setToolTipText("");
        mnuEdit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        mnuEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mnuEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mnuEdit.setMinimumSize(new java.awt.Dimension(40, 40));
        mnuEdit.setPreferredSize(new java.awt.Dimension(40, 40));

        itemPreferences.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        itemPreferences.setToolTipText("");
        itemPreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemPreferencesActionPerformed(evt);
            }
        });
        mnuEdit.add(itemPreferences);

        menuBar.add(mnuEdit);

        mnuHelp.setBorder(null);
        mnuHelp.setForeground(new java.awt.Color(255, 255, 255));
        mnuHelp.setToolTipText("");
        mnuHelp.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        mnuHelp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mnuHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mnuHelp.setMinimumSize(new java.awt.Dimension(40, 40));
        mnuHelp.setPreferredSize(new java.awt.Dimension(40, 40));

        itemAbout.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        itemAbout.setToolTipText("");
        itemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAboutActionPerformed(evt);
            }
        });
        mnuHelp.add(itemAbout);

        menuBar.add(mnuHelp);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Preferences
    private void itemPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemPreferencesActionPerformed
        cardManager.showCard("preferences");
    }//GEN-LAST:event_itemPreferencesActionPerformed

    //About
    private void itemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAboutActionPerformed
        DialogAbout dialogAbout = new DialogAbout(this, true);
        dialogAbout.setVisible(true);
    }//GEN-LAST:event_itemAboutActionPerformed

    //Exit
    private void itemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemExitActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Do you want to exit the application?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_itemExitActionPerformed

    private void itemLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemLogoutActionPerformed
        setMenuVisible(false);
        this.isLoggedIn = false;
        cardManager.showCard("login");
        StyleConfig.showMessageInfo(pnlLogin.lblMessage, "Closed sesion.");
    }//GEN-LAST:event_itemLogoutActionPerformed

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
    private javax.swing.JMenuItem itemAbout;
    private javax.swing.JMenuItem itemExit;
    private javax.swing.JMenuItem itemLogout;
    private javax.swing.JMenuItem itemPreferences;
    private montoya.mediapollingcomponent.MediaPollingComponent mediaPollingComponent;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuHelp;
    // End of variables declaration//GEN-END:variables
}
