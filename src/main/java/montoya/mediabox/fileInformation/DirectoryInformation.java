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
    
    private List<FileInformation> fileList;
    private Set<String> folderPaths;
    
    public DirectoryInformation(List<FileInformation> fileList, Set<String> folderPaths) {
        this.fileList = fileList;
        this.folderPaths = folderPaths;
    }  

    public List<FileInformation> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileInformation> fileList) {
        this.fileList = fileList;
    }

    public Set<String> getFolderPaths() {
        return folderPaths;
    }

    public void setFolderPaths(Set<String> folderPaths) {
        this.folderPaths = folderPaths;
    }
    
    
}
