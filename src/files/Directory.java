package files;

import java.nio.file.Path;
import java.util.Collection;

public class Directory extends Resource {

    private Collection<Path> files;

    public Directory(Collection<Path> coll) {
        this.files = coll;
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
}
