package montoya.mediabox.fileInformation;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Representa un directorio cons sus archivos descargados.
 * <p> Contiene una lista de {@link FileInformation} y permite obtener las rutas de los directorios.
 * 
 * @author Nerea
 */
public class DirectoryInformation implements Serializable{
    
    /** Lista de archivos descargados en un directorio */
    private List<FileInformation> fileList;
    
    /** Rutas de los directorios que almacenan descargas */
    private Set<String> folderPaths;
    
    /**
     * Constructor que inicializa la lista de archivos y la colección de directorios.
     * 
     * @param fileList Lista de archivos descargados
     * @param folderPaths Colección de rutas de los directorios
     */
    public DirectoryInformation(List<FileInformation> fileList, Set<String> folderPaths) {
        this.fileList = fileList;
        this.folderPaths = folderPaths;
    }  

    /** @return Lista de archivos */
    public List<FileInformation> getFileList() {
        return fileList;
    }

    /** @param fileList Almacena una lista de archivos */
    public void setFileList(List<FileInformation> fileList) {
        this.fileList = fileList;
    }

    /** @return Colección de directorios */
    public Set<String> getFolderPaths() {
        return folderPaths;
    }

    /** @param folderPaths Almacena una colección de directorios */
    public void setFolderPaths(Set<String> folderPaths) {
        this.folderPaths = folderPaths;
    }
}
