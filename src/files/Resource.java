package files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.Stream;

public abstract class Resource {

    /*
     * read requested Resource from given Path
     */
    public static Resource readResource(Path path) throws FileNotFoundException {
        if(!Files.exists(path))
            throw new FileNotFoundException();
        else {
            if(Files.isDirectory(path)) {
                try(Stream<Path> paths = Files.walk(path)) {
                    HashSet<Path> dir = new HashSet<>();
                    paths.filter(Files::isRegularFile).forEach(dir::add);
                    return new Directory(dir);
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("file");
            }
        }
       return null;
    }
}
