package at.fh.ooe.moc5.amazingrace.service;

/**
 * Created by Thomas on 12/25/2015.
 */
public class ServiceException extends Exception {
    /**
     * Enumeration which defines the common service errors
     */
    public static enum ServiceErrorCode {
        INVALID_REQUEST,
        TIMEOUT,
        UNKNOWN,
        REQUEST_NOT_OK,
        INVALID_CREDENTIALS
    }

    private ServiceErrorCode errorCode;

    public ServiceException() {
        this(null, null, null);
    }

    public ServiceException(String detailMessage) {
        this(null, detailMessage, null);
    }

    public ServiceException(String detailMessage, Throwable throwable) {
        this(null, detailMessage, throwable);
    }

    public ServiceException(Throwable throwable) {
        this(null, null, throwable);
    }

    public ServiceException(ServiceErrorCode errorCode) {
        this(errorCode, null, null);
    }

    public ServiceException(ServiceErrorCode errorCode, String detailMessage) {
        this(errorCode, detailMessage, null);
    }


    public ServiceException(ServiceErrorCode errorCode, Throwable throwable) {
        this(errorCode, null, throwable);
    }

    public ServiceException(ServiceErrorCode errorCode, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorCode = errorCode;
    }

    public ServiceErrorCode getErrorCode() {
        return errorCode;
    }
}
