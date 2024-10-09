package ua.dragunovskiy.mailing_service.timemechanism.filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.dao.NotificationDao;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.timemechanism.DateComparator;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterNotifications implements Filter<Notification> {

    @Autowired
    private NotificationDao notificationDao;

    private final int period = 59;

    // it's a filter to select only notifications with specified date of sending
    @Override
    public List<Notification> filter() {
        List<Notification> notificationList = notificationDao.getAll();
        List<Notification> filteredNotifications = new ArrayList<>();
        for (Notification notification : notificationList) {
            if (DateComparator.compareActualDate(notification, period)) {
                filteredNotifications.add(notification);
            }
        }
        return filteredNotifications;
    }
}
