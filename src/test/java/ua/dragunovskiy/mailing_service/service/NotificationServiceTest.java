package ua.dragunovskiy.mailing_service.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.dragunovskiy.mailing_service.repository.NotificationDao;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationDao notificationDao;
    @InjectMocks
    private NotificationService serviceUnderTest;
}
