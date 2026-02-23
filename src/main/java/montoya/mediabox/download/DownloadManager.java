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
 * Gestiona la configuración y ejecución de descargas mediante la URL proporcionado por el usuario.
 * Uso de yt-dlp.
 * <p>
 * Define la siguiente información:
 * <ul>
 * <li> Ruta de archivos temporales </li>
 * <li> Ubicación del archivo yt-dlp.exe </li>
 * <li> Creación de listas .m3u </li>
 * <li> Definir la velocidad de descarga </li>
 * </ul>
 * Se usa junto con {@link DownloadWorker} para realizar la descarga y poder actualizar la interfaz.
 *
 * @author Nerea
 */
public class DownloadManager {

    /** {@link DownloadWorker} Ejecuta las descargas en segundo plano. */
    private DownloadWorker downloadWorker;
    
    /** {@link FileProperties} Gestiona las propiedades de los archivos descargados. */
    private FileProperties fileProperties;
    
    /** {@link FileProperties} Panel (vista) que muestra la información d elas descargas. */
    private InfoMedia infoMedia;

    /** Variables generales */
    private String tempPath; //Ruta descarga
    private String ytDlpLocation; //Ubicación archivo yt-dlp
    private boolean createM3u; //Crear archivo .m3u
    private double maxSpeed; //Velocidad de descarga

    /** Constructor por defecto*/
    public DownloadManager() {}

    /**
     * Constructor que inicializa FileProperties
     * 
     * @param fileProperties Objeto que gestiona las propiedades y de archivos descargados
     */
    public DownloadManager(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    /**
     * Obtiene ruta de archivos temporales
     * @return Ruta temporal
     */
    public String getTempPath() {
        return tempPath;
    }

    /**
     * Establece ruta de archivos temporales
     * @param tempPath Ruta temporal
     */
    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    /**
     * Obtiene la ubicación del archivo yt-dlp.exe
     * @return Ruta archivo yt-dlp.exe
     */
    public String getYtDlpLocation() {
        return ytDlpLocation;
    }

    /**
     * Establece la ubicación del archivo yt-dlp.exe
     * @param ytDlpLocation Ruta archivo yt-dlp.exe
     */
    public void setYtDlpLocation(String ytDlpLocation) {
        this.ytDlpLocation = ytDlpLocation;
    }

    /**
     * Indica si debe crearse la archivo .m3u
     * @return True para crear y False para no crear
     */
    public boolean isCreateM3u() {
        return createM3u;
    }

    /**
     * Configura se debe generar archivo .m3u
     * @param createM3u True para crear y False para no crear
     */
    public void setCreateM3u(boolean createM3u) {
        this.createM3u = createM3u;
    }

    /**
     * Obtiene la velocidad de descarga
     * @return Velocidad de descarga
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Establece velocidad de descarga
     * @param maxSpeed Velocidad descarga
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Obtener el último archivo descargado
     * @return Último archivo descargado si no es {@code null}
     */
    public File getLastDownloadedFile() {
        if (downloadWorker != null) {
            return downloadWorker.getLastDownloadedFile();
        }
        return null;
    }

    /**
     * Inicia descarga del archivo desde la URL proporcionada por el usuario
     * @param url Ruta del archivo a descargar
     * @param folder Directorio donde al macenar la descarga
     * @param type Establece el tipo de archivo
     * @param quality Calidad de video
     * @param progressBar Barra de progresso de la descarga
     * @param tblModel Tabla que contiene los archivos descargados
     * @param folderPaths Colección de directorios
     * @param downloadsPanel Panel donde se realizan las descargas
     */
    public void download(String url, String folder, String type, String quality, JProgressBar progressBar, FileTableModel tblModel, Set<String> folderPaths, Downloads downloadsPanel) {
        
        if (url.isEmpty() || folder.isEmpty()) { //Verificación de URL y directorio seleccionado
            JOptionPane.showMessageDialog(null, "Please enter a URL and select a folder.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ytDlpLocation == null || ytDlpLocation.isEmpty()) { //Varificación de archivo yt-dlp.exe configurado
            JOptionPane.showMessageDialog(null, "Please configure the yt-dlp location in Preferences.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProcessBuilder pb = buildCommand(url, folder, type, quality); //Comando para la ejecución de descarga

        progressBar.setVisible(true); //Barra de progreso
        progressBar.setIndeterminate(true);
        
        //Objeto DowloadWorker para ejecutar la descarga en segundo plano
        downloadWorker = new DownloadWorker(pb, folder, progressBar, tblModel, fileProperties, infoMedia, downloadsPanel);
        DownloadWorker task = downloadWorker;

//        //Objeto DowloadWorker para ejecutar la descarga en segundo plano
//        downloadWorker = new DownloadWorker(pb, folder, progressBar, tblModel, fileProperties, folderPaths, infoMedia, downloadsPanel);
//        DownloadWorker task = downloadWorker;

        //Actualiza barra de progreso con el porcentaje
        task.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                int percent = (Integer) evt.getNewValue();
                updateProgressBar(progressBar, percent);
            }
        });
        
        //Inicia barra de progreso y ejecuta el worker
        progressBar.setValue(0);
        task.execute();
    }

    /**
     * Configua el comando para realizar la descarga según las propiedades elegidas por el usuario
     * 
     * @param url Ruta del archivo a descargar
     * @param folder Directorio donde al macenar la descarga
     * @param type Establece el tipo de archivo
     * @param quality Calidad de video
     * @return Un {@link ProcessBuilder} configurado con los argumentos de yt-dlp, o null si los parámetros no son válidos
     */
    private ProcessBuilder buildCommand(String url, String folder, String type, String quality) {
        List<String> cmd = new ArrayList<>();

        cmd.add(ytDlpLocation);
        cmd.add("-o");
        cmd.add(folder + File.separator + "%(title)s.%(ext)s"); //Añade a carpeta el archivo con nombre

        if ("mp3".equals(type) || "wav".equals(type) || "m4a".equals(type)) {
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
        switch (type) {
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
                JOptionPane.showMessageDialog(null, "Unknown format selected: " + type, "Error", JOptionPane.ERROR_MESSAGE);
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

        //Proceso para ejecutar el comando de descarga
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        return pb;
    }

    /**
     * Actualiza la barra de progreso en función del avance de la descarga
     * 
     * @param progressBar Componente visaul del estado de la descarga
     * @param percent Valor de la descarga
     */
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

    /**
     * Asocia panel de información d edescargas para actualizar la vista
     * 
     * @param infoMedia Panel que muestra la información d elas descargas
     */
    public void setInfoMedia(InfoMedia infoMedia) {
        this.infoMedia = infoMedia;
    }
}
