import files.File;
import files.Resource;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Connection extends Thread {

    private Server server;

	private Socket socket;
	private BufferedReader reader;
	private DataOutputStream writer;

	private Method method;
	private Resource resource;
	private Header inputHeader = new Header(HTTPCode.DEFAULT);
	private HTTPCode responseCode = HTTPCode.OK;

	public Connection(Server server, Socket socket) {
	    this.server = server;
		this.socket = socket;
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.writer = new DataOutputStream(socket.getOutputStream());
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
            System.out.println(first);
            if(!first.equals("")) {
                String[] first_arr = first.split(" ");
                if(first_arr.length != 3)
                    throw new IllegalRequestFormatException("First line of request is malformed");

                /*
                 * processing GET request
                 */
                if(Method.GET.name().equals(first_arr[0])) {
                    this.method = Method.GET;
                    String relative_path = first_arr[1];
                    Path path = Paths.get(this.server.getRoot().toString(), relative_path);
                    if(Files.exists(path)) {
                        this.resource = Resource.readResource(path);
                    }
                    else
                        this.responseCode = HTTPCode.NOT_FOUND;
                }
                /*
                 * processing PUT request
                 */
                else if(Method.PUT.name().equals(first_arr[0])) {
                    this.method = Method.PUT;
                    String relative_path = first_arr[1];
                    String[] temp_path = relative_path.split("/");
                    if(temp_path.length != 2 && !temp_path[0].equals("")) {
                        this.responseCode = HTTPCode.FORBIDDEN;
                    }
                    this.resource = new File(Paths.get(this.server.getRoot().toString(), relative_path));
                }
                // anything other than GET
                else
                    this.responseCode = HTTPCode.FORBIDDEN;
            }
            else
                throw new IllegalRequestFormatException("First line of request is empty");

		    /*
		     * process header fields
		     */
			String cur;
			while(!((cur = reader.readLine()).equals(""))) {
			    String[] headerLine = cur.toLowerCase().split(": ");
                if(headerLine.length != 2) {
                    this.responseCode = HTTPCode.BAD_REQUEST;
			        break;
                }
			    this.inputHeader.add(headerLine[0], headerLine[1]);
            }
            System.out.println(this.inputHeader);
            /*
             * process body
             */
            if(this.responseCode == HTTPCode.OK) {
                if(this.method == Method.PUT) {
                    String contentLengthHeader;
                    if((contentLengthHeader = this.inputHeader.get("content-length")) == null)
                        this.responseCode = HTTPCode.LENGTH_REQUIRED;
                    int contentLength;
                    try {
                        contentLength = Integer.parseInt(contentLengthHeader);
                    } catch (NumberFormatException e) {
                        contentLength = -1;
                        this.responseCode = HTTPCode.BAD_REQUEST;
                    }
                    if(contentLength > 0) {
                        byte[] body = new byte[contentLength];
                        InputStream is = this.socket.getInputStream();
                        for(int i = 0; i < contentLength; i++) {
                            int byte_input = is.read();
                            body[i] = (byte) byte_input;
                        }
                        //is.read(body, 0, contentLength);
                        this.socket.shutdownInput();
                        if(this.resource instanceof File) {
                            FileOutputStream out = new FileOutputStream(((File) this.resource).getPath().toFile());
                            out.write(body, 0, contentLength);
                            out.flush();
                            out.close();
                        }
                        else {
                            this.responseCode = HTTPCode.BAD_REQUEST;
                        }
                    }
                }
            }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            //System.out.println(content);
		}
    }

	/*
	 * create response header and respond to Client
	 */
	private void respond() {
        System.out.println("responding");
        try {
            Header responseHeader = new Header(new HashMap<>(), this.responseCode);
            System.out.println(this.responseCode);
            responseHeader.add("Date", new Date().toString());
            byte[] content = null;
            if(this.resource != null && this.method == Method.GET) {
                content = this.resource.readContentFromDisk();
                responseHeader.add("Content-Type", this.resource.getContentType());
                responseHeader.add("Content-Length", content.length);
            }
            writer.writeUTF(responseHeader.toString());
            if(this.resource != null && this.method == Method.GET)
                writer.write(content);
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
