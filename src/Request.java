import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Request extends Thread {
	
	private Socket socket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
	
	String header = 
			"HTTP/1.1 200 OK\n"
			+ "Date: Mon, 23 May 2005 22:38:34 GMT\n"
			+ "Server: Apache/1.3.3.7 (Unix) (Red-Hat/Linux)\n"
			+ "Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT\n"
			+ "ETag: '3f80f-1b6-3e1cb03b'\n"
			+ "Content-Type: text/html; charset=UTF-8\n"
			+ "Accept-Ranges: bytes\n"
			+ "Connection: close \n"
			+ "\n";
	
	String render =
			"<html>\n"
			+ "<head>\n"
			+ "<title>An Example Page</title>\n"
			+ "</head>\n"
			+ "<body>\n"
			+ "Hello World, this is a very simple HTML document.\n"
			+ "</body>\n"
			+ "</html>\n\n";

	// handle Exceptions more beatiful
	public Request(Socket socket) {
		this.socket = socket;
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleData() {
		String content = "";
		try {
			String cur = "";
			while(!((cur = reader.readLine()).equals(""))) {
				content += cur+"\n";
			}
			socket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println(content);
			//return content;
		}
	}
	
	public void respond() {
		try {
			writer.write(this.header+this.render);
			writer.flush();
			socket.shutdownOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		this.handleData();
		this.respond();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		try {
//			//socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
