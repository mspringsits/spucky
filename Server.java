import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	private int port = 80;
	
	String response = 
			"HTTP/1.1 200 OK\n"
			+ "Date: Mon, 23 May 2005 22:38:34 GMT\n"
			+ "Server: Apache/1.3.3.7 (Unix) (Red-Hat/Linux)\n"
			+ "Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT\n"
			+ "ETag: '3f80f-1b6-3e1cb03b'\n"
			+ "Content-Type: text/html; charset=UTF-8\n"
			+ "Content-Length: 138\n"
			+ "Accept-Ranges: bytes\n"
			+ "Connection: close \n"
			+ "\n"
			+ "<html>\n"
			+ "<head>\n"
			+ "<title>An Example Page</title>\n"
			+ "</head>\n"
			+ "<body>\n"
			+ "Hello World, this is a very simple HTML document.\n"
			+ "</body>\n"
			+ "</html>\n\n";
	
	public Server() {}
	
	public Server(int port) {
		this.port = port;
	}
	
	public void spinUp() {
		try {
			ServerSocket server = new ServerSocket(port);
			Socket s = null;
			while(true) {
				s = server.accept();
				
				// read header or get request
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String content = "";
				String cur = "";
				//try
				while(!((cur = br.readLine()).equals(""))) {
					content += cur+"\n";
				}
				System.out.println(content);
				
				// simple response
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bw.write(response);
				bw.flush();
				//bw.close();
				//br.close();
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
