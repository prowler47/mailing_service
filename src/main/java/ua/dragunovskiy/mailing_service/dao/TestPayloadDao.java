package ua.dragunovskiy.mailing_service.dao;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.dragunovskiy.mailing_service.entity.TimerTestEntity;

@Repository
public class TestPayloadDao {

    @Autowired
    private EntityManager entityManager;

    // test method for interaction with database
    @Transactional
    public void addNewTimerTestEntity() {
        TimerTestEntity timerTestEntity = new TimerTestEntity();
        timerTestEntity.setTestPayLoad("Test payload...");
        Session session = entityManager.unwrap(Session.class);
        session.merge(timerTestEntity);
    }
}
