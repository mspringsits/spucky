package files;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collection;

public class Directory extends Resource {

    private Collection<File> files;

    public Directory(Collection<File> coll) {
        this.files = coll;
        this.CONTENT_TYPE = "text/html";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        files.forEach(entry -> {
            sb.append(entry);
            sb.append(System.getProperty("line.separator"));
        });
        return sb.toString();
    }

    @Override
    public byte[] readContentFromDisk() {
        return this.toString().getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public String getContentType() {
        return this.CONTENT_TYPE;
    }
}
