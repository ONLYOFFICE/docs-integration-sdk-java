package exception;

public class OnlyofficeProcessAfterRuntimeException extends RuntimeException {
    public OnlyofficeProcessAfterRuntimeException(String message) {
        super(message);
    }

    public OnlyofficeProcessAfterRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
