package ua.dragunovskiy.mailing_service.timemechanism.timer;

import lombok.experimental.UtilityClass;
import ua.dragunovskiy.mailing_service.timemechanism.check.CheckNotifications;

import java.util.Timer;
import java.util.TimerTask;

@UtilityClass
public class TimerForTask {

    // every certain interval of time call method 'checkNotifications'
    // checkInTime take in arguments interval of time in milliseconds and any implementation of
    // interface CheckNotificationService
    public void checkInTime(int seconds, CheckNotifications checkNotificationsService) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Output every " + seconds + " seconds for testing");
                checkNotificationsService.checkNotifications();
            }
        }, 0, seconds);
    }
}
