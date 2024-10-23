package ua.dragunovskiy.mailing_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class TestMailSender {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("sdragunovskiy@gmail.com");
        simpleMailMessage.setSubject("test");
        simpleMailMessage.setText("some text");
        javaMailSender.send(simpleMailMessage);
    }
}
