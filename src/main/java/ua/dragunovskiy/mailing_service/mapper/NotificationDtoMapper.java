package ua.dragunovskiy.mailing_service.mapper;

import org.springframework.stereotype.Component;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.entity.Notification;

@Component
public class NotificationDtoMapper {
    public NotificationDto map(Notification notification) {
        return new NotificationDto(
                notification.getAddress(),
                notification.getTitle(),
                notification.getPayload(),
                notification.getDate());
    }
}
