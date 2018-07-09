package files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

public class MimeType {

    private static final String DEFAULT_TYPE = "application/octet-stream";

    private static final Path mimeTypesFile = Paths.get("/etc/mime.types");

    private final FileNameMap fileNameMap = URLConnection.getFileNameMap();

    private final FileTypeMap fileTypeMap = FileTypeMap.getDefaultFileTypeMap();

    private final FileTypeMap mimeTypesMap;

    public MimeType() {
        FileTypeMap map = null;
        if (Files.isRegularFile(mimeTypesFile)) {
            try {
                map = new MimetypesFileTypeMap(mimeTypesFile.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mimeTypesMap = map;
    }

    public String getContentType(String filename) {
        String type = null;

        if (mimeTypesMap != null) {
            type = mimeTypesMap.getContentType(filename);
        }

        if (type == null || type.equals(DEFAULT_TYPE)) {
            type = fileNameMap.getContentTypeFor(filename);
        }

        if (type == null || type.equals(DEFAULT_TYPE)) {
            type = fileTypeMap.getContentType(filename);
        }

        return type;
    }
}