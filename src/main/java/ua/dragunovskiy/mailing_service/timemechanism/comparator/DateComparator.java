package ua.dragunovskiy.mailing_service.timemechanism.comparator;

import lombok.experimental.UtilityClass;
import ua.dragunovskiy.mailing_service.entity.Notification;
import ua.dragunovskiy.mailing_service.util.FromStringToDateParser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class DateComparator {

    // compare date of notifications table and current date. if difference
    // between these dates less than period - it will return true.
    // period in minutes
    public boolean compareActualDate(Notification notification, int period) {
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
                return timeDifference <= period;
            }
        }
        return false;
    }

    // find notifications with overdue date in principle:
    // 1. if notification year less than current year - return true,
    // if notification year greater or equal than current year - return false
    //
    // 2. if notification month less than current month - return true,
    // if notification month greater or equal than current month - return false
    //
    // 3. if notification day less than current day - return true,
    // if notification day greater or equal than current day - return false
    public boolean compareOverdueDate(Notification notification) {
        Date currentDate = new Date();
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        var currentYear = currentCalendar.get(Calendar.YEAR);
        var currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
        var currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

        Date notificationDate = FromStringToDateParser.parse(notification.getDate());
        Calendar notificationCalendar = Calendar.getInstance();
        notificationCalendar.setTime(notificationDate);
        var notificationYear = notificationCalendar.get(Calendar.YEAR);
        var notificationMonth = notificationCalendar.get(Calendar.MONTH) + 1;
        var notificationDay = notificationCalendar.get(Calendar.DAY_OF_MONTH);

        if (notificationYear > currentYear) {
            return false;
        } else if (notificationYear < currentYear) {
            System.out.println("need to delete from year");
            return true;
        } else {
            if (notificationMonth > currentMonth) {
                return false;
            } else if (notificationMonth < currentMonth) {
                System.out.println("need to delete from month");
                return true;
            } else {
                if (notificationDay > currentDay) {
                    return false;
                } else if (notificationDay < currentDay) {
                    System.out.println("need to delete from day");
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}

