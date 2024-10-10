package ua.dragunovskiy.mailing_service.timemechanism.timer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.dragunovskiy.mailing_service.timemechanism.check.CheckNotifications;
import ua.dragunovskiy.mailing_service.timemechanism.filter.SimpleFilterNotifications;

import java.util.Timer;
import java.util.TimerTask;


@Component
@RequiredArgsConstructor
public class TimerForTask {

    private final SimpleFilterNotifications filter;

    // every certain interval of time call method 'checkNotifications'
    // checkInTime take in arguments interval of time in milliseconds and any implementation of
    // interface CheckNotificationService
    public void checkInTime(int seconds, CheckNotifications checkNotifications) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                System.out.println("Output every " + seconds + " seconds for testing");
                checkNotifications.checkNotificationsForSend(filter);
            }
        }, 0, seconds);
    }
}
