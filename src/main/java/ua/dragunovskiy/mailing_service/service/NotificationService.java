package ua.dragunovskiy.mailing_service.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.dao.Dao;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.dto.NotificationDtoWithID;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.mapper.NotificationDtoMapper;
import ua.dragunovskiy.mailing_service.mapper.NotificationDtoWithIDMapper;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final Dao<UUID, Notification> notificationDao;
    private final NotificationDtoMapper notificationDtoMapper;
    private final NotificationDtoWithIDMapper notificationDtoWithIDMapper;
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

    public List<NotificationDto> getAllNotificationsByUsernameFromCookie() {
        List<Notification> notificationsByUsername = notificationDao.getAllByUsername();
        return notificationsByUsername.stream().map(notificationDtoMapper::map).toList();
    }

    public List<NotificationDtoWithID> getAllNotificationDtoWithIDByUsernameFromCookies() {
        List<Notification> notificationsByUsername = notificationDao.getAllByUsername();
        return notificationsByUsername.stream().map(notificationDtoWithIDMapper::map).toList();
    }

    public void addNewNotification(Notification notification) {
        notificationDao.add(notification);
    }

    public Notification getNotificationById(UUID id) {
        return notificationDao.getById(id);
    }

    public void updateNotification(UUID updatedNotificationId, Notification notificationForUpdate) {
        notificationDao.update(updatedNotificationId, notificationForUpdate);
    }
    public void deleteNotification(UUID id) {
        notificationDao.delete(id);
    }

}
