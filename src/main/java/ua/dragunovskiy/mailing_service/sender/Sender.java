package ua.dragunovskiy.mailing_service.sender;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

public interface Sender {
    void send(String address, String subject, String message);

    String getType();
}
