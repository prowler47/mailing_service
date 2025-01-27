package ua.dragunovskiy.mailing_service.exception;

public class IncorrectNotificationIdException extends RuntimeException {
    public IncorrectNotificationIdException(String message) {
        super(message);
    }
}
