package ua.dragunovskiy.mailing_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.sender.SenderType;
import ua.dragunovskiy.mailing_service.sender.SimpleMailSender;
import ua.dragunovskiy.mailing_service.service.NotificationService;
import ua.dragunovskiy.mailing_service.service.TestMailSender;
import ua.dragunovskiy.mailing_service.timemechanism.check.SimpleCheckNotifications;
import ua.dragunovskiy.mailing_service.timemechanism.comparator.DateComparator;
import ua.dragunovskiy.mailing_service.timemechanism.filter.SimpleFilterNotifications;
import ua.dragunovskiy.mailing_service.timemechanism.timer.TaskByTimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final SimpleCheckNotifications simpleCheckNotifications;
    private final NotificationService notificationService;
    private final SimpleFilterNotifications simpleFilterNotifications;

    private final TestMailSender testMailSender;
    private final List<SenderType> senderTypes = List.of(SenderType.MAIL, SenderType.TELEGRAM, SenderType.VIBER);

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping("/timerTest")
    public void testTimer() {
//        new TimerForTask(simpleFilterNotifications, simpleCheckNotifications).checkInTime(5000, simpleCheckNotifications);
        new TaskByTimer(simpleFilterNotifications).checkInTimeWithSenderTypes(5000, simpleCheckNotifications, senderTypes);
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
        return simpleFilterNotifications.filterForSending();
    }

    @GetMapping("/compareTest")
    public String getOverdueCompare() {
        Notification notification = new Notification();
        notification.setTitle("test");
        notification.setPayload("test");
        notification.setDate("2024-10-09 10:23");
        if (DateComparator.compareOverdueDate(notification)) {
            System.out.println("need to delete");
        } else System.out.println("nothing to delete");
        return null;
    }

    @GetMapping("sendEmail")
    public String testSendEmail() {
        testMailSender.sendMail();
        return "mail sender send message";
    }
}
