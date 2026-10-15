package montoya.mediabox.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Gestiona la descarga y actualización automática del binario yt-dlp.exe
 * necesario para el funcionamiento del motor de descargas de la aplicación.
 * Los binarios se almacenan de forma local en el directorio AppData del usuario.
 * 
 * @author Nerea
 */
public class YtDlpUpdater {

    /**
     * Archivo ejecutable de yt-dlp en el sistema local.
     */
    private final File folder;
    private final File ytDlpFile;
    private final File ffmpegFile;
    private final File ffprobeFile;
    private final File ffplayFile;

    /**
     * Constructor que inicializa la ruta local de yt-dlp dentro de la carpeta
     * de la aplicación en el AppData del usuario (MediaBox/Tools).
     */
    public YtDlpUpdater() {
        
        String localAppData = System.getenv("LOCALAPPDATA"); //Ubicacion del archivo Yt-Dlp.exe

        folder = new File(localAppData + File.separator + "MediaBox" + File.separator + "Tools");
        
        ytDlpFile = new File(folder, "yt-dlp.exe");
        ffmpegFile = new File(folder, "ffmpeg.exe");
        ffprobeFile = new File(folder, "ffprobe.exe");
        ffplayFile = new File(folder, "ffplay.exe");
    }

    /**
     * Obtiene el archivo ejecutable asociado a yt-dlp.
     *
     * @return El objeto {@link File} que apunta a la ubicación de yt-dlp.exe.
     */
    public File getYtDlpFile() {
        return ytDlpFile;
    }
    
    /**
     * Comprueba si están presentes todas las herramientas.
     *
     * @return true si existen todos los ejecutables.
     */
    public boolean areAllToolsPresent() {
        return ytDlpFile.isFile() && ffmpegFile.isFile() && ffprobeFile.isFile() && ffplayFile.isFile();
    }

    /**
     * Descarga las herramientas que falten.
     *
     * @throws IOException si ocurre un error de descarga o extracción.
     */
    public void ensureAllToolsReady() throws IOException {

        Files.createDirectories(folder.toPath());

        // Descargar yt-dlp si no existe.
        if (!ytDlpFile.isFile()) {
            downloadFile();
        }

        // Descargar FFmpeg si falta alguno de sus ejecutables.
        if (!ffmpegFile.isFile() || !ffprobeFile.isFile() || !ffplayFile.isFile()) {
            downloadFfmpeg();
        }
    }

    /**
     * Descarga yt-dlp desde GitHub.
     *
     * @throws IOException si falla la descarga.
     */
    public void downloadFile() throws IOException {

        Files.createDirectories(folder.toPath());

        File temporaryFile = new File(folder, "yt-dlp-new.tmp");

        try {
            URI uri = URI.create("https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe");

            URLConnection connection = uri.toURL().openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);

            try (InputStream input = connection.getInputStream()) {
                Files.copy(input,temporaryFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            }

            if (temporaryFile.length() == 0) {
                throw new IOException("El archivo yt-dlp descargado está vacío.");
            }

            Files.move(temporaryFile.toPath(),ytDlpFile.toPath(),StandardCopyOption.REPLACE_EXISTING);

        } finally {
            Files.deleteIfExists(temporaryFile.toPath());
        }
    }

    /**
     * Descarga FFmpeg y extrae sus tres ejecutables en la carpeta Tools.
     *
     * @throws IOException si falla la descarga o no se encuentran los archivos.
     */
    private void downloadFfmpeg() throws IOException {

        Files.createDirectories(folder.toPath());

        File zipFile = new File(folder, "ffmpeg-download.zip");

        // Distribución Essentials para Windows de 64 bits.
        URI uri = URI.create("https://github.com/GyanD/codexffmpeg/releases/download/9.0.2/ffmpeg-9.0.2-essentials_build.zip");

        try {
            URLConnection connection = uri.toURL().openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(120000);

            // Descargar el paquete ZIP.
            try (InputStream input = connection.getInputStream()) {
                Files.copy(input,zipFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            }

            boolean foundFfmpeg = ffmpegFile.isFile();
            boolean foundFfprobe = ffprobeFile.isFile();
            boolean foundFfplay = ffplayFile.isFile();

            // Buscar los ejecutables dentro del ZIP.
            try (ZipInputStream zipInput = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {

                ZipEntry entry;

                while ((entry = zipInput.getNextEntry()) != null) {

                    if (entry.isDirectory()) {
                        zipInput.closeEntry();
                        continue;
                    }

                    String name = entry.getName().replace('\\', '/');

                    File targetFile = null;

                    if (name.endsWith("/bin/ffmpeg.exe")
                            && !foundFfmpeg) {
                        targetFile = ffmpegFile;
                    } else if (name.endsWith("/bin/ffprobe.exe")
                            && !foundFfprobe) {
                        targetFile = ffprobeFile;
                    } else if (name.endsWith("/bin/ffplay.exe")
                            && !foundFfplay) {
                        targetFile = ffplayFile;
                    }

                    if (targetFile != null) {

                        File temporaryFile = new File(folder,targetFile.getName() + ".tmp");

                        try {
                            Files.copy(zipInput,temporaryFile.toPath(),StandardCopyOption.REPLACE_EXISTING);

                            if (temporaryFile.length() == 0) {
                                throw new IOException(
                                        "El archivo descargado está vacío: "
                                        + targetFile.getName()
                                );
                            }

                            Files.move(temporaryFile.toPath(),targetFile.toPath(),StandardCopyOption.REPLACE_EXISTING);

                            if (targetFile.equals(ffmpegFile)) {
                                foundFfmpeg = true;
                            } else if (targetFile.equals(ffprobeFile)) {
                                foundFfprobe = true;
                            } else if (targetFile.equals(ffplayFile)) {
                                foundFfplay = true;
                            }

                        } finally {
                            Files.deleteIfExists(temporaryFile.toPath());
                        }
                    }

                    zipInput.closeEntry();

                    if (foundFfmpeg && foundFfprobe && foundFfplay) {
                        break;
                    }
                }
            }

            if (!foundFfmpeg || !foundFfprobe || !foundFfplay) {
                throw new IOException(
                        "No se han encontrado todos los ejecutables de FFmpeg."
                );
            }

        } finally {
            Files.deleteIfExists(zipFile.toPath());
        }
    }
}