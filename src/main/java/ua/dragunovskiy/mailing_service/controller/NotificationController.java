package ua.dragunovskiy.mailing_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.dragunovskiy.mailing_service.dto.NotificationDto;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.repository.NotificationDao;
import ua.dragunovskiy.mailing_service.service.NotificationService;
import ua.dragunovskiy.mailing_service.timemechanism.filter.SimpleFilterNotifications;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpleFilterNotifications filterNotifications;

    @GetMapping("/getAllNotifications")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/getById/{id}")
    public Notification getById(@PathVariable("id") UUID id) {
        return notificationService.getNotificationById(id);
    }

    @PostMapping("/addNotification")
    public void addNotification(@RequestBody Notification notification) {
        notificationDao.save(notification);
    }

    @PostMapping("/deleteOverdueNotifications")
    public void findAndDeleteOverdueNotifications() {
        filterNotifications.filterForDelete();
    }

    @DeleteMapping("/deleteNotification/{id}")
    public void deleteNotification(@PathVariable("id") UUID id) {
        notificationService.deleteNotification(id);
    }
}
