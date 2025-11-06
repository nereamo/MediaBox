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
    
    public List<FileInformation> downloads;
    public Set<String> downloadFolders;
    
    public DirectoryInformation(List<FileInformation> downloads, Set<String> downloadFolders) {
        this.downloads = downloads;
        this.downloadFolders = downloadFolders;
    }  
}
