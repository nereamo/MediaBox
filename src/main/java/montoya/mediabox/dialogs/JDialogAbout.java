package montoya.mediabox.dialogs;

import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Contiene la información de alumno, curso y los recursos para la tarea
 * @author Nerea
 */
public class JDialogAbout extends JDialog {
    
    private static final Logger logger = Logger.getLogger(JDialogAbout.class.getName());

    public JDialogAbout(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(400,350);
        setLocationRelativeTo(this);
        setTitle("About");
        
        //Texto mostrado en About
        String aboutText =  """
                            <html>
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
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlAbout = new javax.swing.JPanel();
        lblAbout = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlAbout.setBackground(new java.awt.Color(255, 153, 51));
        pnlAbout.setMinimumSize(new java.awt.Dimension(400, 350));
        pnlAbout.setLayout(null);

        lblAbout.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblAbout.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        pnlAbout.add(lblAbout);
        lblAbout.setBounds(10, 10, 380, 270);

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

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JDialogAbout dialog = new JDialogAbout(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblAbout;
    private javax.swing.JPanel pnlAbout;
    // End of variables declaration//GEN-END:variables
}
