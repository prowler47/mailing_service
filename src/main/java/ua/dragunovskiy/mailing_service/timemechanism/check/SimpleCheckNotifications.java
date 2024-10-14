package ua.dragunovskiy.mailing_service.timemechanism.check;

import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.sender.Sender;
import ua.dragunovskiy.mailing_service.sender.SenderType;
import ua.dragunovskiy.mailing_service.sender.senderutil.ChooseSender;
import ua.dragunovskiy.mailing_service.timemechanism.filter.Filter;
import ua.dragunovskiy.mailing_service.util.PrintNotificationToConsole;

import java.util.List;

// simple implementation of CheckNotifications
@Service
public class SimpleCheckNotifications implements CheckNotifications {

    // checking only those notifications that should be sent in the given period on the current day
    @Override
    public void checkNotificationsForSend(Filter<Notification> filter) {
        List<Notification> filteredForSendingList = filter.filterForSending();
        for (Notification notification : filteredForSendingList) {
            PrintNotificationToConsole.printNotificationToConsole(notification);
        }
    }

    public void checkNotificationsForSendWithSenderTypes(Filter<Notification> filter, List<SenderType> senderTypes) {
        List<Notification> filteredForSendingList = filter.filterForSending();
        for (Notification notification : filteredForSendingList) {
            for (SenderType senderType : senderTypes) {
                Sender sender = ChooseSender.chooseSender(senderType);
                sender.send("test", "test", notification.getPayload());
            }
        }
    }

    @Override
    public void checkNotificationsForDelete() {

    }
}
