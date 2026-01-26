package montoya.mediabox.fileInformation;

import java.io.Serializable;

/**
 * Implementa serializable para permitir alamcenar los datos en archivo .json 
 * Contiene la información del archivo (nombre, tamaño, tipo y duración)
 *
 * @author Nerea
 */
public class FileInformation implements Serializable {

    private String name;
    private long size;
    private String type;
    private String date;
    private String folderPath;

    public FileInformation(String name, long size, String type, String date, String folderPath) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.date = date;
        this.folderPath = folderPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
    
    
}
