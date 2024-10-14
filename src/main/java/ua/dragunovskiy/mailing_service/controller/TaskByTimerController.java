package ua.dragunovskiy.mailing_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dragunovskiy.mailing_service.sender.SenderType;
import ua.dragunovskiy.mailing_service.timemechanism.check.SimpleCheckNotifications;
import ua.dragunovskiy.mailing_service.timemechanism.filter.SimpleFilterNotifications;
import ua.dragunovskiy.mailing_service.timemechanism.timer.TaskByTimer;

import java.util.List;

@RestController
@RequestMapping("/taskByTimer")
@RequiredArgsConstructor
public class TaskByTimerController {

    private final SimpleCheckNotifications simpleCheckNotifications;
    private final SimpleFilterNotifications simpleFilterNotifications;
    private final List<SenderType> senderTypes = List.of(SenderType.MAIL, SenderType.TELEGRAM, SenderType.VIBER);
    @GetMapping("/timerTest")
    public void startTaskByTimer() {
//        new TimerForTask(simpleFilterNotifications, simpleCheckNotifications).checkInTime(5000, simpleCheckNotifications);
        new TaskByTimer(simpleFilterNotifications).checkInTimeWithSenderTypes(5000, simpleCheckNotifications, senderTypes);
    }
}
