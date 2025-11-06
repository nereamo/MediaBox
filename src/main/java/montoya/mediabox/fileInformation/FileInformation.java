package montoya.mediabox.fileInformation;

import java.io.Serializable;

/**
 * Implementa serializable para permitir alamcenar los datos en archivo .json 
 * Contiene la información del archivo (nombre, tamaño, tipo y duración)
 *
 * @author Nerea
 */
public class FileInformation implements Serializable {

    public String name;
    public long size;
    public String type;
    public String date;
    public String folderPath;

    public FileInformation(String name, long size, String type, String date, String folderPath) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.date = date;
        this.folderPath = folderPath;
    }
}
