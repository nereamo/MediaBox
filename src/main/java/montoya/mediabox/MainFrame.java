package montoya.mediabox;

import java.util.*;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import montoya.mediabox.panels.*;
import montoya.mediabox.controller.*;
import montoya.mediabox.dialogs.DialogAbout;
import montoya.mediabox.download.DownloadManager;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.configUI.UIStyles;
import montoya.mediapollingcomponent.MediaEvent;
import montoya.mediapollingcomponent.MediaListener;
import montoya.mediapollingcomponent.apiclient.Media;

/**
 * Ventana principal de la aplicación {@code MediaBox}.
 * <p>
 * La clase extiende {@code javax.swing.JFrame}, permite que esta clase sea un diálogo modal.
 * Utiliza un {@link CardLayout} para el intercambio de paneles (vistas) de la interfaz.
 * </p>
 * <p>
 * Controla:
 * <ul>
 * <li> Barra de menú y sus acciones.</li>
 * <li> Autenticación del usuario.</li>
 * <li> Inicialización de componetntes y estilos visuales (LookAndFeel).</li>
 * <li> Comunicación con el componente de escucha {@link MediaPolling}.</li>
 * </ul>
 * 
 * @author Nerea
 */
public class MainFrame extends JFrame {
    
    /** {@link Downloads} Gestiona la lista y el estado de las descargas activas. */
    public Downloads pnlDownload;
    
    /** {@link Preferences} Configura los ajustes de la aplicación. */
    private Preferences pnlPreferences;
    
    /** {@link Login} Gestiona la autenticación de usuario. */
    private Login pnlLogin;
    
    /** {@link DownloadManager} Contiene los parámetros necesarios para realizar descargas. */
    private DownloadManager downloadManager;
    
    /** {@link CardManager} Gestionar el intercambio de paneles. */
    private CardManager cardManager;
    
    /** Layout usado para los distintos paneles(vistas) como si fueran cartas. */
    private CardLayout layout;
    
    /** Contiene los paneles mostrados en la aplicación. */
    private JPanel container;
    
    /** Etiqueta para mostrar mensajes informativos al usuario. */
    public JLabel lblMessage = new JLabel();
    
    /** Colección de rutas de carpetas seleccionadas por el usuario. */
    private final Set<String> folderPaths = new HashSet<>();

    /**
     * Constructor que inicializa la ventana principal.
     * Contiene los métodos necesarios para construir la interfaz y la navegación entre las vistas.
     */
    public MainFrame() {
        initLookAndFeelMenu(); //Colores de MenuBar
        initComponents(); //Inicializa los componentes
        initContainer(); //Inicialización del contenedor de los JPanel
        initFrameConfig(); //Título, tamaño e icono del JFrame
        initMenuAndItems(); //Estilos aplicados a JMenuBar y JMenuItem
        setMenuVisible(false); //Visibilidad del Menú
        initPanels(); //Instáncias de los distintos JPanel
        initNavigation(); //Organiza las vistas
    }
    
    /** Configuración de los colores del menú y sus ítems {@link UIManager} */
    private void initLookAndFeelMenu(){
        UIManager.put("PopupMenu.background", UIStyles.MEDIUM_GREY_COLOR);
        UIManager.put("MenuItem.selectionBackground", UIStyles.LIGHT_PURPLE);
        UIManager.put("Menu.selectionBackground", UIStyles.MEDIUM_GREY_COLOR);
        UIManager.put("MenuItem.selectionForeground", UIStyles.BLACK_COLOR);
    }
    
    /** Propiedades principales de la ventana */
    private void initFrameConfig() {
        this.setTitle("MediaBox");
        this.setSize(940, 1200);
        this.setLocationRelativeTo(null);
        Image icon = new ImageIcon(getClass().getResource("/images/icon.png")).getImage();
        this.setIconImage(icon);
    }
    
    /** Aplica tamaño, texto, icono, tooltip y cambia el icono (color) al seleccionar un ítem {@link UIStyles} */
    private void initMenuAndItems(){
        UIStyles.styleMenuAndItems(mnuEdit, null, "/images/edit.png", "Settings", "");
        UIStyles.styleMenuAndItems(mnuFile, null, "/images/logout_exit.png", "Logout or Exit", "");
        UIStyles.styleMenuAndItems(mnuHelp, null, "/images/help.png", "Information MediaBox", "");
        
        UIStyles.styleMenuAndItems(itemExit, "Exit", "/images/exit.png", "Close App", "/images/exit_black.png");
        UIStyles.styleMenuAndItems(itemLogout, "Logout", "/images/logout.png", "Return to login", "/images/logout_black.png");
        UIStyles.styleMenuAndItems(itemPreferences, "Settings", "/images/settings.png", "Edit settings", "/images/settings_black.png");
        UIStyles.styleMenuAndItems(itemAbout, "About", "/images/information.png", "Information MediaBox", "/images/information_black.png");
    }
    
    /** Controla la visibilidad del menú y el nombre de usuario logeado {@link UIStyles} */
    public void setMenuVisible(boolean visible) {
        menuBar.setBackground(UIStyles.DARK_GREY_COLOR);
        menuBar.add(Box.createHorizontalGlue());
        
        lblMessage.setForeground(UIStyles.DARK_GREY_COLOR);
        lblMessage.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        menuBar.add(lblMessage);
        
        menuBar.setVisible(visible);
        this.revalidate();
        this.repaint();
    }
    
    /** Inicializa el contenedor permitiendo el intercambio entre los distintos paneles (vistas) */
    private void initContainer() {
        layout = new CardLayout();
        container = new JPanel(layout);
        cardManager = new CardManager(container, layout);
        this.setResizable(true);
        
        this.setJMenuBar(null);
        this.getContentPane().setLayout(new java.awt.BorderLayout());
        this.getContentPane().add(container, java.awt.BorderLayout.CENTER);
        this.getContentPane().add(menuBar, java.awt.BorderLayout.NORTH);
        this.getContentPane().remove(mediaPollingComponent);
    }
    
    /** Inicializa los JPanel mostrados en la apliación */
    private void initPanels(){
        downloadManager = new DownloadManager(new FileProperties());
        pnlLogin = new Login(this, cardManager, mediaPollingComponent);
        pnlDownload = new Downloads(this, folderPaths, downloadManager, mediaPollingComponent);
        pnlPreferences = new Preferences(this, downloadManager, cardManager);
        cardManager.initCards(pnlLogin, pnlDownload, pnlPreferences);
    }
    
    /** Establece la navegación principal mostrando la pantalla de login si el usuario no esta autentificado */
    private void initNavigation() {
        cardManager.showCard("login");
        pnlLogin.autoLogin();
    }

    /**
     * Configuración del componente que escucha medios de la API.
     * Inicializa el componente listener con el token de seguridad.
     * 
     * @param token de acceso para la API.
     * @see MediaListener
     */
    public void initializePolling(String token) {

        mediaPollingComponent.setToken(token); //Token autenticación
        mediaPollingComponent.addMediaListener(new MediaListener() { //Listener
            private boolean isFirstRun = true; //Evita la primera escucha

            @Override
            public void newMediaFound(MediaEvent me) {
                List<Media> listaMedios = me.getMediaList(); //Obtiene medios
                
                if (listaMedios != null && !listaMedios.isEmpty()) {
                    
                    if (isFirstRun) { //Ignora la primera ejecución (datos iniciales)
                        isFirstRun = false;
                        return;
                    }

                    Media ultimoMedia = listaMedios.get(listaMedios.size() - 1); //Obtiene último archivo subido
                    System.out.println("New media: " + ultimoMedia.mediaFileName); //Muestra información del último archivo
                }
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

        mediaPollingComponent.setApiUrl("https://difreenet9.azurewebsites.net");
        mediaPollingComponent.setPollingInterval(10);
        mediaPollingComponent.setRunning(true);
        getContentPane().add(mediaPollingComponent, java.awt.BorderLayout.CENTER);

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

    /**
     * Cambia la vista actual al panel de configuración Preferences.
     * 
     * @param evt Evento de acción generado al hacer clic en el ítem
     */
    private void itemPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemPreferencesActionPerformed
        cardManager.showCard("preferences");
    }//GEN-LAST:event_itemPreferencesActionPerformed

    /**
     * Abre el diálogo de información "About" de la aplicación.
     * 
     * @param evt Evento de acción generado al hacer clic en el ítem
     */
    private void itemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAboutActionPerformed
        DialogAbout dialogAbout = new DialogAbout(this, true);
        dialogAbout.setVisible(true);
    }//GEN-LAST:event_itemAboutActionPerformed

    /**
     * Solicita confirmación por parte del usuario y cierra la aplicación en caso de ser afirmativa.
     * 
     * @param evt Evento de acción generado al hacer clic en el ítem
     */
    private void itemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemExitActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Do you want to exit the application?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_itemExitActionPerformed

    /**
     * Cierra la sessión del usuario, oculta el menú y muetra el panel de login.
     * 
     * @param evt Evento de acción generado al hacer clic en el ítem
     */
    private void itemLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemLogoutActionPerformed
        setMenuVisible(false);
        cardManager.showCard("login");
        UIStyles.showMessageInfo(pnlLogin.lblMessage, "Session closed.");
    }//GEN-LAST:event_itemLogoutActionPerformed

    public static void main(String args[]) {
        /* Set the Metal look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            // Esto activa el tema oscuro de FlatLaf
            com.formdev.flatlaf.FlatDarkLaf.setup();

            UIManager.put("ComboBox.selectionBackground", UIStyles.LIGHT_PURPLE);
            UIManager.put("ComboBox.selectionForeground", Color.WHITE);
            UIManager.put("List.selectionBackground", UIStyles.LIGHT_PURPLE);
            UIManager.put("List.selectionForeground", Color.WHITE);
            
            UIManager.put("List.selectionInactiveBackground", UIStyles.LIGHT_PURPLE);
            UIManager.put("ComboBox.selectionInactiveBackground", UIStyles.LIGHT_PURPLE);

            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 15);

            //Color de foco de los componentes
            UIManager.put("Component.focusColor", UIStyles.LIGHT_PURPLE);
            UIManager.put("Component.focusedBorderColor", UIStyles.LIGHT_PURPLE);
            UIManager.put("TextComponent.focusedBorderColor", UIStyles.LIGHT_PURPLE);

        } catch (Exception ex) {
            System.err.println("No se pudo iniciar FlatLaf, usando tema por defecto.");
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
