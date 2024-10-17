package ua.dragunovskiy.mailing_service.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class Time {
    public String getCurrentTime() {
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(currentDate);
    }

    public boolean timeComparator(String currentTime, String notificationTime) {
        String currentHours = currentTime.split(":")[0];
        String currentMinutes = currentTime.split(":")[1];
        String notificationHours = notificationTime.split(":")[0];
        String notificationMinutes = notificationTime.split(":")[1];
        if (Integer.parseInt(currentHours) == Integer.parseInt(notificationHours)) {
            return Integer.parseInt(currentMinutes) > Integer.parseInt(notificationMinutes) + 2;
        }
        if (Integer.parseInt(currentHours) > Integer.parseInt(notificationHours)) {
            return Integer.parseInt(currentMinutes) >= 2;
        }
        return false;
    }
}
