package montoya.mediabox.download;

import java.io.*;
import javax.swing.*;
import java.util.Set;
import java.util.Date;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.panels.Downloads;
import montoya.mediabox.panels.InfoMedia;

/**
 * Clase creada con ayuda de Copilot para entender como funciona SwingWorker y que metodos utilizar. SwingWorker ejecuta tareas en segundo plano impidiendo que no se bloquee la GUI. Contiene metodos para descaragar archivos en segundo plano
 *
 * @author Nerea
 */
public class DownloadWorker extends SwingWorker<Void, String> {

    private File lastDownloadFile;
    private final FileTableModel tblModel;
    private InfoMedia infoMedia;
    private final FileProperties fileProperties;
    private final Downloads downloadsPanel;

    private final ProcessBuilder pb;
    private final String folder;
    private final JProgressBar barProgress;
    private final Set<String> folderPaths;

    public DownloadWorker(ProcessBuilder pb, String folder, JProgressBar progressBar, FileTableModel tblModel, FileProperties fileProperties, Set<String> folderPaths, InfoMedia infoMedia, Downloads downloadsPanel) {
        this.pb = pb;
        this.folder = folder;
        this.barProgress = progressBar;
        this.tblModel = tblModel;
        this.fileProperties = fileProperties;
        this.folderPaths = folderPaths;
        this.infoMedia = infoMedia;
        this.downloadsPanel = downloadsPanel;
    }

    //Devuelve ultimo archivo descargado
    public File getLastDownloadedFile() {
        return lastDownloadFile;
    }

    //Metodo principal para ejecucion en segundo plano
    @Override
    protected Void doInBackground() throws Exception {

        SwingUtilities.invokeLater(new Runnable() { //Hilo secundario para progressBar
            @Override
            public void run() {
                barProgress.setValue(0);
                barProgress.setString(null);
                barProgress.setIndeterminate(true);
            }
        });

        Process p = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                publish(line); //Lineas mostradas en JTextArea con info de descarga

                if (line.contains("[download]") && line.contains("%")) { //Si la linea contiene % actualiza JProgressBar
                    int percent = extractPercentage(line);
                    if (percent >= 0 && percent <= 100) {
                        setProgress(percent);
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

                lastDownloadFile = latest;
                FileInformation info = fileInfo(latest);

                fileProperties.addDownload(info);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        infoMedia.refreshFiles();// Refrescar archivos
                    }
                });
            }
        }

        p.destroy();
        return null;
    }

//    //Recibe el texto enviado de publish y lo muestra en JTextArea
//    @Override
//    protected void process(List<String> chunks) {
//        for (String line : chunks) {
//            outputArea.append(line + "\n");
//        }
//    }

    //Se ejecuta cuando doInBackground ha terminado, muestra mensaje de finalización
    @Override
    protected void done() {

        try {
            get();
            barProgress.setIndeterminate(false);
            barProgress.setValue(100);
            barProgress.setString("Download completed!"); //Mensage si se completó la descarga correctamente

            SwingUtilities.invokeLater(new Runnable() { //Hilo secundario para progressBar
                @Override
                public void run() {
                    downloadsPanel.showOpenLastButton();
                }
            });
        
        
        } catch (Exception e) {
            barProgress.setIndeterminate(false);
            barProgress.setValue(0);
            barProgress.setString("Error in download"); //Mensage de error si hubo algún problema durante la descarga
        }
    }

    //Extrae el % de cada linea de la descarga
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

    //Crea un objeto FileInformation con los datos de la descarga
    private FileInformation fileInfo(File file) {
        String name = file.getName();
        long size = file.length();
        String type = "unknown";
        try {
            type = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            System.err.println("Error extracting MIME type: " + e.getMessage());
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified()));
        String folderPath = file.getParent();
        return new FileInformation(name, size, type, date, folderPath);
    }
}
