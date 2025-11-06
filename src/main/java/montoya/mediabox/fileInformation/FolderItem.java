package montoya.mediabox.fileInformation;

import java.io.File;

/**
 * Muestra el nombre de directorio en JList pero sin mostrar la ruta completa
 * 
 * @author Nerea
 */
public class FolderItem {

    private String fullPath;

    public FolderItem(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getFullPath() {
        return fullPath;
    }

    @Override
    public String toString() {
        return new File(fullPath).getName();
    }
}
