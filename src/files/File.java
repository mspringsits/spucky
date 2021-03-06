package files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.probeContentType;

public class File extends Resource {

    private Path path;

    public File(Path path) throws IOException {
        this.path = path;
        this.CONTENT_TYPE = new MimeType().getContentType(this.path.toString());
    }

    public byte[] readContentFromDisk() throws IOException {
        return Files.readAllBytes(this.path);
    }

    @Override
    public String toString() {
        return this.path.getFileName().toString();
    }

    public Path getPath() {
        return this.path;
    }
}
