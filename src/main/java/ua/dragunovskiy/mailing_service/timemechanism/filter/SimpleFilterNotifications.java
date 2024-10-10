package ua.dragunovskiy.mailing_service.timemechanism.filter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.dao.NotificationDao;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.service.NotificationService;
import ua.dragunovskiy.mailing_service.timemechanism.comparator.DateComparator;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleFilterNotifications implements Filter<Notification> {

    private final NotificationDao notificationDao;
    private final NotificationService notificationService;
    private final int period = 59;

    // it's a filter to select only notifications with specified date of sending
    @Override
    public List<Notification> filterForSending() {
        List<Notification> notificationList = notificationDao.getAll();
        List<Notification> filteredNotifications = new ArrayList<>();
        for (Notification notification : notificationList) {
            if (DateComparator.compareActualDate(notification, period)) {
                filteredNotifications.add(notification);
            }
        }
        return filteredNotifications;
    }

    @Override
    public void filterForDelete() {
        List<Notification> notificationList = notificationDao.getAll();
        for (Notification notification : notificationList) {
            if (DateComparator.compareOverdueDate(notification)) {
                notificationService.deleteNotification(notification.getId());
            }
        }
    }
}
