package ua.dragunovskiy.mailing_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.exception.IncorrectNotificationIdException;
import ua.dragunovskiy.mailing_service.exception.IncorrectUserIdException;
import ua.dragunovskiy.mailing_service.mapper.NotificationDtoMapper;
import ua.dragunovskiy.mailing_service.repository.Dao;
import ua.dragunovskiy.mailing_service.dto.NotificationDtoWithID;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.exception.OverdueMessageException;
import ua.dragunovskiy.mailing_service.exception.UsernameFromCookieNotFound;
import ua.dragunovskiy.mailing_service.mapper.NotificationDtoWithIDMapper;
import ua.dragunovskiy.mailing_service.security.storage.SimpleUserNameStorage;
import ua.dragunovskiy.mailing_service.util.Time;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final Dao<UUID, Notification> notificationDao;
    private final NotificationDtoWithIDMapper notificationDtoWithIDMapper;
    private final EncryptionService encryptionService;
    private final SimpleUserNameStorage userNameStorage;
    private final NotificationDtoMapper notificationDtoMapper;

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

    public NotificationDto save(Notification notification) {
        Notification savedNotification = notificationDao.save(notification);
        return notificationDtoMapper.map(savedNotification);
    }

    public List<Notification> getAllNotifications() {
       return notificationDao.getAll();
    }

    public List<NotificationDto> getAllNotificationDto() {
        return notificationDao.getAll().stream().map(notificationDtoMapper::map).collect(Collectors.toList());
    }

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

    public NotificationDto getNotificationDtoById(UUID id) {
        Notification notificationById = notificationDao.getById(id);
        if (notificationById == null) {
            throw new IncorrectNotificationIdException("Notification with given id is not exist");
        }
        return notificationDtoMapper.map(notificationById);
    }

    public Notification updateNotification(UUID updatedNotificationId, Notification notificationForUpdate) throws OverdueMessageException {
        Notification updatedEntity = notificationDao.getById(updatedNotificationId);
        if (Time.timeComparatorV3(Time.getCurrentTime(), updatedEntity.getDate())) {
            System.out.println("message already send");
            System.out.println(Time.getCurrentTime() + " - " + updatedEntity.getDate());
            throw new OverdueMessageException("This message is already send");
        }
        return notificationDao.update(updatedNotificationId, notificationForUpdate);
    }

    public NotificationDto update(UUID updatedNotificationId, Notification notificationForUpdate) throws OverdueMessageException {
        Notification updatedEntity = notificationDao.getById(updatedNotificationId);
        if (updatedEntity == null) {
            throw new IncorrectNotificationIdException("Incorrect id for updated notification");
        }
        if (Time.timeComparatorV3(Time.getCurrentTime(), updatedEntity.getDate())) {
            System.out.println("message already send");
            System.out.println(Time.getCurrentTime() + " - " + updatedEntity.getDate());
            throw new OverdueMessageException("This message is already send");
        }
        Notification updatedNotification = notificationDao.update(updatedNotificationId, notificationForUpdate);
        return notificationDtoMapper.map(updatedNotification);
    }
    public void deleteNotification(UUID id) {
        try {
            notificationDao.delete(id);
        } catch (IllegalArgumentException e) {
            throw new IncorrectNotificationIdException("Incorrect notification id");
        }
    }
}
