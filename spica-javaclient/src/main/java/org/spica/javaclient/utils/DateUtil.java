package org.spica.javaclient.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtil {

    public int getDayOfWeek (LocalDate localDate) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = localDate.get(weekFields.weekOfWeekBasedYear());
        return weekNumber;
    }

    public String getDateAndTimeAsString(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YY HH:mm");
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YY");
        return localDateTime.format(formatter);
    }

    public String getDateAsString(LocalDate localDate) {
        if (localDate == null)
            return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        return localDate.format(formatter);
    }

    /**
     * gets date from string
     *
     * null -> now
     * 1208 -> 12.8.
     * 12.08 -> 12.8.[THIS YEAR]
     *
     * @param dateAsString
     * @return date as {@link LocalDate}
     */
    public LocalDate getDate (final String dateAsString) {
        if (dateAsString == null)
            return LocalDate.now();

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

    /**
     * gets time from string
     *
     * null -> now
     * 1234 -> 12:34
     * 12:34 -> 12:34
     *
     * @param dateAsString time from string
     *
     * @return time als {@link LocalTime}
     */
    public LocalTime getTime (final String dateAsString) {
        if (dateAsString == null)
            return LocalTime.now();

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
