import java.util.HashMap;
import java.util.Map;

public class Header {

    private final static String VERSION = "HTTP/1.1";
    private String status;
    private HashMap<String, String> header = new HashMap<>();

    public Header(Map<String, String> map, String status) {
        this.status = status;
        for(Map.Entry<String, String> entry: map.entrySet()) {
            this.header.put(entry.getKey(), entry.getValue());
        }
    }

    public Header(String status) {
        this.status = status;
    }

    public void add(String key, String value) {
        this.header.put(key, value);
    }

    public void add(String key, int value) {
        this.header.put(key, value + "");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s", VERSION, this.status));
        sb.append(System.getProperty("line.separator"));
        for (HashMap.Entry<String, String> entry: this.header.entrySet()) {
            sb.append(String.format("%s: %s", entry.getKey(), entry.getValue()));
            sb.append(System.getProperty("line.separator"));
        }
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
