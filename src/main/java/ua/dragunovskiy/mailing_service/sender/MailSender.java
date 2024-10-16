package ua.dragunovskiy.mailing_service.sender;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class MailSender implements Sender {

    private String senderName = "mail";
    @Override
    public void send(String address, String subject, String message) {
        System.out.println("Mail sender is working... Message from notifications is: " + message);
    }

    @Override
    public String getType() {
        return "mailSender";
    }
}
