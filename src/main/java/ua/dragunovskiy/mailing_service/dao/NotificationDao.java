package ua.dragunovskiy.mailing_service.dao;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.dragunovskiy.mailing_service.entity.Notification;

import java.util.List;
import java.util.UUID;

@Repository("notificationDao")
public class NotificationDao implements Dao<UUID, Notification>  {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void add(Notification entity) {
        Session session = entityManager.unwrap(Session.class);
        session.merge(entity);
    }

    @Override
    @Transactional
    public List<Notification> getAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<Notification> query = session.createQuery("from Notification", Notification.class);
        return query.getResultList();
    }

    @Override
    public Notification getById(UUID id) {
        return null;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Session session = entityManager.unwrap(Session.class);
        Notification notificationForDelete = session.get(Notification.class, id);
        session.remove(notificationForDelete);
    }
}
