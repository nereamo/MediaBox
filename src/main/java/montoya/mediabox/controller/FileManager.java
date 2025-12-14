package montoya.mediabox.controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import montoya.mediabox.fileInformation.DirectoryInformation;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;
import montoya.mediapollingcomponent.MediaPollingComponent;
import montoya.mediapollingcomponent.apiclient.Media;

/**
 *
 * @author Nerea
 */
public class FileManager {
    
    private DirectoryInformation directoryInfo;
    private TypeFilter typeFilter;
    private MediaPollingComponent mediaPollingComponent;
    
    public FileManager(DirectoryInformation directoryInfo,TypeFilter typeFilter, MediaPollingComponent mediaPollingComponent){
        
        this.directoryInfo = directoryInfo;
        this.typeFilter = typeFilter;
        this.mediaPollingComponent = mediaPollingComponent;
    }
    
    
    public List<FileInformation> getBothFiles(String filter) {
        List<FileInformation> bothFiles = new ArrayList<>();

        List<FileInformation> localFiles = typeFilter.filterByType(directoryInfo.fileList, filter);

        List<FileInformation> networkFiles = getNetworkFiles(filter);
        for (FileInformation netFi : networkFiles) {
            for (FileInformation localFi : localFiles) {
                if (netFi.name.equalsIgnoreCase(localFi.name)) {
                    bothFiles.add(localFi);
                }
            }
        }

        return bothFiles;
    }
    
    public List<FileInformation> getNetworkFiles(String filter) {
        List<FileInformation> networkFiles = new ArrayList<>();
        String token = mediaPollingComponent.getToken();
        String apiUrl = mediaPollingComponent.getApiUrl();

        if (token == null || token.isBlank() || apiUrl == null || apiUrl.isBlank()) {
            System.err.println("Cannot fetch network files: token or API URL is missing");
            return networkFiles;
        }

        try {
            // Obtener todos los medios de la API
            List<Media> mediaList = mediaPollingComponent.getAllMedia(token);

            // Convertir a FileInformation
            for (Media m : mediaList) {
                FileInformation fi = new FileInformation(m.mediaFileName, 0, m.mediaMimeType, null, "API FILES");
                networkFiles.add(fi);
            }

            // Aplicar el mismo filtro por tipo que usamos para los locales
            networkFiles = typeFilter.filterByType(networkFiles, filter);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error retrieving network files:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return networkFiles;
    }
    
    public List<FileInformation> getLocalFiles(String folderPath, String filter) {
        List<FileInformation> localFiles = typeFilter.filterByDirectory(directoryInfo.fileList, folderPath);

        return typeFilter.filterByType(localFiles, filter);
    }
    
    public void refreshFiles(FileProperties fileProperties){
        this.directoryInfo = fileProperties.loadDownloads();
    }
}
