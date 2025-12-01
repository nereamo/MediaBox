package montoya.mediabox.fileInformation;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Permite guardar el directorio donde se ha realizado la descarga.
 * Se usa junto con {@link FileInformation}.
 * 
 * @author Nerea
 */
public class DirectoryInformation implements Serializable{
    
    public List<FileInformation> fileList;
    public Set<String> folderPaths;
    
    public DirectoryInformation(List<FileInformation> fileList, Set<String> folderPaths) {
        this.fileList = fileList;
        this.folderPaths = folderPaths;
    }  
}
