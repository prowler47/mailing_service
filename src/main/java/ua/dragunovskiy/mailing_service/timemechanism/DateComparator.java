package ua.dragunovskiy.mailing_service.timemechanism;

import lombok.experimental.UtilityClass;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.util.FromStringToDateParser;

import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class DateComparator {

        // compare date of notifications table and current date. if difference
        // between these dates less than period - it will return true.
        // period in minutes
        public boolean compareDate(Notification notification, int period) {
        period = period * 60000;
        String notificationDate = notification.getDate();
        String notificationDateWithoutTime = notificationDate.split(" ")[0];
        String notificationDateTime = notificationDate.split(" ")[1];

        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateWithoutTime = simpleDateFormat.format(currentDate).split(" ")[0];
        String currentDateTime = simpleDateFormat.format(currentDate).split(" ")[1];

        if (notificationDateWithoutTime.equals(currentDateWithoutTime)) {
                Date parseNotificationDateTime = FromStringToDateParser.parseTime(notificationDateTime);
                Date parseCurrentDateTime = FromStringToDateParser.parseTime(currentDateTime);
                if (parseNotificationDateTime.after(parseCurrentDateTime)) {
                        long timeDifference = parseNotificationDateTime.getTime() - parseCurrentDateTime.getTime();
                        if (timeDifference <= period) {
                                return true;
                        }
                }
        }
        return false;
    }
}
