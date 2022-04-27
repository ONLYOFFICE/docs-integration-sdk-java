package exception;

public class OnlyofficeProcessBeforeRuntimeException extends RuntimeException {
    public OnlyofficeProcessBeforeRuntimeException(String message) {
        super(message);
    }

    public OnlyofficeProcessBeforeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
