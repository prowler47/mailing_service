package ua.dragunovskiy.mailing_service.timemechanism.check;

import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.sender.SenderType;
import ua.dragunovskiy.mailing_service.timemechanism.filter.Filter;
import ua.dragunovskiy.mailing_service.util.PrintNotificationToConsole;
import java.util.List;

// it's implementation of CheckNotifications just for testing call
@Service
public class TestCheckNotifications implements CheckNotifications {

    @Override
    public void checkNotificationsForSend(Filter<Notification> filter) {
        List<Notification> filteredForSendingList = filter.filterForSending();
        for (Notification notification : filteredForSendingList) {
            PrintNotificationToConsole.printNotificationToConsole(notification);
        }
    }

    @Override
    public void checkNotificationsForSendWithSenderTypes(Filter<Notification> filter, List<SenderType> senderTypes) {

    }

    @Override
    public void checkNotificationsForDelete() {

    }
}
