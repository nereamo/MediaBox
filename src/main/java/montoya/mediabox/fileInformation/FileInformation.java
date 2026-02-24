package montoya.mediabox.fileInformation;

import java.io.Serializable;

/**
 * Implementa serializable para permitir almacenar los datos en archivo .json 
 * <p>
 * Propiedades del archivo:
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

    /** Variables genéricas */
    private String name;
    private long size;
    private String type;
    private String date;
    private String folderPath;

    /**
     * Constructor que inicializa un objeto FileInformation
     * 
     * @param name Nombre del archivo
     * @param size Tamaño en MB/s
     * @param type MymeTipe
     * @param date Fecha de descarga
     * @param folderPath Directorio de ubicación de la descarga
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
