package ua.dragunovskiy.mailing_service.exception;

public class OverdueMessageException extends RuntimeException {
    public OverdueMessageException(String message) {
        super(message);
    }
}
