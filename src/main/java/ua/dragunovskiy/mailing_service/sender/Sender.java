package ua.dragunovskiy.mailing_service.sender;

public interface Sender {
    void send(String address, String subject, String message);
}
