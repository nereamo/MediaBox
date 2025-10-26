package montoya.mediabox.download;

import java.io.*;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Nerea
 */

//SwingWorker ejecuta tareas en segundo plano impidiendo que no se bloquee la GUI
public class DownloadWorker extends SwingWorker<Void, String>{
    
    private final ProcessBuilder pb;
    private final String folder;
    private final JTextArea outputArea;
    private final JProgressBar progressBar;
    private File lastDownloadedFile;
    
    public DownloadWorker(ProcessBuilder pb, String folder, JTextArea outputArea, JProgressBar progressBar) {
        this.pb = pb;
        this.folder = folder;
        this.outputArea = outputArea;
        this.progressBar = progressBar;
    }
    
    public File getLastDownloadedFile() {
        return lastDownloadedFile;
    }

    //Metodo principal para ejecucion en segundo plano
    @Override
    protected Void doInBackground() throws Exception {
        Process p = pb.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                publish(line);

                if (line.contains("[download]") && line.contains("%")) {
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
                lastDownloadedFile = latest;
            }
        }

        p.destroy();
        return null;
    }

    //Opcional --> Ejecuta thread, recibe datos de publish() y mustra txt en JTestArea
    @Override
    protected void process(List<String> chunks) {
        for (String line : chunks) {
            outputArea.append(line + "\n");
        }
    }

    //Metodo llamado cuando doInBackground termina
    @Override
    protected void done() {
        progressBar.setIndeterminate(false);
        progressBar.setValue(100);
        progressBar.setString("Download completed!");
        JOptionPane.showMessageDialog(null, "Download completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    //Extrae el % de la descarga
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
