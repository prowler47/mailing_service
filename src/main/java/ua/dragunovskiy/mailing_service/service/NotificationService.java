package ua.dragunovskiy.mailing_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.repository.Dao;
import ua.dragunovskiy.mailing_service.dto.NotificationDtoWithID;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.exception.OverdueMessage;
import ua.dragunovskiy.mailing_service.exception.UsernameFromCookieNotFound;
import ua.dragunovskiy.mailing_service.mapper.NotificationDtoWithIDMapper;
import ua.dragunovskiy.mailing_service.security.storage.SimpleUserNameStorage;
import ua.dragunovskiy.mailing_service.util.Time;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final Dao<UUID, Notification> notificationDao;
    private final NotificationDtoWithIDMapper notificationDtoWithIDMapper;
    private final EncryptionService encryptionService;
    private final SimpleUserNameStorage userNameStorage;

    public Notification saveNewNotification(Notification notification) {
            notification.setUsername(encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage()));
            notification.setPayload(notification.getPayload() + "\n - from " + userNameStorage.getUsernameFromStorage());
            String username = encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage());
            if (username == null) {
                throw new UsernameFromCookieNotFound("Username is null");
            }
            System.out.println(username);
            notificationDao.save(notification);
            return notification;
    }

    public List<Notification> getAllNotifications() {
       return notificationDao.getAll();
    }

//    public List<NotificationDtoWithID> getAllNotificationDtoWithIDByUsernameFromCookiesV2() {
//        try {
//            String encodeUsername = encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage());
//            List<Notification> allNotifications = notificationDao.getAll();
//            List<Notification> notificationsByUsername = allNotifications.stream().filter(notification -> notification.getUsername().equals(encodeUsername)).toList();
//            return notificationsByUsername.stream().map(notificationDtoWithIDMapper::map).toList();
//        } catch (NullPointerException e) {
//            throw new UsernameFromCookieNotFound("Username is null");
//        }
//    }

    public List<NotificationDtoWithID> getAllNotificationDtoWithIDByUsernameFromCookiesV2() {
            String encodeUsername = encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage());
            if (encodeUsername == null) {
                throw new UsernameFromCookieNotFound("Username is null");
            }
            System.out.println(encodeUsername);
            List<Notification> allNotifications = notificationDao.getAll();
            List<Notification> notificationsByUsername = allNotifications.stream().filter(notification -> notification.getUsername().equals(encodeUsername)).toList();
            return notificationsByUsername.stream().map(notificationDtoWithIDMapper::map).toList();
    }

    public Notification getNotificationById(UUID id) {
        return notificationDao.getById(id);
    }

    public Notification updateNotification(UUID updatedNotificationId, Notification notificationForUpdate) throws OverdueMessage {
        Notification updatedEntity = notificationDao.getById(updatedNotificationId);
        if (Time.timeComparatorV2(Time.getCurrentTime(), updatedEntity.getDate())) {
            System.out.println("message already send");
            System.out.println(Time.getCurrentTime() + " - " + updatedEntity.getDate());
            throw new OverdueMessage("This message is already send");
        }
        return notificationDao.update(updatedNotificationId, notificationForUpdate);
    }
    public void deleteNotification(UUID id) {
        notificationDao.delete(id);
    }
}
