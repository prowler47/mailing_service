package ua.dragunovskiy.mailing_service.util;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class FromDatetimeLocalToStringParser {
    public String parse(String date) {
        return date.replace('T', ' ');
    }
}
