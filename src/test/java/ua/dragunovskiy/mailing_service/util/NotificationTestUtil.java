package ua.dragunovskiy.mailing_service.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ua.dragunovskiy.mailing_service.entity.Notification;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class NotificationTestUtil {

    private final UUID testId = UUID.fromString("e8f62b10-f370-4a3a-a0c9-1a242b97fc00");
    private final String testTitle = "Title for testing mailing service";
    public Notification createTestNotification() {
       return  Notification.builder()
                .id(testId)
                .address("max@mail.com")
                .date("2025-01-09 13:01")
                .username("User for test")
                .title(testTitle)
                .payload("test payload")
                .build();
    }

    public List<Notification> createListOfTestNotifications() {
        return List.of(Notification.builder()
                        .address("tom@mail.com")
                        .date("2025-01-09 13:01")
                        .username("User for test 1")
                        .title(testTitle)
                        .payload("test payload 1")
                        .build(),
                Notification.builder()
                        .address("jon@mail.com")
                        .date("2025-01-09 13:01")
                        .username("User for test 2")
                        .title(testTitle)
                        .payload("test payload 2")
                        .build(),
                Notification.builder()
                        .address("jack@mail.com")
                        .date("2025-01-09 13:01")
                        .username("User for test 3")
                        .title(testTitle)
                        .payload("test payload 3")
                        .build());
    }
    public String getTestTitle() {
        return testTitle;
    }
}
