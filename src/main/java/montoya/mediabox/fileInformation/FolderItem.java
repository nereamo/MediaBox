package montoya.mediabox.fileInformation;

import java.io.File;

/**
 * Muestra el nombre de directorio en JList pero sin mostrar la ruta completa
 * 
 * @author Nerea
 */
public class FolderItem {

    private String fullPath;
    private boolean isNetwork;

    public FolderItem(String fullPath, boolean isNetwork) {
        this.fullPath = fullPath;
        this.isNetwork = isNetwork;
    }

    public String getFullPath() {
        return fullPath;
    }

    public boolean isIsNetwork() {
        return isNetwork;
    }

    @Override
    public String toString() {
        return new File(fullPath).getName();
    }
}
