package ua.dragunovskiy.mailing_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.service.NotificationService;
import ua.dragunovskiy.mailing_service.timemechanism.check.TestCheckNotificationsService;
import ua.dragunovskiy.mailing_service.timemechanism.filter.FilterNotifications;
import ua.dragunovskiy.mailing_service.timemechanism.timer.TimerForTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestCheckNotificationsService testCheckNotificationsService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FilterNotifications filterNotifications;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping("/timerTest")
    public void testTimer() {
        TimerForTask.checkInTime(5000, testCheckNotificationsService);
    }

    @PostMapping("/addNotification")
    public void addNewNotification() throws ParseException {
        notificationService.addNewNotification();
    }

    @PostMapping("/addNotificationJson")
    public void addNewNotificationFromJson(@RequestBody Notification notification) {
        notificationService.addNewNotification(notification);
    }

    @GetMapping("/date")
    public String currentDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

    @GetMapping("/filterNotifications")
    public List<Notification> getListOfNotificationsForSend() {
       return filterNotifications.filter();
    }
}
