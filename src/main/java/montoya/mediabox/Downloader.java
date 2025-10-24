package montoya.mediabox;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Nerea
 */
public class Downloader {
    
    private String tempPath;
    private String ytDlpLocation;
    private boolean createM3u;
    private double maxSpeed;
    
    public Downloader(){}
    
    //Getters y Setters
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
    
    //Verificacion de campos completados
    public void download(String url, String folder, String format, JTextArea outputArea) {
        if (url.isEmpty() || folder.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a URL and select a folder.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ytDlpLocation == null || ytDlpLocation.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please configure the yt-dlp location in Preferences.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProcessBuilder pb = buildCommand(url, folder, format);

        try {
            executeDownload(pb, outputArea);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error during download:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Instrucciones para la descarga
    private ProcessBuilder buildCommand(String url,String folder, String format) {
        ProcessBuilder pb;
        if ("mp3".equals(format)) {
            pb = new ProcessBuilder(
                    ytDlpLocation,
                    "-x", "--audio-format", "mp3","-o", folder + File.separator + "%(title)s.%(ext)s",url);
        
        } else {
            pb = new ProcessBuilder(
                    ytDlpLocation,
                    "-f", "mp4","-o", folder + File.separator + "%(title)s.%(ext)s",url);
        }

        //Playlist .m3u
        if (createM3u) {
            pb.command().add("--write-info-json");
        }

        //Limite de velocidad
        if (maxSpeed > 0) {
            pb.command().add("--limit-rate");
            pb.command().add(maxSpeed + "M");
        }

        pb.redirectErrorStream(true);
        return pb;
    }
    
    //Ejecuta proceso yt-dlp
    private void executeDownload(ProcessBuilder pb, JTextArea outputArea) throws IOException, InterruptedException {
        Process p = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String finalLine = line;
            SwingUtilities.invokeLater(() -> outputArea.append(finalLine + "\n"));
            }
        }

        if (p.waitFor() == 0) {
            JOptionPane.showMessageDialog(null, "Download completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "An error occurred during the download.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        p.destroy();
    }
}
