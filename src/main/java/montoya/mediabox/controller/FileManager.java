package montoya.mediabox.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import montoya.mediabox.fileInformation.DirectoryInformation;
import montoya.mediabox.fileInformation.FileInformation;
import montoya.mediabox.fileInformation.FileProperties;

/**
 * Gestiona las operaciones sobre los archivos mostrados en la aplicación.
 * <p> Permite:
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
//    private MediaPollingComponent mediaPollingComponent;
    
    /** {@link FileProperties} Gestiona las propiedades de los archivos descargados. */
    private FileProperties fileProperties;
    
    /**
     * Constructor que inicializa un objeto {@link FileManager}
     * 
     * @param directoryInfo Proporciona información de los archivos locales
     * @param typeFilter Aplica filtro por directorio y tipo de archivo
     * @param fileProperties Gestiona las propiedades de los archivos
     */
    public FileManager(DirectoryInformation directoryInfo,TypeFilter typeFilter, FileProperties fileProperties){
        
        this.directoryInfo = directoryInfo;
        this.typeFilter = typeFilter;
        this.fileProperties = fileProperties;
    }
    
    /**
     * Obtiene los archivos locales pertenecientes al directorio seleccionado
     * 
     * @param folderPath Ruta del directorio seleccionado
     * @param filter Filtro de tipo de archivo
     * @return Devuelve la lista de archivos que coincidad con directorio y filtro
     */
    public Set<FileInformation> getLocalFiles(String folderPath, String filter) {
        Set<FileInformation> localFiles = typeFilter.filterByDirectory(directoryInfo.getFileList(), folderPath);

        return typeFilter.filterByType(localFiles, filter);
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
        reloadDirectoryInfo();
    }
    
    /**
     * Actualiza los archivos descargados almacenados localmente
     */
    public void reloadDirectoryInfo(){
        this.directoryInfo = fileProperties.loadDownloads();
    }
}
