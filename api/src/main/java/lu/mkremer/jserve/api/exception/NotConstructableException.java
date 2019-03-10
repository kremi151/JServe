package lu.mkremer.jserve.api.exception;

public class NotConstructableException extends RuntimeException {

    public NotConstructableException(String message) {
        super(message);
    }

    public NotConstructableException(String message, Throwable t) {
        super(message, t);
    }

}
