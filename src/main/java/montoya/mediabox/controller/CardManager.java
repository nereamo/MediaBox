package montoya.mediabox.controller;

import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 *
 * @author Nerea
 */
public class CardManager {
    
    private JPanel container;
    private CardLayout layout;
    public static final String CARD_LOGIN = "login";
    public static final String CARD_MAIN = "main";
    public static final String CARD_PREF = "preferences";
    
    public CardManager(JPanel container, CardLayout cards){
        this.container = container;
        this.layout = cards; 
    }
    
    //Añade los panels a CardLayout
    public void initCards(JPanel pnlLogin, JPanel pnlMain, JPanel preferences){
        
        container.add(pnlLogin,CARD_LOGIN);
        container.add(pnlMain, CARD_MAIN);
        container.add(preferences, CARD_PREF); 
    }

    //Cambia el panel que se muestra
    public void showCard(String cardName) {
        layout.show(container, cardName);
        container.revalidate();
        container.repaint();
    } 
}
