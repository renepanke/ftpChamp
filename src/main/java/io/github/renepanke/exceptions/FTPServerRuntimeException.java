package io.github.renepanke.exceptions;

public class FTPServerRuntimeException extends RuntimeException {

    public FTPServerRuntimeException() {
    }

    public FTPServerRuntimeException(String message) {
        super(message);
    }

    public FTPServerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FTPServerRuntimeException(Throwable cause) {
        super(cause);
    }

    public FTPServerRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
