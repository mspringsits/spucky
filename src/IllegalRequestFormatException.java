import java.io.IOException;

public class IllegalRequestFormatException extends IOException {

    public IllegalRequestFormatException() {
        super();
    }

    public IllegalRequestFormatException(String msg) {
        super(msg);
    }
}
