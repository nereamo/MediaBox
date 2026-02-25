package montoya.mediabox.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import montoya.mediabox.fileInformation.DirectoryInformation;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediapollingcomponent.MediaPollingComponent;
import montoya.mediapollingcomponent.apiclient.Media;

/**
 * Gestiona las operaciones sobre los archivos mostrados en la aplicación.
 * <p>
 * Permite:
 * <ul> 
 * <li> Obtener archivos locales, remotos y mixtos.</li>
 * <li> Filtrar por directorio y tipo de archivo.</li>
 * <li> Descarga y subida de archivos.</li>
 * <li> Eliminar archivos locales.</li>
 * <li> Reproducir archivos locales.</li>
 * </ul> 
 * 
 * @author Nerea
 */
public class FileManager {
    
    /** {@link DirectoryInformation} Proporciona información del directorio y sus descargas. */
    private DirectoryInformation directoryInfo;
    
    /** {@link TypeFilter} Permite aplicar el filtro por tipo de archivo y por directorio. */
    private TypeFilter typeFilter;
    
    /** {@link MediaPollingComponent} Listener que notifica nuevos medios en la API. */
    private MediaPollingComponent mediaPollingComponent;
    
    /** {@link FileProperties} Gestiona las propiedades de los archivos descargados. */
    private FileProperties fileProperties;
    
    /**
     * Constructor que inicializa un objeto {@link FileManager}
     * 
     * @param directoryInfo Proporciona información de los archivos locales
     * @param typeFilter Aplica filtro por directorio y tipo de archivo
     * @param mediaPollingComponent Destiona la comunicación entre API y aplicación
     * @param fileProperties Gestiona las propiedades de los archivos
     */
    public FileManager(DirectoryInformation directoryInfo,TypeFilter typeFilter, MediaPollingComponent mediaPollingComponent, FileProperties fileProperties){
        
        this.directoryInfo = directoryInfo;
        this.typeFilter = typeFilter;
        this.mediaPollingComponent = mediaPollingComponent;
        this.fileProperties = fileProperties;
    }
    
    /**
     * Obtiene los archivos locales pertenecientes al directorio seleccionado
     * 
     * @param folderPath Ruta del directorio seleccionado
     * @param filter Filtro de tipo de archivo
     * @return Devuelve la lista de archivos que coincidad con directorio y filtro
     */
    public List<FileInformation> getLocalFiles(String folderPath, String filter) {
        List<FileInformation> localFiles = typeFilter.filterByDirectory(directoryInfo.getFileList(), folderPath);

        return typeFilter.filterByType(localFiles, filter);
    }
    
    
    /**
     * Obtiene los archivos presentes en la API y local.
     * 
     * @param filter Filtro de tipo de archivo
     * @return Devuelve la lista de archivos que están presentes en ambos lados
     */
    public List<FileInformation> getBothFiles(String filter) {
        List<FileInformation> bothFiles = new ArrayList<>();

        //Obtener archivos locales
        List<FileInformation> localFiles = typeFilter.filterByType(directoryInfo.getFileList(), filter);

        //Obtener archivos de API
        List<FileInformation> networkFiles = getNetworkFiles(filter);
        
        //Compara las dos listas en busca de la concidencia
        for (FileInformation netFi : networkFiles) {
            for (FileInformation localFi : localFiles) {
                if (netFi.getName().equalsIgnoreCase(localFi.getName())) {
                    bothFiles.add(localFi);
                }
            }
        }

        return bothFiles;
    }
    
    /**
     * Obtiene los archivos presentes en la API
     * 
     * @param filter Filtro de tipo de archivo
     * @return Devuelve la lista de archivos que están presentes en la API
     */
    public List<FileInformation> getNetworkFiles(String filter) {
        List<FileInformation> networkFiles = new ArrayList<>();
        String token = mediaPollingComponent.getToken();
        String apiUrl = mediaPollingComponent.getApiUrl();

        if (token == null || token.isBlank() || apiUrl == null || apiUrl.isBlank()) {
            System.err.println("Cannot fetch network files: token or API URL is missing");
            return networkFiles;
        }

        try {
            //Obtener archivos API
            List<Media> mediaList = mediaPollingComponent.getAllMedia(token);

            //Convertir Media en FileInformation
            for (Media m : mediaList) {
                FileInformation fi = new FileInformation(m.mediaFileName, 0, m.mediaMimeType, null, "API FILES");
                networkFiles.add(fi);
            }

            //Aplicar filtro
            networkFiles = typeFilter.filterByType(networkFiles, filter);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error retrieving network files:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return networkFiles;
    }
    
    /**
     * Permite reproducir el archivo seleccionado en la tabla
     * 
     * @param file Archivo que se va a reproducir
     * @throws IOException Exception si el archivo no existe o no se puede reproducir
     */
    public void openLocalFile(FileInformation  file) throws IOException{
        
        File f = new File(file.getFolderPath(), file.getName());
        if(!f.exists()){
            throw new IOException("File not found: " + file.getName());
        }
        Desktop.getDesktop().open(f);
    }
    
    /**
     * Elimina fisicamente el archivo seleccionado en la tabla y actualiza la información de las descargas
     * 
     * @param fileInfo Información del archivo a eliminar
     */
    public void deleteLocalFile(FileInformation fileInfo) {
        fileProperties.deleteDownload(fileInfo, directoryInfo.getFileList(), directoryInfo.getFolderPaths());
        refreshFiles();
    }
    
    /**
     * Descarga un archivo de la API y lo guarda en la carpeta de distino elegida por el usuario
     *
     * @param fileInfo Información del archivo descargado
     * @param folder Directorio destino de la descarga
     * @throws Exception si el archivo no existe o no se puede descargar
     */
    public void downloadFileFromApi(FileInformation fileInfo, File folder) throws Exception {
        List<Media> mediaList = mediaPollingComponent.getAllMedia(mediaPollingComponent.getToken());

        // Buscar archivo por nombre usando bucle normal
        Media mediaFile = null;
        for (Media m : mediaList) {
            if (m.mediaFileName.equals(fileInfo.getName())) {
                mediaFile = m;
                break;
            }
        }
        if (mediaFile == null) {
            throw new Exception("Cannot find media in API");
        }

        File file = new File(folder, fileInfo.getName()); //Crear objeto File

        mediaPollingComponent.download(mediaFile.id, file, mediaPollingComponent.getToken()); //Descarga archivo de API a local

        //Crea objeto FileInformation para registrar la descarga
        FileInformation newFile = new FileInformation(mediaFile.mediaFileName, file.length(),
                mediaFile.mediaMimeType, LocalDate.now().toString(), folder.getAbsolutePath());

        fileProperties.addDownload(newFile); //Añade descarga a archivos locales
        
        this.directoryInfo = fileProperties.loadDownloads(); //Actualiza información de descargas
    }

    /**
     * Reliza la subida del archivo local a la API
     * 
     * @param file Archivo local que se va a subir
     * @throws Exception si el archivo no existe o no se puede subir
     */
    public void uploadFileToApi(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new Exception("File not found");
        }
        String mimeType = Files.probeContentType(file.toPath());
        mediaPollingComponent.uploadFileMultipart(file, mimeType, mediaPollingComponent.getToken());
        mediaPollingComponent.setLastChecked(OffsetDateTime.now().minusMinutes(1).toString());
        refreshFiles(); // Actualizamos los datos internos
    }
    
    
    /**
     * Actualiza los archivos descargados almacenados localmente
     */
    public void refreshFiles(){
        this.directoryInfo = fileProperties.loadDownloads();
    }
}
