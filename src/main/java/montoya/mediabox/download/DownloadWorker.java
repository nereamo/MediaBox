package montoya.mediabox.download;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;

/**
 * Clase creada con ayuda de Copilot para entender como funciona SwingWorker y que metodos utilizar.
 * SwingWorker ejecuta tareas en segundo plano impidiendo que no se bloquee la GUI.
 * Contiene metodos para descaragar archivos en segundo plano
 * @author Nerea
 */

public class DownloadWorker extends SwingWorker<Void, String>{
    
    private final ProcessBuilder pb;
    private final String folder;
    private final JTextArea outputArea;
    private final JProgressBar progressBar;
    private File lastDownloadedFile;
    private final FileTableModel model;
    private final JList<String> lstDownloads;
    private final FileProperties fProp;
    
    public DownloadWorker(ProcessBuilder pb, String folder, JTextArea outputArea, JProgressBar progressBar, FileTableModel model, FileProperties fProp, JList<String> lstDownloads) {
        this.pb = pb;
        this.folder = folder;
        this.outputArea = outputArea;
        this.progressBar = progressBar;
        this.model = model;
        this.fProp = fProp;
        this.lstDownloads = lstDownloads;
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
                File latest = files[0]; //Archivo mas reciente
                for (File f : files) {
                    if (f.lastModified() > latest.lastModified()) {
                        latest = f;
                    }
                }
                //Para la tabla
                lastDownloadedFile = latest;
                FileInformation info = fileInfo(lastDownloadedFile); //Crea objeto FileInfo con los datos
                //SwingUtilities.invokeLater(() -> model.addFile(info)); // --> lambda
                SwingUtilities.invokeLater(new Runnable() { //Añade el objeto a la tabla
                    @Override
                    public void run() {
                        model.addFile(info);
                        DefaultListModel<String> listModel = (DefaultListModel<String>) lstDownloads.getModel();
                        listModel.addElement(info.name);
                    }
                });
                fProp.guardarDescargas(model.getFileList()); //Guarda la lista de descargas en downloads.json
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
    
    //Extrae la informacion de la descarga para mostrarla en JTable
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
        
        return new FileInformation(name, size, type, date);
    }
}
