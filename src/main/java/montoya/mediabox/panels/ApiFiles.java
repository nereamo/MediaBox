
package montoya.mediabox.panels;

import java.awt.FlowLayout;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.styleConfig.StyleConfig;
import montoya.mediapollingcomponent.MediaPollingComponent;
import montoya.mediapollingcomponent.apiclient.Media;

/**
 *
 * @author Nerea
 */
public class ApiFiles extends javax.swing.JPanel {
    
    JButton btnDownload = new JButton("DOWNLOAD");
    JButton btnUpload = new JButton("UPLOAD");
    
    private InfoMedia infoMedia;
    private MediaPollingComponent mediaPollingComponent;
    private FileProperties fileProperties;


    public ApiFiles(InfoMedia infoMedia, MediaPollingComponent mediaPollingComponent, FileProperties fileProperties) {
        
        initComponents();
        
        this.infoMedia = infoMedia;
        this.mediaPollingComponent = mediaPollingComponent;
        this.fileProperties = fileProperties;
        
        
        configPanel();
        configButtons();
        buttonsActions();

        add(btnDownload);
        add(btnUpload);
    }
    
    private void configPanel(){
        StyleConfig.createTitleBorder(this, "DOWNLOAD & UPLOAD API FILES");
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    }
    
    private void configButtons(){
        
        btnDownload.setBackground(StyleConfig.LIGHT_BLUE_COLOR);
        btnDownload.setForeground(StyleConfig.DARK_BLUE_COLOR);
        btnDownload.setToolTipText("Download this API file to your computer");
        
        btnUpload.setBackground(StyleConfig.LIGHT_BLUE_COLOR);
        btnUpload.setForeground(StyleConfig.DARK_BLUE_COLOR);
        btnUpload.setToolTipText("Upload a local file to the API");
    }
    
    private void buttonsActions(){
        btnDownload.addActionListener(e -> downloadFile());
        btnUpload.addActionListener(e -> uploadFile());
    }
    
    private void downloadFile(){
        try {
            FileInformation info = infoMedia.getSelectedFile(); // <-- obtenemos el archivo desde InfoMedia
            if (info == null) {
                JOptionPane.showMessageDialog(this, "Please select a file in the table.");
                return;
            }

            // Validar que sea archivo de API
            if (!infoMedia.isNetworkFileSelected()) {
                JOptionPane.showMessageDialog(this, "Select a network file to download.");
                return;
            }

            // Obtener archivo de API
            List<Media> mediaList = mediaPollingComponent.getAllMedia(mediaPollingComponent.getToken());
            montoya.mediapollingcomponent.apiclient.Media mediaFile = mediaList.stream()
                    .filter(m -> m.mediaFileName.equals(info.name))
                    .findFirst()
                    .orElse(null);

            if (mediaFile == null) {
                JOptionPane.showMessageDialog(this, "Cannot find the selected media in API.");
                return;
            }

            // Elegir directorio local
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File folder = fileChooser.getSelectedFile();
            File file = new File(folder, info.name);

            // Descarga real
            mediaPollingComponent.download(mediaFile.id, file, mediaPollingComponent.getToken());

            // Actualizar JSON y tabla
            FileInformation newFile = new FileInformation(mediaFile.mediaFileName, file.length(),
                    mediaFile.mediaMimeType, LocalDate.now().toString(), folder.getAbsolutePath());

            fileProperties.addDownload(newFile);
            infoMedia.refreshFiles();

            JOptionPane.showMessageDialog(this, "Download completed.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Download failed:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void uploadFile(){
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File file = fileChooser.getSelectedFile();
            if (file == null || !file.exists()) {
                JOptionPane.showMessageDialog(this, "Cannot find the selected file.");
                return;
            }

            String mimeType = Files.probeContentType(file.toPath());
            mediaPollingComponent.uploadFileMultipart(file, mimeType, mediaPollingComponent.getToken());
            mediaPollingComponent.setLastChecked(OffsetDateTime.now().minusMinutes(1).toString());

            infoMedia.refreshFiles();

            JOptionPane.showMessageDialog(this, "Upload completed.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Upload failed:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
