package files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.stream.Stream;

public abstract class Resource {

    protected String CONTENT_TYPE;

    /*
     * read requested Resource from given Path
     */
    public static Resource readResource(Path path) throws FileNotFoundException {
        if(!Files.exists(path))
            throw new FileNotFoundException();
        else {
            if(Files.isDirectory(path)) {
                try(Stream<Path> paths = Files.walk(path)) {
                    HashSet<File> dir = new HashSet<>();
                    paths.filter(Files::isRegularFile).forEach(entry -> {
                        try {
                            dir.add(new File(entry));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    Resource res = new Directory(dir);
                    return res;
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Resource res = new File(path);
                    return res;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
       return null;
    }

    public abstract byte[] readContentFromDisk() throws IOException;

    /*
     * get Content Type of file
     */
    public String getContentType() {
        return this.CONTENT_TYPE;
    }
}
