package montoya.mediabox.controller;

import java.util.ArrayList;
import java.util.List;
import montoya.mediabox.fileInformation.FileInformation;

/**
 * Permite aplicar filtros para mostrar los archivos deseados.
 * <p> Filtros:
 * <ul>
 * <li> El directorio al que pertenecen </li>
 * <li> Tipo de archivo (formato) </li>
 * </ul>
 * 
 * @author Nerea
 */
public class TypeFilter {
    
    /**
     * Muestra los archivos pertenecientes al directorio seleccionado en la lista.
     * 
     * @param allFiles Lista de los archivos
     * @param folderPath Ruta del directorio por la que se filtra
     * @return Lista de archivos pertenecientes al directorio
     */
    public List<FileInformation> filterByDirectory(List<FileInformation> allFiles, String folderPath) {
        
        List<FileInformation> filter = new ArrayList<>();

        for (FileInformation fi : allFiles) {
            
            if (fi.getFolderPath().equals(folderPath)) {
                filter.add(fi);
            }
        }

        return filter;
    }
    
    /**
     * Muestra los archivos que contengas ese formato.
     * 
     * @param allFiles Lista de los archivos
     * @param filter Filtro por tipo de archivo
     * @return Lista de archivos con el mismo tipo de archivo
     */
    public List<FileInformation> filterByType(List<FileInformation> allFiles, String filter) {

        List<FileInformation> applyFilter = new ArrayList<>();
        
        for (FileInformation fi : allFiles) {

            if (filter == null || filter.equals("ALL")) {
                applyFilter.add(fi);
            } else {
                switch (filter) {
                    case "MP4":
                        if (fi.getType().contains("mp4")) {
                            applyFilter.add(fi);
                        }
                        break;
                    case "MKV":
                        if (fi.getType().contains("x-matroska")) {
                            applyFilter.add(fi);
                        }
                        break;
                    case "WEBM":
                        if (fi.getType().contains("webm")) {
                            applyFilter.add(fi);
                        }
                        break;
                    case "MP3":
                        if (fi.getType().contains("mpeg")) {
                            applyFilter.add(fi);
                        }
                        break;
                    case "WAV":
                        if (fi.getType().contains("wav")) {
                            applyFilter.add(fi);
                        }
                        break;
                    case "M4A":
                        if (fi.getType().contains("m4a")) {
                            applyFilter.add(fi);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return applyFilter;
    }
}
