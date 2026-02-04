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
 * Clase encargada de gestionar los archivos mostrados en la tabla.
 * Muestra archivos de API, locales y que esten en ambos sitios a la vez.
 * @author Nerea
 */
public class FileManager {
    
    private DirectoryInformation directoryInfo;
    private TypeFilter typeFilter;
    private MediaPollingComponent mediaPollingComponent;
    private FileProperties fileProperties;
    
    public FileManager(DirectoryInformation directoryInfo,TypeFilter typeFilter, MediaPollingComponent mediaPollingComponent, FileProperties fileProperties){
        
        this.directoryInfo = directoryInfo;
        this.typeFilter = typeFilter;
        this.mediaPollingComponent = mediaPollingComponent;
        this.fileProperties = fileProperties;
    }
    
    //Obtiene los archivos locales dependiendo del directorio seleccionado
    public List<FileInformation> getLocalFiles(String folderPath, String filter) {
        List<FileInformation> localFiles = typeFilter.filterByDirectory(directoryInfo.getFileList(), folderPath);

        return typeFilter.filterByType(localFiles, filter);
    }
    
    
    //Obtiene los archivos de API y local
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
    
    //Obtiene los archivos de API
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

            //Convertir Medi en FileInformation
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
    
    public void playFile(FileInformation  file) throws IOException{
        
        File f = new File(file.getFolderPath(), file.getName());
        if(!f.exists()){
            throw new IOException("File not found: " + file.getName());
        }
        Desktop.getDesktop().open(f);
    }
    
    public void deleteFile(FileInformation fileInfo) {
        fileProperties.deleteDownload(fileInfo, directoryInfo.getFileList(), directoryInfo.getFolderPaths());
    }
    
    public void downloadFile(FileInformation fileInfo, File folder) throws Exception {
        List<Media> mediaList = mediaPollingComponent.getAllMedia(mediaPollingComponent.getToken());
        Media mediaFile = mediaList.stream()
                .filter(m -> m.mediaFileName.equals(fileInfo.getName()))
                .findFirst()
                .orElseThrow(() -> new Exception("Cannot find media in API"));

        File file = new File(folder, fileInfo.getName());
        mediaPollingComponent.download(mediaFile.id, file, mediaPollingComponent.getToken());

        FileInformation newFile = new FileInformation(mediaFile.mediaFileName, file.length(),
                mediaFile.mediaMimeType, LocalDate.now().toString(), folder.getAbsolutePath());

        fileProperties.addDownload(newFile);
        
        this.directoryInfo = fileProperties.loadDownloads();
    }

    public void uploadFile(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new Exception("File not found");
        }
        String mimeType = Files.probeContentType(file.toPath());
        mediaPollingComponent.uploadFileMultipart(file, mimeType, mediaPollingComponent.getToken());
        mediaPollingComponent.setLastChecked(OffsetDateTime.now().minusMinutes(1).toString());
        refreshFiles(); // Actualizamos los datos internos
    }
    
    
    //Refresca la lista de archivos
    public void refreshFiles(){
        this.directoryInfo = fileProperties.loadDownloads();
    }

}
