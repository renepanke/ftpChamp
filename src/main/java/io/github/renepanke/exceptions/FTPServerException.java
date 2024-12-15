package io.github.renepanke.exceptions;

public class FTPServerException extends Exception {

    public FTPServerException() {
    }

    public FTPServerException(String message) {
        super(message);
    }

    public FTPServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public FTPServerException(Throwable cause) {
        super(cause);
    }

    public FTPServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
