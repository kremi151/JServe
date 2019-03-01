package lu.mkremer.jserve.exception;

public class NotMappableException extends RuntimeException {

    public NotMappableException(String message) {
        super(message);
    }

    public NotMappableException(String message, Throwable t) {
        super(message, t);
    }

}
