package ua.dragunovskiy.mailing_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.service.NotificationService;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/addNotification")
    public void addNotification(@RequestBody Notification notification) {
        notificationService.addNewNotification(notification);
    }

    @DeleteMapping("/deleteNotification/{id}")
    public void deleteNotification(@PathVariable("id") UUID id) {
        notificationService.deleteNotification(id);
    }
}
