package montoya.mediabox;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
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
    
    private File lastDownloadedFile;

    public File getLastDownloadedFile() {
        return lastDownloadedFile;
    }
    
    //Verificacion de campos completados
    public void download(String url, String folder, String format, JTextArea outputArea, JProgressBar progressBar) {
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
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true); //Animación de JProgressBar
            
            executeDownload(pb, folder, outputArea, progressBar);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error during download:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Instrucciones de descarga
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

        //Playlist json .m3u
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
    private void executeDownload(ProcessBuilder pb, String folder, JTextArea outputArea, JProgressBar progressBar) throws IOException, InterruptedException {
        Process p = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String finalLine = line;
                
                //SwingUtilities.invokeLater(() -> outputArea.append(finalLine + "\n")); --> Lambda
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        outputArea.append(finalLine + "\n");
                    }
                });
                
                //Si hay progreso de descarga, obtiene los valores y actualiza la barra
                if (line.contains("[download]") && line.contains("%")) { 
                    int percent = extractPercentage(line);
                    if (percent >= 0 && percent <= 100) {
                        updateProgressBar(progressBar, percent);
                    }
                }
            }
        }

        if (p.waitFor() == 0) {
            File dir = new File(folder);
            File[] files = dir.listFiles();

            if (files != null && files.length > 0) {
                File latest = files[0];
                for (File f : files) {
                    if (f.lastModified() > latest.lastModified()) {
                        latest = f;
                    }
                }
                lastDownloadedFile = latest;
            }
            updateProgressBar(progressBar, 100);
            
            JOptionPane.showMessageDialog(null, "Download completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "An error occurred during the download.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        p.destroy();
    }
    
    //Actualiza la barra de progresso
    private void updateProgressBar(JProgressBar progressBar, int percent) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setIndeterminate(false);
                progressBar.setValue(percent);
                progressBar.setString("Downloading... " + percent + "%");
            }
        });
        
//        SwingUtilities.invokeLater(() -> {    --> lambda
//            progressBar.setIndeterminate(false);
//            progressBar.setValue(percent);
//            progressBar.setString("Downloading... " + percent + "%");
//        });
    }

    //Obtiene %
    private int extractPercentage(String line) {
        try {
            int start = line.indexOf("[download]") + 10;
            int end = line.indexOf("%", start);
            if (end > start) {
                String percent = line.substring(start, end).replaceAll("[^0-9.]", "").trim();
                return (int) Float.parseFloat(percent);
            }
        } catch (Exception e) {
            System.err.println("Error extracting percentage: " + e.getMessage());
        }
        return -1;
    }
    
    
}
