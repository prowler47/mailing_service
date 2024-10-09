package ua.dragunovskiy.mailing_service.util;

import lombok.experimental.UtilityClass;

import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class FromStringToDateParser {
    public Date parse(String stringDate) {
        String[] split = stringDate.split(" ");
        String date = split[0];
        String time = split[1];

        String[] splitDate = date.split("-");
        var year = splitDate[0];
        var month = Integer.parseInt(splitDate[1]) - 1;
        var day = splitDate[2];

        String[] splitTime = time.split(":");
        var hours = splitTime[0];
        var minutes = splitTime[1];

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hours));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minutes));

        return calendar.getTime();
    }

    public Date parseTime(String time) {
        String[] splitTime = time.split(":");
        var hours = splitTime[0];
        var minutes = splitTime[1];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hours));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minutes));
        return calendar.getTime();
    }

    public Date parseDateWithoutTime(String date) {
        String[] splitDate = date.split("-");
        String year = splitDate[0];
        int month = Integer.parseInt(splitDate[1]) - 1;
        String day = splitDate[2];

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        return calendar.getTime();
    }
}
