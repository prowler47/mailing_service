package ua.dragunovskiy.mailing_service.exception;

public class IncorrectUserIdException extends RuntimeException {
    public IncorrectUserIdException(String message) {
        super(message);
    }
}
