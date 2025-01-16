package ua.dragunovskiy.mailing_service.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.exception.OverdueMessage;
import ua.dragunovskiy.mailing_service.exception.UsernameFromCookieNotFound;
import ua.dragunovskiy.mailing_service.service.EncryptionService;
import ua.dragunovskiy.mailing_service.security.storage.SimpleUserNameStorage;
import ua.dragunovskiy.mailing_service.util.FromDatetimeLocalToStringParser;
import ua.dragunovskiy.mailing_service.util.Time;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("notificationDao")
@RequiredArgsConstructor
public class NotificationDao implements Dao<UUID, Notification> {
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Notification save(Notification notification) {
        Session session = entityManager.unwrap(Session.class);
        session.merge(notification);
        return notification;
    }

    @Override
    @Transactional
    public Notification update(UUID updatedEntityId, Notification entityForUpdate) {
        Notification updatedEntity = getById(updatedEntityId);
        Session session = entityManager.unwrap(Session.class);
        updatedEntity.setUsername(entityForUpdate.getUsername());
        updatedEntity.setId(entityForUpdate.getId());
        updatedEntity.setTitle(entityForUpdate.getTitle());
        updatedEntity.setAddress(entityForUpdate.getAddress());
        String parseDate = FromDatetimeLocalToStringParser.parse(entityForUpdate.getDate());
        updatedEntity.setDate(parseDate);
        updatedEntity.setPayload(entityForUpdate.getPayload());
        session.merge(updatedEntity);
        return updatedEntity;
    }

    @Override
    @Transactional
    public List<Notification> getAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<Notification> query = session.createQuery("from Notification", Notification.class);
        return query.getResultList();
    }

    @Transactional
    public List<Notification> getAllForTesting(String title) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Notification", Notification.class).getResultList()
                .stream().filter(n -> n.getTitle().equals(title)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Notification getById(UUID id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Notification.class, id);
    }

    @Transactional
    public Notification getByTitle(String title) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Notification", Notification.class).getResultList()
                .stream().filter(n -> n.getTitle().equals(title)).findFirst().orElse(null);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Session session = entityManager.unwrap(Session.class);
        Notification notificationForDelete = session.get(Notification.class, id);
        session.remove(notificationForDelete);
    }

    @Transactional
    public void deleteByTitle(String title) {
        Session session = entityManager.unwrap(Session.class);
        List<Notification> notificationToDelete = session.createQuery("from Notification", Notification.class).getResultList()
                .stream().filter(n -> n.getTitle().equals(title)).toList();
        if (!CollectionUtils.isEmpty(notificationToDelete)) {
            for (Notification notification : notificationToDelete) {
                session.remove(notification);
            }
        }
    }

    @Transactional
    public void deleteAll() {
        Session session = entityManager.unwrap(Session.class);
        List<Notification> allNotifications = session.createQuery("from Notification", Notification.class).getResultList();
        for (Notification notification : allNotifications) {
            session.remove(notification);
        }
    }
}
