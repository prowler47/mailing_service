package ua.dragunovskiy.mailing_service.sender;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import ua.dragunovskiy.mailing_service.sender.*;

@RequiredArgsConstructor
public class ChooseSender {

    private final SimpleMailSender simpleMailSender;

    // TODO: need to change return valur for Viber and Telegrams cases to Spring beans instead with 'new'
    // TODO: because Spring don't find needed bean in case with 'new' operator
    public Sender chooseSender(SenderType senderType) {
        if (senderType.equals(SenderType.MAIL)) return simpleMailSender;
        else if (senderType.equals(SenderType.VIBER)) return new ViberSender();
        else return new TelegramSender();
    }
}
