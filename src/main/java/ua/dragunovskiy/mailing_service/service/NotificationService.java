package ua.dragunovskiy.mailing_service.service;

import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.dao.Dao;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.util.FromStringToDateParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    @Qualifier("notificationDao")
    private Dao<UUID, Notification> notificationDao;
    public void addNewNotification() throws ParseException {
        Notification notification = new Notification();
        notification.setTitle("test_notification");
        notification.setPayload("some_payload...");
        String exampleDate = "2024-10-07 12:52";
        notification.setDate(exampleDate);
        notificationDao.add(notification);
    }

    public void addNewNotification(Notification notification) {
        notificationDao.add(notification);
    }

    public void deleteNotification(UUID id) {
        notificationDao.delete(id);
    }
}
