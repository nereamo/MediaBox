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
 * Clase encargada de gestionar los archivos mostrados en la tabla.
 * Muestra archivos de API, locales y que esten en ambos sitios a la vez.
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
    
    //Obtiene los archivos de API y local
    public List<FileInformation> getBothFiles(String filter) {
        List<FileInformation> bothFiles = new ArrayList<>();

        //Obtener archivos locales
        List<FileInformation> localFiles = typeFilter.filterByType(directoryInfo.fileList, filter);

        //Obtener archivos de API
        List<FileInformation> networkFiles = getNetworkFiles(filter);
        
        //Compara las dos listas en busca de la concidencia
        for (FileInformation netFi : networkFiles) {
            for (FileInformation localFi : localFiles) {
                if (netFi.name.equalsIgnoreCase(localFi.name)) {
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
    
    //Obtiene los archivos locales dependiendo del directorio seleccionado
    public List<FileInformation> getLocalFiles(String folderPath, String filter) {
        List<FileInformation> localFiles = typeFilter.filterByDirectory(directoryInfo.fileList, folderPath);

        return typeFilter.filterByType(localFiles, filter);
    }
    
    //Refresca la lista de archivos
    public void refreshFiles(FileProperties fileProperties){
        this.directoryInfo = fileProperties.loadDownloads();
    }
}
