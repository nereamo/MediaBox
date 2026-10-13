package montoya.mediabox.download;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author Nerea
 */
public class YtDlpUpdater {

    private final File ytDlpFile;

    public YtDlpUpdater() {
        String localAppData = System.getenv("LOCALAPPDATA");

        File folder = new File(localAppData + File.separator + "MediaBox" + File.separator + "Tools");
        ytDlpFile = new File(folder, "yt-dlp.exe");
    }

    public File getYtDlpFile() {
        return ytDlpFile;
    }

    public void downloadYtDlp() throws IOException {

        File folder = ytDlpFile.getParentFile();
        Files.createDirectories(folder.toPath());

        File temporaryFile = new File(folder, "yt-dlp-new.exe");

        try {
            URI uri = URI.create(
                    "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe"
            );

            try (var input = uri.toURL().openStream()) {
                Files.copy(input,temporaryFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            }

            Files.move(temporaryFile.toPath(),ytDlpFile.toPath(),StandardCopyOption.REPLACE_EXISTING);

        } finally {
            Files.deleteIfExists(temporaryFile.toPath());
        }
    }

}
