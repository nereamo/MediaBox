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
    
    /** Indica si es directorio de red */
    private boolean isNetwork;
    
    /** Indica si el directorio es mixto (local y red) */
    private boolean isBoth;

    /**
     * Constructor que crea un nuevo directorio dependiendo su hubicacion.
     * 
     * @param fullPath Ruta completa del directorio
     * @param isNetwork True si es el directorio de red
     * @param isBoth True si es el directorio mixto
     */
    public FolderItem(String fullPath, boolean isNetwork, boolean isBoth) {
        this.fullPath = fullPath;
        this.isNetwork = isNetwork;
        this.isBoth = isBoth;
    }

    /** @return Ruta completa */
    public String getFullPath() {
        return fullPath;
    }

    /** @return True si es directorio de red */
    public boolean isIsNetwork() {
        return isNetwork;
    }

    /** @return True si es directorio mixto */
    public boolean isIsBoth() {
        return isBoth;
    }

    /** @return Nombre del directorio */
    @Override
    public String toString() {
        return new File(fullPath).getName();
    }
}
