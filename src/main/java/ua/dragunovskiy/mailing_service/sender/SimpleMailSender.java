package ua.dragunovskiy.mailing_service.sender;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class SimpleMailSender implements Sender {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(String address, String subject, String message) {
        System.out.println("Mail sender is working... Message from notifications is: " + message);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(address);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public String getType() {
        return "mailSender";
    }

}
