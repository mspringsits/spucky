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
import java.util.Date;
import java.util.HashMap;

public class Connection extends Thread {

    private Server server;

	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	private Method method;
	private Resource resource;
	private HTTPCode responseCode = HTTPCode.OK;

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
	private void readRequest() {
		StringBuilder content = new StringBuilder();
		try {
		    // read and process first line of request
		    String first = reader.readLine();
		    if(!first.equals("")) {
                String[] first_arr = first.split(" ");
                if(first_arr.length != 3)
                    throw new IllegalRequestFormatException("First line of request is malformed");

                if(Method.GET.name().equals(first_arr[0])) {
                    String relative_path = first_arr[1];
                    Path path = Paths.get(this.server.getRoot().toString(), relative_path);
                    if(Files.exists(path)) {
                        this.resource = Resource.readResource(path);
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
            System.out.println(content);
		}
	}

	/*
	 * create response header and respond to Client
	 */
	private void respond() {
		try {
            Header responseHeader = new Header(new HashMap<>(), this.responseCode);
            responseHeader.add("Date", new Date().toString());
            byte[] content = this.resource.readContentFromDisk();
            if(this.resource != null) {
                responseHeader.add("Content-Type", this.resource.getContentType());
                responseHeader.add("Content-Length", content.length);
            }
            writer.write(responseHeader.toString());
            if(this.resource != null)
                writer.write(new String(content));
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
