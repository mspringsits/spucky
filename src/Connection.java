import files.Resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Connection extends Thread {

    private Server server;

	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	private Method method;
	private String resource;
	private HTTPCode responseCode;
	
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

	public Connection(Server server, Socket socket) {
	    this.server = server;
		this.socket = socket;
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * read request and return requested resources
	 */
	public Resource readRequest() {
		StringBuilder content = new StringBuilder();
		Resource res = null;
		try {
		    // read and process first line of request
		    String first = reader.readLine();
		    if(!first.equals("")) {
                String[] first_arr = first.split(" ");
                if(first_arr.length != 3)
                    throw new IllegalRequestFormatException("First line of request is malformed");

                if(Method.GET.name().equals(first_arr[0])) {
                    this.resource = first_arr[1];
                    Path path = Paths.get(this.server.getRoot().toString(), this.resource);
                    if(Files.exists(path)) {
                        res = Resource.readResource(path);
                    }
                    else
                        this.responseCode = HTTPCode.NOT_FOUND;
                }
                // anything other than GET
                else
                    this.responseCode = HTTPCode.FORBIDDEN;
            }
            else
                throw new IllegalRequestFormatException("First line of request is empty");

		    // process header fields
			String cur;
			while(!((cur = reader.readLine()).equals(""))) {
				content.append(cur);
				content.append(System.getProperty("line.separator"));
			}
			socket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//System.out.println(content);
			return res;
		}
	}
	
	public void respond() {
		try {
			//writer.write(this.header+this.render);
			writer.flush();
			socket.shutdownOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		this.readRequest();
		this.respond();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
