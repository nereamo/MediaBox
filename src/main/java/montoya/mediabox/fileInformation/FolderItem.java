package montoya.mediabox.fileInformation;

import java.io.File;

/**
 * Permite diferenciar entre los directorios de red, locales y mixtos.
 * <p>Muestra solo el nombre no la ruta completa.
 * 
 * @author Nerea
 */
public class FolderItem {

    /** Ruta completa del directorio */
    private String fullPath;

    /**
     * Constructor que crea un nuevo directorio dependiendo su hubicacion.
     * 
     * @param fullPath Ruta completa del directorio
     */
//    public FolderItem(String fullPath, boolean isNetwork, boolean isBoth) {
    public FolderItem(String fullPath) {
        this.fullPath = fullPath;
    }

    /** @return Ruta completa */
    public String getFullPath() {
        return fullPath;
    }

    /** @return Nombre del directorio */
    @Override
    public String toString() {
        return new File(fullPath).getName();
    }
}
