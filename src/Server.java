import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

public class Server {
	
	private int port = 80;
	private Path root;
	
	public Server() {}
	
	public Server(int port) {
		this.port = port;
	}

    public Server(Path root) {
        this.root = root;
    }

    public Server(int port, Path root) {
	    this.port = port;
	    this.root = root;
    }

    public Path getRoot() {
	    return this.root;
    }
	
	public void start() throws IOException {
	    ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			Socket s = null;
			while(true) {
				s = server.accept();
				Connection r = new Connection(this, s);
				r.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		    if(server != null)
		        server.close();
        }

	}
}
