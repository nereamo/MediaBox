package montoya.mediabox.download;

import java.io.*;
import javax.swing.*;
import java.util.Date;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediabox.fileInformation.FileTableModel;
import montoya.mediabox.panels.Downloads;
import montoya.mediabox.panels.InfoMedia;

/**
 * Clase creada con ayuda de Copilot para entender como funciona SwingWorker y que metodos utilizar. 
 * <p> SwingWorker ejecuta tareas en segundo plano impidiendo que no se bloquee la GUI. 
 *
 * @author Nerea
 */
public class DownloadWorker extends SwingWorker<Void, String> {

    /** {@link File} Último archivo descargado */
    private File lastDownloadFile;
    
    /** {@link FileTableModel} Modelo de tabla que muestra los archivos. */
    private final FileTableModel tblModel;
    
    /** {@link InfoMedia} Panel que contiene información de los archivos. */
    private InfoMedia infoMedia;
    
    /** {@link FileProperties} Gestiona las propiedades de los archivos descargados. */
    private final FileProperties fileProperties;
    
    /** {@link Downloads} Panel que permite realizar la descarga. */
    private final Downloads downloadsPanel;

    /** Proceso externo para realizar la descarga */
    private final ProcessBuilder pb;
    
    /** Directorio de la descarga */
    private final String folder;
    
    /** Barra de progreso asociada a la descarga. */
    private final JProgressBar barProgress;
    
    /**
     * Ejecuta un proceso externo (descarga) en segundo plano sin bloquear la interfaz gráfica.
     * 
     * @param pb Proceso configurado para ejecutar la descarga
     * @param folder Directorio donde gusrdar la descarga
     * @param progressBar Muestra el progreso de descarga
     * @param tblModel Modelo de tabla d elos archivos
     * @param fileProperties Objeto que gestiona las propiedades y de archivos descargados
     * @param infoMedia Panel (vista) que muestra la información d elas descargas
     * @param downloadsPanel Panel (vista) que permite realizar una descarga
     */
    public DownloadWorker(ProcessBuilder pb, String folder, JProgressBar progressBar, FileTableModel tblModel, FileProperties fileProperties, InfoMedia infoMedia, Downloads downloadsPanel) {
        this.pb = pb;
        this.folder = folder;
        this.barProgress = progressBar;
        this.tblModel = tblModel;
        this.fileProperties = fileProperties;
        this.infoMedia = infoMedia;
        this.downloadsPanel = downloadsPanel;
    }

    /** @return Última descarga */
    public File getLastDownloadedFile() {
        return lastDownloadFile;
    }

    /**
     * Ejecuta la descarga en segundo plano. Inicia barra de progreso, detecta el último archivo, registra la descarga en FileProperties y actualiza el panel de información
     *
     *
     * @return {@code null}, ya que el resultado no se utiliza
     * @throws Exception Si ocurre un error durante la ejecución del proceso
     */
    @Override
    protected Void doInBackground() throws Exception {

        boolean errorDetected = false;

        SwingUtilities.invokeLater(new Runnable() { //Hilo secundario para progressBar
            @Override
            public void run() {
                barProgress.setValue(0); //Reinicio del valor
                barProgress.setString(null); //Elimina el texto mostrado
                barProgress.setIndeterminate(false);
            }
        });

        pb.redirectErrorStream(true);
        Process p = pb.start(); //Ejecuta yt-dlp

        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"))) {

            String line;

            while ((line = br.readLine()) != null) { //Lee línea a línea la salida del proceso
                System.out.println(line);

                if (line.contains("ERROR")) { //Detecta errores de yt-dlp
                    errorDetected = true;
                }

                //Detecta el progreso
                if (line.contains("[download]") && line.contains("%")) {
                    int percent = extractPercentage(line); //Extrae el porcentaje
                    if (percent >= 0 && percent <= 100) {
                        setProgress(percent); //Notifica progreso al SwingWorker
                    }
                }
            }
        }

        int exitCode = p.waitFor();
        if (exitCode == 0 && !errorDetected) { //Verifica el resultado de la descarga

            File dir = new File(folder); //Directorio de la descarga
            File[] files = dir.listFiles(); //Lista de archivos del directorio

            if (files != null && files.length > 0) {
                File latest = files[0];

                for (File f : files) { //Busca el archivo más reciente
                    if (f.lastModified() > latest.lastModified()) {
                        latest = f;
                    }
                }

                lastDownloadFile = latest; //Guarda la referencia del último archivo
                FileInformation info = fileInfo(latest); //Crea objeto con metadatos

                fileProperties.addDownload(info); //Registra la descarga

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        infoMedia.refreshFiles();// Refrescar panel de información
                    }
                });
            }
        } else {
            throw new Exception("Download failed"); //Si falló la descarag lanza excepción
        }

        p.destroy(); //Libera recursos
        return null;
    }

    /** Se ejecuta automáticamente cuando la descarga en segundo plano ha finalizado */
    @Override
    protected void done() {

        try {
            //Si la descarga es correcta, muestra mensaje de confirmación, y barra de progresso al 100%
            get();
            barProgress.setIndeterminate(false);
            barProgress.setValue(100);
            barProgress.setString("DOWNLOAD COMPLETED!");

            SwingUtilities.invokeLater(new Runnable() { //Hilo secundario para progressBar
                @Override
                public void run() {
                    downloadsPanel.showOpenLastButton(); //Botón activado despues de la descarga
                }
            });

        } catch (Exception e) { //Si ocurre una excepción, devuelve mensaje informativo
            barProgress.setIndeterminate(false);
            barProgress.setValue(0);
            barProgress.setString("ERROR IN DOWNLOAD");
        }
    }

    /**
     * Extrae el porcentaje de la descarga permitiendo actualizar la barra de progreso.
     * 
     * @param line Texto mostrado con el progreso de descarga
     * @return Porcentaje del progreso de la descarga
     */
    private int extractPercentage(String line) {
        try {
            int start = line.indexOf("[download]") + 10; //Posición después de "[download]"
            int end = line.indexOf("%", start); //Posición del símbolo %
            if (end > start) {
                //Extrae el número, eliminando caracteres no numéricos
                String percent = line.substring(start, end).replaceAll("[^0-9.]", "").trim();
                return (int) Float.parseFloat(percent); //Confierte float a int
            }
        } catch (Exception e) {
            System.err.println("Error extracting percentage: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Crea objeto FileInformation con sus propiedades (nombre, tamaño, tipo, fecha y directorio).
     * 
     * @param file Archivo del cual se obtienen los datos
     * @return Objeto FileInformation con la información del archivo
     */
    private FileInformation fileInfo(File file) {
        String name = file.getName(); //Nombre del archivo
        long size = file.length(); //Tamaño en bytes
        String type = "unknown"; //Tipo MIME por defecto
        
        try {
            type = Files.probeContentType(file.toPath()); //Detecta el tipo MIME
        } catch (IOException e) {
            System.err.println("Error extracting MIME type: " + e.getMessage());
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified())); //Fecha de descarga
        String folderPath = file.getParent(); //Ruta del directorio
        
        return new FileInformation(name, size, type, date, folderPath);
    }
}
