package montoya.mediabox.fileInformation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Contiene los metodos necesarios para la creación y recuperación del archivo donde almacenar la información de las descargas
 * @author Nerea
 */
public class FileProperties {
    
    private static final String FOLDER_NAME = System.getProperty("user.home") + "/Archivos MediaBox";
    private static final Path JSON_PATH = Paths.get(FOLDER_NAME, "downloads.json");
    
    //Guarda las descargas en un archivo .json
    public void guardarDescargas(List<FileInformation> lista) {
        try {
            Files.createDirectories(Paths.get(FOLDER_NAME));

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(JSON_PATH.toFile()))) {
                out.writeObject(lista);
            }
        } catch (IOException e) {
            System.err.println("Error saving downloads: " + e.getMessage());
        }
    }

    //Recupera los datos del archivo .json y los muestra en la tabla
    public static List<FileInformation> cargarDescargas() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(JSON_PATH.toFile()))) {
            return (List<FileInformation>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); // Si no existe el archivo, empieza vacío
        }
    }
}
