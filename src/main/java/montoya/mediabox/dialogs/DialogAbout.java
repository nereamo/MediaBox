package montoya.mediabox.dialogs;

import java.awt.*;
import javax.swing.*;
import montoya.mediabox.configUI.UIStyles;

/**
 * Dialog que contiene la información de alumno, curso y los recursos para la tarea
 * 
 * @author Nerea
 */
public class DialogAbout extends JDialog {
    
    /**
     * Constructor que inicializa un dialogo modal que muestra información de la aplicación.
     * 
     * @param parent Ventana padre donde se mostrará el dialogo
     * @param modal El dialogo puede ser mpdal, bloqueando la ventana hasta cerrarlo
     */
    public DialogAbout(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(400,350);
        setLocationRelativeTo(this);
        setTitle("About");
        
        //Texto mostrado en About
        String aboutText =  """
                            <html>
                                <style>
                                    body { margin-left: 20px; }
                                </style>
                            
                                <h2>Nerea Montoya</h2>
                                <p>Course: 2025/2026</p> 
                                <p>Module: Interface Development</p>
                
                                <h2>MediaBox</h2>
                                <p>Application for downloading videos.</p>
                           
                                <h2>Resources:</h2> 
                                <ul>
                                    <li>yt-dlp</li>
                                    <li>ffmpeg</li> 
                                    <li>ffplay.exe</li> 
                                    <li>ffprobe.exe</li> 
                                </ul>
                            </html>""";
        
        lblAbout.setText(aboutText);
        lblAbout.setVerticalAlignment(JLabel.TOP);
        UIStyles.styleFixLabel(lblAbout, aboutText, "");
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlAbout = new javax.swing.JPanel();
        lblAbout = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlAbout.setMinimumSize(new java.awt.Dimension(400, 350));
        pnlAbout.setLayout(null);

        lblAbout.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblAbout.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        pnlAbout.add(lblAbout);
        lblAbout.setBounds(0, 0, 400, 350);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlAbout, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlAbout, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblAbout;
    private javax.swing.JPanel pnlAbout;
    // End of variables declaration//GEN-END:variables
}
