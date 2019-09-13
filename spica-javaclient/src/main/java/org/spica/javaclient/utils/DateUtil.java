package org.spica.javaclient.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public String getDateAndTimeAsString(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }

    public String getTimeAsString(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }
}
