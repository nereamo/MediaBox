package montoya.mediabox.info;

import java.io.Serializable;

/**
 *
 * @author Nerea
 */

//Class serializable que permite alamcenar los datos en archivo .dat
public class FileInformation implements Serializable{
    
//    public String name;
//    public long size;
//    public String type;
//    public String date;
//    
//    public FileInformation(String name, long size, String type, String date){
//        this.name = name;
//        this.size = size;
//        this.type = type;
//        this.date = date;
//    }
    
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
