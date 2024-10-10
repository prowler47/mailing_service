package ua.dragunovskiy.mailing_service.timemechanism.check;

import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.timemechanism.filter.Filter;

public interface CheckNotifications {
    void checkNotificationsForSend(Filter<Notification> filter);
    void checkNotificationsForDelete();
}
