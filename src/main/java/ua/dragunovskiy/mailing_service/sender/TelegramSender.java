package ua.dragunovskiy.mailing_service.sender;

import org.springframework.stereotype.Service;

@Service
public class TelegramSender implements Sender {
    @Override
    public void send(String address, String subject, String message) {
        System.out.println("Telegram sender is working...message from notifications is: " + message);
    }
}
