import java.io.BufferedReader;
import java.io.StringReader;

public class Request {

    private Header header;
    private byte[] content;

    public Request(String content) {
        boolean readHeader = true;
        new BufferedReader(new StringReader(content)).lines()
                .forEach(
                        entry -> {
                            if(entry.equals(""))
                                System.out.println("backslash");
                            else
                                System.out.println(entry);
                        });
    }
}
