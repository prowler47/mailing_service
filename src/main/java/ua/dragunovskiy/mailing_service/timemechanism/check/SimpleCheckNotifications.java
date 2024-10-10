package ua.dragunovskiy.mailing_service.timemechanism.check;

import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.entity.Notification;
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

    @Override
    public void checkNotificationsForDelete() {

    }
}
