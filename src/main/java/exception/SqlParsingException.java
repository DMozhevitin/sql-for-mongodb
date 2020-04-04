package exception;

public class SqlParsingException extends Exception {

    public SqlParsingException(String message) {
        super(message);
    }

    public SqlParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
