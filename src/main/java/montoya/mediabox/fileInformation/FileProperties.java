package montoya.mediabox.fileInformation;

import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Guarda y recupera información de las desacargas
 *
 * @author Nerea
 */
public class FileProperties {

    private static final String FOLDER_NAME = System.getProperty("user.home") + "/Descargas MediaBox";
    private static final Path JSON_PATH = Paths.get(FOLDER_NAME, "downloads.json");
    
     //Lee archivo .json y develve objeto DirectoryInformation
    public DirectoryInformation loadDownloads() {

        try {
            Files.createDirectories(Paths.get(FOLDER_NAME));

            if (!Files.exists(JSON_PATH)) {
                return new DirectoryInformation(new ArrayList<>(), new HashSet<>());
            }

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(JSON_PATH.toFile()))) {
                DirectoryInformation data = (DirectoryInformation) in.readObject();

                Set<String> rebuiltPaths = new HashSet<>();
                for (FileInformation fi : data.getFileList()) {
                    rebuiltPaths.add(fi.getFolderPath());
                }
                data.getFolderPaths().clear();
                data.getFolderPaths().addAll(rebuiltPaths);

                return data;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading downloads.json: " + e.getMessage());
            return new DirectoryInformation(new ArrayList<>(), new HashSet<>());
        }
    }
    
    //Sobrescribe el fichero .json con toda la lista actual
    public void saveAllDownloads(DirectoryInformation dirInfo) {
        
        try {
            
            Files.createDirectories(Paths.get(FOLDER_NAME));
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(JSON_PATH.toFile()))) {
                out.writeObject(dirInfo);
            }
        
        } catch (IOException e) {
            System.err.println("Error saving all download data: " + e.getMessage());
        }
    }
    
    public void addDownload(FileInformation newFile) {

        try {
            Files.createDirectories(Paths.get(FOLDER_NAME));
            
            DirectoryInformation data = loadDownloads();
            data.getFileList().add(newFile);
            data.getFolderPaths().add(newFile.getFolderPath());
            saveAllDownloads(data);

        } catch (Exception e) {
            System.err.println("Error adding download: " + e.getMessage());
        }
    }
    
    //Borra archivo descargado físicamente y de la lista guardada en .json
    public void deleteDownload(FileInformation fileInfo, List<FileInformation> allFiles, Set<String> allDirs) {

        //Borra archivo fisico
        File f = new File(fileInfo.getFolderPath(), fileInfo.getName());
        if (f.exists()) {
            f.delete();
        }

        for(int i = 0; i <allFiles.size(); i++){
            FileInformation fi = allFiles.get(i);
            if(fi.getName().equals(fileInfo.getName()) && fi.getFolderPath().equals(fileInfo.getFolderPath())){
                allFiles.remove(i);
                break;
            }
        }
        
        boolean emptyFolder = true;
        for(int i = 0; i < allFiles.size(); i++){
            FileInformation fi = allFiles.get(i);
            if(fi.getFolderPath().equals(fileInfo.getFolderPath())){
                emptyFolder = false;
                break;
            }
        }
        
        if(emptyFolder){
            allDirs.remove(fileInfo.getFolderPath());
        }
        
        saveAllDownloads(new DirectoryInformation(allFiles, allDirs));
    }
}
