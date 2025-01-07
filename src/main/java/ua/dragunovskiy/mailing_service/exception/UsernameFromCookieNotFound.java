package ua.dragunovskiy.mailing_service.exception;

public class UsernameFromCookieNotFound extends RuntimeException {
    public UsernameFromCookieNotFound(String message) {
        super(message);
    }
}
