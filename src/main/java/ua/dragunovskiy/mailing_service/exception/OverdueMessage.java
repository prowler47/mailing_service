package ua.dragunovskiy.mailing_service.exception;

public class OverdueMessage extends RuntimeException {
    public OverdueMessage(String message) {
        super(message);
    }
}
