package ua.dragunovskiy.mailing_service.sender;

import org.springframework.stereotype.Service;

@Service
public class ViberSender implements Sender {
    @Override
    public void send(String address, String subject, String message) {
        System.out.println("Viber sender is working...message from notification is: " + message);
    }

    @Override
    public String getType() {
        return "viberSender";
    }
}
