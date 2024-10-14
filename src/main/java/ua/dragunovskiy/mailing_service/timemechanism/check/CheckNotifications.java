package ua.dragunovskiy.mailing_service.timemechanism.check;

import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.sender.SenderType;
import ua.dragunovskiy.mailing_service.timemechanism.filter.Filter;

import java.util.List;

public interface CheckNotifications {
    void checkNotificationsForSend(Filter<Notification> filter);

    void checkNotificationsForSendWithSenderTypes(Filter<Notification> filter, List<SenderType> senderTypes);
    void checkNotificationsForDelete();
}
