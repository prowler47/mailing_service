package ua.dragunovskiy.mailing_service.exception;

public class UsernameFromCookieNotFoundException extends RuntimeException {
    public UsernameFromCookieNotFoundException(String message) {
        super(message);
    }
}
