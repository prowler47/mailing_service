package ua.dragunovskiy.mailing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationDto {
    private String address;
    private String title;
    private String payload;
    private String date;
}
