package ua.dragunovskiy.mailing_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ua.dragunovskiy.mailing_service.dto.ErrorDto;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.exception.IncorrectNotificationIdException;
import ua.dragunovskiy.mailing_service.exception.IncorrectUserIdException;
import ua.dragunovskiy.mailing_service.exception.OverdueMessageException;
import ua.dragunovskiy.mailing_service.repository.NotificationDao;
import ua.dragunovskiy.mailing_service.service.NotificationService;
import ua.dragunovskiy.mailing_service.timemechanism.filter.SimpleFilterNotifications;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final SimpleFilterNotifications filterNotifications;

    @GetMapping("/getAllNotifications")
    public ResponseEntity<?> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getAllNotificationDto() {
        return ResponseEntity.ok(notificationService.getAllNotificationDto());
    }

    @GetMapping("/notifications/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(notificationService.getNotificationDtoById(id));
        } catch (IncorrectNotificationIdException e) {
            return ResponseEntity.badRequest()
                    .body(ErrorDto.builder()
                            .status(404)
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/notifications")
    public ResponseEntity<?> saveNotification(@RequestBody Notification notification) {
      return ResponseEntity.ok(notificationService.save(notification));
    }

    @PutMapping("/notifications/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable("id") UUID id, @RequestBody Notification notification) {
        try {
            return ResponseEntity.ok(notificationService.update(id, notification));
        } catch (OverdueMessageException | IncorrectNotificationIdException e) {
            return ResponseEntity.badRequest()
                    .body(ErrorDto.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/deleteOverdueNotifications")
    public void findAndDeleteOverdueNotifications() {
            filterNotifications.filterForDelete();
    }

    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable("id") UUID id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok().build();
        } catch (IncorrectNotificationIdException e) {
            return ResponseEntity.badRequest().body(
                    ErrorDto.builder()
                            .status(404)
                            .message(e.getMessage())
                            .build());
        }
    }
}
