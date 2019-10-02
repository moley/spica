package org.spica.javaclient.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

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

    public String getDateAsString(LocalDate localDate) {
        if (localDate == null)
            return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    public LocalDate getDate (final String dateAsString) {
        String trimmed = dateAsString.trim();

        LocalDate today = LocalDate.now();

        if (trimmed.length() == 4) {
            String day = dateAsString.substring(0, 2);
            String month = dateAsString.substring(2, 4);

            return LocalDate.of(today.getYear(), Integer.valueOf(month).intValue(), Integer.valueOf(day).intValue());
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");
            TemporalAccessor temporalAccessor = formatter.parse(trimmed);
            return LocalDate.from(temporalAccessor);
        }

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

    public String getDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        return String.format("%02d:%02d h", absSeconds / 3600, (absSeconds % 3600) / 60);
    }
}
