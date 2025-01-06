package ua.dragunovskiy.mailing_service.mapper;

import org.springframework.stereotype.Component;
import ua.dragunovskiy.mailing_service.dto.NotificationDtoWithID;
import ua.dragunovskiy.mailing_service.entity.Notification;

@Component
public class NotificationDtoWithIDMapper {
    public NotificationDtoWithID map(Notification notification) {
        return new NotificationDtoWithID(
                notification.getId(),
                notification.getAddress(),
                notification.getTitle(),
                notification.getPayload(),
                notification.getDate());
    }
}
