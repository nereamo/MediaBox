package montoya.mediabox.download;

import java.io.File;
import java.util.Set;
import javax.swing.*;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.fileInformation.FolderItem;

/**
 * Contiene los parametros necesarios para la configuracion de la descarga.
 * Se usa junto con {@link DownloadWorker}.
 *
 * @author Nerea
 */
public class DownloadManager {

    private String tempPath;
    private String ytDlpLocation;
    private boolean createM3u;
    private double maxSpeed;
    private DownloadWorker dw;
    private FileProperties fProp;

    public DownloadManager() {
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
        if (dw != null) {
            return dw.getLastDownloadedFile();
        }
        return null;
    }

    public DownloadManager(FileProperties fProp) {
        this.fProp = fProp;
    }

    //Verifica si los campos de preferences se han completado
    public void download(String url, String folder, String format, JTextArea outputArea, JProgressBar progressBar, FileTableModel model, JList<FolderItem> lstDownloads, Set<String> directoriosDescarga) {
        if (url.isEmpty() || folder.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a URL and select a folder.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ytDlpLocation == null || ytDlpLocation.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please configure the yt-dlp location in Preferences.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProcessBuilder pb = buildCommand(url, folder, format);

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);

        dw = new DownloadWorker(pb, folder, outputArea, progressBar, model, fProp, lstDownloads, directoriosDescarga);
        DownloadWorker task = dw;

        task.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                int percent = (Integer) evt.getNewValue();
                updateProgressBar(progressBar, percent);
            }
        });

        task.execute();
    }

    //Instrucciones de descarga dependiendo del formato seleccioando
    private ProcessBuilder buildCommand(String url, String folder, String format) {
        ProcessBuilder pb;
        if ("mp3".equals(format)) {
            pb = new ProcessBuilder(
                    ytDlpLocation,
                    "-x", "--audio-format", "mp3", "-o", folder + File.separator + "%(title)s.%(ext)s", url);

        } else {
            pb = new ProcessBuilder(
                    ytDlpLocation,
                    "-f", "mp4", "-o", folder + File.separator + "%(title)s.%(ext)s", url);
        }

        //Archivo .m3u
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

//        SwingUtilities.invokeLater(() -> {    --> lambda
//            progressBar.setIndeterminate(false);
//            progressBar.setValue(percent);
//            progressBar.setString("Downloading... " + percent + "%");
//        });
    }
}
