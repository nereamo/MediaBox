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
    private boolean isBoth;

    public FolderItem(String fullPath, boolean isNetwork, boolean isBoth) {
        this.fullPath = fullPath;
        this.isNetwork = isNetwork;
        this.isBoth = isBoth;
    }

    public String getFullPath() {
        return fullPath;
    }

    public boolean isIsNetwork() {
        return isNetwork;
    }

    public boolean isIsBoth() {
        return isBoth;
    }

    @Override
    public String toString() {
        return new File(fullPath).getName();
    }
}
