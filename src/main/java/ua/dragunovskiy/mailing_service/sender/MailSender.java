package ua.dragunovskiy.mailing_service.sender;

import org.springframework.stereotype.Service;

@Service
public class MailSender implements Sender {
    @Override
    public void send(String address, String subject, String message) {
        System.out.println("Mail sender is working... Message from notifications is: " + message);
    }
}
