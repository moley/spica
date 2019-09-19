package org.spica.javaclient.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
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

    public String getDateAsString(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }

    public LocalTime getTime (final String dateAsString) {
        String trimmed = dateAsString.trim();

        if (trimmed.length() == 4) {
            String hour = dateAsString.substring(0, 2);
            String minute = dateAsString.substring(2, 4);
            return LocalTime.of(Integer.valueOf(hour).intValue(), Integer.valueOf(minute).intValue());
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.from(formatter.parse(trimmed));
        }

    }
}
