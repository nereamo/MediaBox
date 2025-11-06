package montoya.mediabox.fileInformation;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Metodos necesarios para la creación, guardado y recuperación del archivo .json que almacena informacion de las descargas
 *
 * @author Nerea
 */
public class FileProperties {

    private static final String FOLDER_NAME = System.getProperty("user.home") + "/Archivos MediaBox";
    private static final Path JSON_PATH = Paths.get(FOLDER_NAME, "downloads.json");

    //Crea carpeta y almacena fichero .json con información de la descarga
    public void guardarDatos(DirectoryInformation data) {
        try {
            Files.createDirectories(Paths.get(FOLDER_NAME));
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(JSON_PATH.toFile()))) {
                out.writeObject(data);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar datos de descarga: " + e.getMessage());
        }
    }

    //Lee archivo .json y develve la información de la descarga y su directorio
    public DirectoryInformation cargarDatos() {
        try {
            Files.createDirectories(Paths.get(FOLDER_NAME));
            if (!Files.exists(JSON_PATH)) {
                return new DirectoryInformation(new ArrayList<>(), new HashSet<>());
            }
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(JSON_PATH.toFile()))) {
                return (DirectoryInformation) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            return new DirectoryInformation(new ArrayList<>(), new HashSet<>());
        }
    }
}
