package montoya.mediabox.fileInformation;

import java.io.Serializable;

/**
 * Implementa {@link serializable} para permitir almacenar la información del archivo descargado en un JSON.
 * <p> Propiedades del archivo:
 * <ul>
 * <li> Nombre </li>
 * <li> Tamaño </li>
 * <li> Tipo </li>
 * <li> Duración </li>
 * </ul>
 *
 * @author Nerea
 */
public class FileInformation implements Serializable {

    /** Nombre del archivo. */
    private String name;
    
    /** Tamaño del archivo en bytes. */
    private long size;
    
    /** Tipo MIME del archivo. */
    private String type;
    
    /** Fecha de descarga. */
    private String date;
    
    /** Ruta del directorio donde se encuentra el archivo. */
    private String folderPath;

    /**
     * Constructor que inicializa un objeto FileInformation
     * 
     * @param name Nombre del archivo
     * @param size Tamaño del archivo en bytes
     * @param type Tipo MIME del archivo
     * @param date Fecha de descarga
     * @param folderPath Directorio donde se encuentra el archivo
     */
    public FileInformation(String name, long size, String type, String date, String folderPath) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.date = date;
        this.folderPath = folderPath;
    }

    /** @return Nombre del archivo */
    public String getName() {
        return name;
    }

    /** @param name Almacena nombre */
    public void setName(String name) {
        this.name = name;
    }

    /** @return Tamaño del archivo */
    public long getSize() {
        return size;
    }

    /** @param size Almacena el tamaño */
    public void setSize(long size) {
        this.size = size;
    }

    /** @return Tipo de archivo */
    public String getType() {
        return type;
    }

    /** @param type Almacena el tipo */
    public void setType(String type) {
        this.type = type;
    }

    /** @return Fecha de la descarga */
    public String getDate() {
        return date;
    }

    /** @param date Almacena la fecha */
    public void setDate(String date) {
        this.date = date;
    }

    /** @return Directorio del archivo */
    public String getFolderPath() {
        return folderPath;
    }

    /** @param folderPath Almacena el directorio de ubicación */
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
}
