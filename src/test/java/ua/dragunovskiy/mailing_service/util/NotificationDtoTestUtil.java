package ua.dragunovskiy.mailing_service.util;

import lombok.experimental.UtilityClass;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;

import java.util.List;

@UtilityClass
public class NotificationDtoTestUtil {

    private final String testTitle = "Title for testing mailing service";
    public NotificationDto createNotificationDto() {
        return NotificationDto.builder()
                .address("test address")
                .title("test title")
                .payload("test payload")
                .date(Time.getCurrentTimePlusOneYear())
                .build();
    }

    public List<NotificationDto> createListOfNotificationDto() {
        return List.of(NotificationDto.builder()
                        .address("test1 address")
                        .title("test1 title")
                        .payload("test1 payload")
                        .date(Time.getCurrentTimePlusOneYear())
                        .build(),
                NotificationDto.builder()
                        .address("test2 address")
                        .title("test2 title")
                        .payload("test2 payload")
                        .date(Time.getCurrentTimePlusOneYear())
                        .build(),
                NotificationDto.builder()
                        .address("test3 address")
                        .title("test3 title")
                        .payload("test3 payload")
                        .date(Time.getCurrentTimePlusOneYear())
                        .build());
    }
}
