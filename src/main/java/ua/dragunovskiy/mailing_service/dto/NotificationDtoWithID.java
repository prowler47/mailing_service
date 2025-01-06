package ua.dragunovskiy.mailing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationDtoWithID {
    private UUID id;
    private String address;
    private String title;
    private String payload;
    private String date;
}
