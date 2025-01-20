package ua.dragunovskiy.mailing_service.util;

import lombok.experimental.UtilityClass;
import ua.dragunovskiy.mailing_service.dto.NotificationDtoWithID;
import ua.dragunovskiy.mailing_service.entity.Notification;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@UtilityClass
public class NotificationTestUtil {

    private final UUID testId = UUID.fromString("e8f62b10-f370-4a3a-a0c9-1a242b97fc00");
    private final String testTitle = "Title for testing mailing service";
    public Notification createTestNotification() {
       return  Notification.builder()
                .id(testId)
                .address("max@mail.com")
                .date(Time.getCurrentTimePlusOneYear())
                .username("User for test")
                .title(testTitle)
                .payload("test payload")
                .build();
    }

    public Notification createTestNotificationForRegisteredUser() {
        return  Notification.builder()
                .id(testId)
                .address("toma@gmail.com")
                .date(Time.getCurrentTimePlusOneYear())
                .username("toma")
                .title(testTitle)
                .payload("test payload")
                .build();
    }

    public Notification createTestNotificationWithOverdueDate() {
        return  Notification.builder()
                .id(testId)
                .address("max@mail.com")
                .date("2022-02-01 12:45")
                .username("User for test")
                .title(testTitle)
                .payload("test payload")
                .build();
    }

    public Notification createTestNotificationForUpdateNotificationTest() {
        return  Notification.builder()
                .id(UUID.randomUUID())
                .address("ozzy@mail.com")
                .date(Time.getCurrentTimePlusOneYear())
                .username("User2 for test")
                .title(testTitle)
                .payload("test payload2")
                .build();
    }

    public List<Notification> createListOfTestNotifications() {
        return List.of(Notification.builder()
                        .address("tom@mail.com")
                        .date("2025-01-09 13:01")
                        .username("user for test")
                        .title(testTitle)
                        .payload("test payload 1")
                        .build(),
                Notification.builder()
                        .address("jon@mail.com")
                        .date("2025-01-09 13:01")
                        .username("user for test")
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

    public List<Notification> listOfFilteredByUsernameTestNotifications() {
        return List.of(Notification.builder()
                        .address("tom@mail.com")
                        .date("2025-01-09 13:01")
                        .username("User for test")
                        .title(testTitle)
                        .payload("test payload 1")
                        .build(),
                Notification.builder()
                        .address("jon@mail.com")
                        .date("2025-01-09 13:01")
                        .username("User for test")
                        .title(testTitle)
                        .payload("test payload 2")
                        .build());
    }

    public List<NotificationDtoWithID> listOfTestNotificationDtoWithId() {
        return List.of(NotificationDtoWithID.builder()
                        .address("tom@mail.com")
                        .date("2025-01-09 13:01")
                        .title(testTitle)
                        .payload("test payload 1")
                        .build(),
                NotificationDtoWithID.builder()
                        .address("jon@mail.com")
                        .date("2025-01-09 13:01")
                        .title(testTitle)
                        .payload("test payload 2")
                        .build());
    }

    public String getTestTitle() {
        return testTitle;
    }
}
