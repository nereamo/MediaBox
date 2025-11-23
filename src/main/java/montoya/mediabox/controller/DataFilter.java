package montoya.mediabox.controller;

import java.util.ArrayList;
import java.util.List;
import montoya.mediabox.fileInformation.FileInformation;

/**
 *
 * @author Nerea
 */
public class DataFilter {
    
    //Filtramos por directorio seleccionado en la lista
    public List<FileInformation> filterByDirectory(List<FileInformation> allFiles, String folderPath) {
        
        List<FileInformation> filter = new ArrayList<>();

        for (FileInformation fi : allFiles) {
            
            if (fi.folderPath.equals(folderPath)) {
                filter.add(fi);
            }
        }

        return filter;
    }
    
    //Filtramos por tipo de archivo en el combobox
    public List<FileInformation> filterByType(List<FileInformation> files, String selectedType) {

        List<FileInformation> filter = new ArrayList<>();
        
        for (FileInformation fi : files) {

            if (selectedType == null || selectedType.equals("All")) {
                filter.add(fi);
            } else {
                switch (selectedType) {
                    case "MP4":
                        if (fi.type.contains("mp4")) {
                            filter.add(fi);
                        }
                        break;
                    case "MKV":
                        if (fi.type.contains("x-matroska")) {
                            filter.add(fi);
                        }
                        break;
                    case "WEBM":
                        if (fi.type.contains("webm")) {
                            filter.add(fi);
                        }
                        break;
                    case "MP3":
                        if (fi.type.contains("mpeg")) {
                            filter.add(fi);
                        }
                        break;
                    case "WAV":
                        if (fi.type.contains("wav")) {
                            filter.add(fi);
                        }
                        break;
                    case "M4A":
                        if (fi.type.contains("m4a")) {
                            filter.add(fi);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return filter;
    }
}
