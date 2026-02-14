package montoya.mediabox.download;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.panels.Downloads;
import montoya.mediabox.panels.InfoMedia;

/**
 * Contiene los parametros necesarios para la configuracion de la descarga. Se usa junto con {@link DownloadWorker}.
 *
 * @author Nerea
 */
public class DownloadManager {

    private DownloadWorker downloadWorker;
    private FileProperties fileProperties;
    private InfoMedia infoMedia;

    private String tempPath;
    private String ytDlpLocation;
    private boolean createM3u;
    private double maxSpeed;

    public DownloadManager() {
    }

    public DownloadManager(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

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

    //Devuelve el ultimo archivo descargado
    public File getLastDownloadedFile() {
        if (downloadWorker != null) {
            return downloadWorker.getLastDownloadedFile();
        }
        return null;
    }

    //Verifica si los campos de preferences se han completado
    public void download(String url, String folder, String format, String quality, JProgressBar progressBar, FileTableModel tblModel, Set<String> folderPaths, Downloads downloadsPanel) {
        if (url.isEmpty() || folder.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a URL and select a folder.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ytDlpLocation == null || ytDlpLocation.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please configure the yt-dlp location in Preferences.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProcessBuilder pb = buildCommand(url, folder, format, quality);

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);

        downloadWorker = new DownloadWorker(pb, folder, progressBar, tblModel, fileProperties, folderPaths, infoMedia, downloadsPanel);
        DownloadWorker task = downloadWorker;

        task.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                int percent = (Integer) evt.getNewValue();
                updateProgressBar(progressBar, percent);
            }
        });
        
        progressBar.setValue(0);
        task.execute();
    }

    //Devuelve ProcessBuilder con configuración para yt-dlp
    private ProcessBuilder buildCommand(String url, String folder, String format, String quality) {
        List<String> cmd = new ArrayList<>();

        cmd.add(ytDlpLocation);
        cmd.add("-o");
        cmd.add(folder + File.separator + "%(title)s.%(ext)s"); //Añade a carpeta el archivo con nombre

        if ("mp3".equals(format) || "wav".equals(format) || "m4a".equals(format)) {
            cmd.add("-o");
            cmd.add(folder + File.separator + "%(title)s_audio.%(ext)s");
        } else {
            cmd.add("-o");
            cmd.add(folder + File.separator + "%(title)s.%(ext)s");
        }

        String qualityFilter = "";

        //Ajustar calidad de video
        switch (quality) {
            case "1080":
                qualityFilter = "[height<=1080]";
                break;
            case "720":
                qualityFilter = "[height<=720]";
                break;
            case "480":
                qualityFilter = "[height<=480]";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown quality selected: " + quality, "Error", JOptionPane.ERROR_MESSAGE);
                return null;
        }

        //Ajustar descarga según formato
        switch (format) {
            //Audio
            case "mp3":
                cmd.add("-x");
                cmd.add("--audio-format");
                cmd.add("mp3");
                break;
            case "wav":
                cmd.add("-x");
                cmd.add("--audio-format");
                cmd.add("wav");
                break;
            case "m4a":
                cmd.add("-x");
                cmd.add("--audio-format");
                cmd.add("m4a");
                break;
            //Video
            case "mp4":
                cmd.add("-f");
                cmd.add("bestvideo" + qualityFilter + "+bestaudio[ext=m4a]/mp4");
                cmd.add("--merge-output-format");
                cmd.add("mp4");
                break;
            case "mkv":
                cmd.add("-f");
                cmd.add("bestvideo" + qualityFilter + "+bestaudio/best");
                cmd.add("--merge-output-format");
                cmd.add("mkv");
                break;
            case "webm":
                cmd.add("-f");
                cmd.add("bestvideo[ext=webm]" + qualityFilter + "+bestaudio[ext=webm]/webm");
                cmd.add("--merge-output-format");
                cmd.add("webm");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown format selected: " + format, "Error", JOptionPane.ERROR_MESSAGE);
                return null;
        }

        //Archivo .m3u
        if (createM3u) {
            cmd.add("--write-info-json");
        }

        //Limite velocidad
        if (maxSpeed > 0) {
            cmd.add("--limit-rate");
            cmd.add(maxSpeed + "M");
        }

        cmd.add(url); //url del archivo

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        return pb;
    }

    //Actualiza visualmente la barra de progresso
    private void updateProgressBar(JProgressBar progressBar, int percent) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setIndeterminate(false);
                progressBar.setValue(percent);
                progressBar.setString("Downloading... " + percent + "%");
            }
        });
    }

    public void setInfoMedia(InfoMedia infoMedia) {
        this.infoMedia = infoMedia;
    }
}
