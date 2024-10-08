package ua.dragunovskiy.mailing_service.timemechanism.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.dragunovskiy.mailing_service.dao.TestPayloadDao;

// it's implementation of CheckNotifications just for testing call
@Service
public class TestCheckNotificationsService implements CheckNotifications {

    @Autowired
    private TestPayloadDao testPayloadDao;

    @Override
    public void checkNotifications() {
        testPayloadDao.addNewTimerTestEntity();
    }
}
