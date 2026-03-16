package montoya.mediabox.fileInformation;

import montoya.mediaBox.utils.Logger;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Gestiona la información de las descargas almacenada en el JSON.
 * 
 * <p>Permite:
 * <ul>
 * <li> Cargar la información alamacenada en JSON </li>
 * <li> Añadir una nueva descarga en JSON </li>
 * <li> Eliminar una descarga en JSON </li>
 * </ul>
 *
 * @author Nerea
 */
public class FileProperties {

    /** Carpeta principal donde se guardan las descargas */
    private static final String FOLDER_NAME = System.getProperty("user.home") + "/Descargas MediaBox";
    
    /** Ruta del archivo JSON donde se almacena la información de descargas */
    private static final Path JSON_PATH = Paths.get(FOLDER_NAME, "downloads.json");
    
    /**
     * Carga la información de las descargas
     * 
     * @return {@link DirectoryInformation} con la lista de archivos y directorios
     */
    public DirectoryInformation loadDownloads() {

        try {
            Files.createDirectories(Paths.get(FOLDER_NAME)); //Si le directorio no existe, lo crea

            if (!Files.exists(JSON_PATH)){ //Si el JSON no existe, devuelve objeto vacío
                return new DirectoryInformation(new HashSet<>(), new HashSet<>());
            }

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(JSON_PATH.toFile()))) {
                DirectoryInformation data = (DirectoryInformation) in.readObject();

                //Rutas únicas de los directorios
                Set<String> rebuiltPaths = new HashSet<>();
                for (FileInformation fi : data.getFileList()) {
                    rebuiltPaths.add(fi.getFolderPath());
                }
                data.getFolderPaths().clear(); //Limpiar rutas antiguas
                data.getFolderPaths().addAll(rebuiltPaths);

                return data;
            }

        } catch (IOException | ClassNotFoundException e) {
            Logger.logError("Error loading downloads.json", e);
            return new DirectoryInformation(new HashSet<>(), new HashSet<>());
        }
    }
    
    /**
     * Guarda la información de las descragas en el archivo JSON
     * 
     * @param dirInfo Objeto {@link DirectoryInformation} que contiene la lista de descargas
     */
    public void saveAllDownloads(DirectoryInformation dirInfo) {
        
        try {
            
            Files.createDirectories(Paths.get(FOLDER_NAME)); //Si le directorio no existe, lo crea
            
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(JSON_PATH.toFile()))) {
                out.writeObject(dirInfo); //Serializar objeto
            }
        
        } catch (IOException e) {
            Logger.logError("Error saving all download data to downloads.json", e);
        }
    }
    
    /**
     * Añade la nueva descarga a la lista y actualiza el JSON
     * 
     * @param newFile Objeto {@link FileInformation} último archivo descargado.
     */
    public void addDownload(FileInformation newFile) {

        try {
            Files.createDirectories(Paths.get(FOLDER_NAME)); //Si le directorio no existe, lo crea
            
            DirectoryInformation data = loadDownloads(); //Carga lista actual de JSON
            data.getFileList().add(newFile); //Añade el nuevo archivo
            data.getFolderPaths().add(newFile.getFolderPath()); //Añade el directorio
            saveAllDownloads(data); //Guarda los cambios en JSON

        } catch (Exception e) {
            Logger.logError("Error adding new download to downloads.json", e);
        }
    }
    
    /**
     * Elimina el archivo físicamente y del archivo JSON.
     * Actualiza la lista de directorios si éste se queda vacío.
     * 
     * @param fileInfo Información del archivo a eliminar
     * @param allFiles Lista de todos los archivos
     * @param allDirs Colección de las rutas de los directorios
     */
    public void deleteDownload(FileInformation fileInfo, Set<FileInformation> allFiles, Set<String> allDirs) {

        //Borra archivo fisico
        File f = new File(fileInfo.getFolderPath(), fileInfo.getName());
        if (f.exists()) {
            f.delete();
        }

        allFiles.remove(fileInfo);

        //Elimina el directorio que se quedó vacío
        boolean emptyFolder = true;
        for (FileInformation fi : allFiles) {
            if (fi.getFolderPath().equals(fileInfo.getFolderPath())) {
                emptyFolder = false;
                break;
            }
        }

        if (emptyFolder) {
            allDirs.remove(fileInfo.getFolderPath());
        }

        //guardar cambien en JSON
        saveAllDownloads(new DirectoryInformation(allFiles, allDirs));
    }

    /**
     * Borra el historial completo de descargas del archivo JSON.
     */
    public void clearCache() {
        try {
            //Creamos un objeto de información vacío
            DirectoryInformation emptyData = new DirectoryInformation(new HashSet<>(), new HashSet<>());

            //Sobreescribimos el archivo JSON con datos vacíos
            saveAllDownloads(emptyData);

            System.out.println("Clear history.");

        } catch (Exception e) {
            Logger.logError("Error clearing downloads.json cache", e);
        }
    }
}
