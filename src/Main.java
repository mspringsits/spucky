import java.io.IOException;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) {
		Server http = new Server(1111, Paths.get("test"));
        try {
            http.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
