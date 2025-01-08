package ua.dragunovskiy.mailing_service.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.exception.OverdueMessage;
import ua.dragunovskiy.mailing_service.exception.UsernameFromCookieNotFound;
import ua.dragunovskiy.mailing_service.service.EncryptionService;
import ua.dragunovskiy.mailing_service.security.storage.SimpleUserNameStorage;
import ua.dragunovskiy.mailing_service.util.FromDatetimeLocalToStringParser;
import ua.dragunovskiy.mailing_service.util.Time;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("notificationDao")
@RequiredArgsConstructor
public class NotificationDao implements Dao<UUID, Notification>  {
    private final EncryptionService encryptionService;
    private final SimpleUserNameStorage userNameStorage;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void add(Notification entity) {
        Session session = entityManager.unwrap(Session.class);
        entity.setUsername(encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage()));
        entity.setPayload(entity.getPayload() + "\n - from " + userNameStorage.getUsernameFromStorage());
        session.merge(entity);
    }

    @Override
    @Transactional
    public void update(UUID updatedEntityId, Notification entityForUpdate) {
        Notification updatedEntity = getById(updatedEntityId);
        Session session = entityManager.unwrap(Session.class);
        updatedEntity.setUsername(entityForUpdate.getUsername());
        updatedEntity.setId(entityForUpdate.getId());
        updatedEntity.setTitle(entityForUpdate.getTitle());
        updatedEntity.setAddress(entityForUpdate.getAddress());
        String parseDate = FromDatetimeLocalToStringParser.parse(entityForUpdate.getDate());
        updatedEntity.setDate(parseDate);
        if (Time.timeComparatorV2(Time.getCurrentTime(), updatedEntity.getDate())) {
            System.out.println("message already send");
            throw new OverdueMessage("This message is already send");
        }
        updatedEntity.setPayload(entityForUpdate.getPayload());
        session.merge(updatedEntity);
    }

    @Override
    @Transactional
    public List<Notification> getAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<Notification> query = session.createQuery("from Notification", Notification.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Notification getById(UUID id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Notification.class, id);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Session session = entityManager.unwrap(Session.class);
        Notification notificationForDelete = session.get(Notification.class, id);
        session.remove(notificationForDelete);
    }

    @Override
    @Transactional
    public List<Notification> getAllByUsername() {
        String encodeUsername = encryptionService.encodeUsername(userNameStorage.getUsernameFromStorage());
        Session session = entityManager.unwrap(Session.class);
        try {
            List<Notification> allNotifications = session.createQuery("from Notification", Notification.class).getResultList();
            return allNotifications.stream().filter(notification -> notification.getUsername().equals(encodeUsername)).toList();
        } catch (NullPointerException e) {
            throw new UsernameFromCookieNotFound("Username is null");
        }

    }
}
