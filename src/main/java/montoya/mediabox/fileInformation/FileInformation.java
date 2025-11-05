package montoya.mediabox.fileInformation;

import java.io.Serializable;

/**
 * Implementa serializable para permitir alamcenar los datos en archivo .json
 * @author Nerea
 */

public class FileInformation implements Serializable{
    
    public String name;
    public long size;
    public String type;
    public String date;
    
    public FileInformation(String name, long size, String type, String date){
        this.name = name;
        this.size = size;
        this.type = type;
        this.date = date;
    }
}
