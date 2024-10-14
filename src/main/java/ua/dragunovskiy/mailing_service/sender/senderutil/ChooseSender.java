package ua.dragunovskiy.mailing_service.sender.senderutil;

import lombok.experimental.UtilityClass;
import ua.dragunovskiy.mailing_service.sender.*;

@UtilityClass
public class ChooseSender {
    public Sender chooseSender(SenderType senderType) {
        if (senderType.equals(SenderType.MAIL)) return new MailSender();
        else if (senderType.equals(SenderType.VIBER)) return new ViberSender();
        else return new TelegramSender();
    }
}
