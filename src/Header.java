import java.util.HashMap;
import java.util.Map;

public class Header {

    private final static String VERSION = "HTTP/1.1";
    private HTTPCode status;
    private HashMap<String, String> header = new HashMap<>();

    public Header(Map<String, String> map, HTTPCode status) {
        this.status = status;
        for(Map.Entry<String, String> entry: map.entrySet()) {
            this.header.put(entry.getKey(), entry.getValue());
        }
        this.header.put("Server", "spucky");
    }

    public Header(HTTPCode status) {
        this.status = status;
        this.header.put("Server", "spucky");
    }

    public void add(String key, String value) {
        this.header.put(key, value);
    }

    public void add(String key, int value) {
        this.header.put(key, value + "");
    }

    public String get(String key) {
        return this.header.get(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %d %s", VERSION, this.status.getCode(), this.status));
        sb.append(System.getProperty("line.separator"));
        for (HashMap.Entry<String, String> entry: this.header.entrySet()) {
            sb.append(String.format("%s: %s", entry.getKey(), entry.getValue()));
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        Header header = (Header) o;
        return this.header.equals(header);
    }

    @Override
    public int hashCode() {
        return this.header.hashCode();
    }
}
