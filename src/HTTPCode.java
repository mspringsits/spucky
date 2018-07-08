public enum HTTPCode {

    OK (200),
    FORBIDDEN (403);

    private final int code;

    private HTTPCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
