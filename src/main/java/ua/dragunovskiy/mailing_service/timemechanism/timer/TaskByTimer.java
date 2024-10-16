package ua.dragunovskiy.mailing_service.timemechanism.timer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.dragunovskiy.mailing_service.sender.SenderType;
import ua.dragunovskiy.mailing_service.timemechanism.check.CheckNotifications;
import ua.dragunovskiy.mailing_service.timemechanism.filter.SimpleFilterNotifications;
import ua.dragunovskiy.mailing_service.util.Time;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@Component
@RequiredArgsConstructor
public class TaskByTimer {

    private final SimpleFilterNotifications filter;

    // every certain interval of time call method 'checkNotifications'
    // checkInTime take in arguments interval of time in milliseconds and any implementation of
    // interface CheckNotificationService
    public void checkInTime(int intervalInMilliSeconds, CheckNotifications checkNotifications) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkNotifications.checkNotificationsForSend(filter);
            }
        }, 0, intervalInMilliSeconds);
    }

    public void checkInTimeWithSenderTypes(int intervalInMilliSeconds, CheckNotifications checkNotifications, List<SenderType> senderTypes) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("checkInTimeWithSenderTypes update every 3 minutes... - " + Time.getCurrentTime());
                checkNotifications.checkNotificationsForSendWithSenderTypes(filter, senderTypes);
            }
        }, 0, intervalInMilliSeconds);
    }
}
