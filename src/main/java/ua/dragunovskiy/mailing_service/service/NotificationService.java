package ua.dragunovskiy.mailing_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.repository.Dao;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.dto.NotificationDtoWithID;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.exception.OverdueMessage;
import ua.dragunovskiy.mailing_service.exception.UsernameFromCookieNotFound;
import ua.dragunovskiy.mailing_service.mapper.NotificationDtoMapper;
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

    public void saveNewNotification(Notification notification) {
        notification.setUsername(encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage()));
        notification.setPayload(notification.getPayload() + "\n - from " + userNameStorage.getUsernameFromStorage());
        notificationDao.save(notification);
    }

    public List<Notification> getAllNotifications() {
       return notificationDao.getAll();
    }

    public List<NotificationDtoWithID> getAllNotificationDtoWithIDByUsernameFromCookiesV2() {
        try {
            String encodeUsername = encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage());
            List<Notification> allNotifications = notificationDao.getAll();
            List<Notification> notificationsByUsername = allNotifications.stream().filter(notification -> notification.getUsername().equals(encodeUsername)).toList();
            return notificationsByUsername.stream().map(notificationDtoWithIDMapper::map).toList();
        } catch (NullPointerException e) {
            throw new UsernameFromCookieNotFound("Username is null");
        }
    }

    public Notification getNotificationById(UUID id) {
        return notificationDao.getById(id);
    }

    public void updateNotification(UUID updatedNotificationId, Notification notificationForUpdate) throws OverdueMessage {
        Notification updatedEntity = notificationDao.getById(updatedNotificationId);
        if (Time.timeComparatorV2(Time.getCurrentTime(), updatedEntity.getDate())) {
            System.out.println("message already send");
            throw new OverdueMessage("This message is already send");
        }
        notificationDao.update(updatedNotificationId, notificationForUpdate);
    }
    public void deleteNotification(UUID id) {
        notificationDao.delete(id);
    }
}
