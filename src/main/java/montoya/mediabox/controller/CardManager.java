package montoya.mediabox.controller;

import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 * Clase que gestiona los distintos paneles (vistas) mostrados en la interfaz.
 * <p> Vistas:
 * <ul> 
 * <li> Panel Login.</li>
 * <li> Panel Downloads.</li>
 * <li> Panel Preferences.</li>
 * </ul> 
 * 
 * @author Nerea
 */
public class CardManager {
    
    /** Contiene los paneles mostrados en la aplicación. */
    private JPanel container;
    
    /** Layout usado para los distintos paneles(vistas) como si fueran cartas. */
    private CardLayout layout;
    
    /** 
     * Identificadores de los distintos paneles.
     * Cada constante corresponde a un panel.
     */
    public static final String CARD_LOGIN = "login";
    public static final String CARD_DOWN = "downloads";
    public static final String CARD_PREF = "preferences";
    
    /**
     * Constructor que inicializa el gestor de tarjetas (vistas) utilizadas por el CardLayout.
     * 
     * @param container JPanel que contiene las tarjetas (paneles) que seran mostrados
     * @param cards Layout de tipo CardLayout encargado de gestionar el cambio entre vistas
     */
    public CardManager(JPanel container, CardLayout cards){
        this.container = container;
        this.layout = cards; 
    }
    
    /**
     * Inicializa las tarjetas (vistas) del CardLayout
     * 
     * @param pnlLogin JPanel correspondiente a la pantalla Login
     * @param pnlDownloads JPanel correspondiente a la pantalla Downloads
     * @param preferences JPanel correspondiente a la pantalla Preferences
     */
    public void initCards(JPanel pnlLogin, JPanel pnlDownloads, JPanel preferences){
        
        container.add(pnlLogin,CARD_LOGIN);
        container.add(pnlDownloads, CARD_DOWN);
        container.add(preferences, CARD_PREF); 
    }

    /**
     * Muestra la tarjeta indicada del CardLayout
     * 
     * @param cardName Identificador de la tarjeta a mostrar
     */
    public void showCard(String cardName) {
        layout.show(container, cardName);
        container.revalidate();
        container.repaint();
    } 
}
