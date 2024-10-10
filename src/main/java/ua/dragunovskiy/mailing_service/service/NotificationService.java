package ua.dragunovskiy.mailing_service.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.dao.Dao;
import ua.dragunovskiy.mailing_service.entity.Notification;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Dao<UUID, Notification> notificationDao;
    public void addNewNotification() throws ParseException {
        Notification notification = new Notification();
        notification.setTitle("test_notification");
        notification.setPayload("some_payload...");
        String exampleDate = "2024-10-07 12:52";
        notification.setDate(exampleDate);
        notificationDao.add(notification);
    }

    public List<Notification> getAllNotifications() {
       return notificationDao.getAll();
    }

    public void addNewNotification(Notification notification) {
        notificationDao.add(notification);
    }

    public void deleteNotification(UUID id) {
        notificationDao.delete(id);
    }
}
