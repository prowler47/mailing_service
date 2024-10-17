package ua.dragunovskiy.mailing_service.util;

import lombok.experimental.UtilityClass;
import ua.dragunovskiy.mailing_service.entity.Notification;

@UtilityClass
public class NotificationToConsolePrinter {
    public void printNotificationToConsole(Notification notification) {
        System.out.println("id: " + notification.getId() + "\n" +
                "title: " + notification.getTitle() + "\n" +
                "payload: " + notification.getPayload() + "\n" +
                "date: " + notification.getDate() + "\n");
    }
}
