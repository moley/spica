package org.spica.commons;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtils {

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

    public String getDateAsStringLongFormat(LocalDate localDate) {
        if (localDate == null)
            return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        return localDate.format(formatter);
    }

    /**
     * gets date from string
     *
     * null  .... now
     * 1208  .... 12.8.
     * 12.08 .... 12.8.[THIS YEAR]
     *
     * @param dateAsString  the date as formatted string
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
        else if (trimmed.length() == 5) {
            String day = dateAsString.substring(0, 2);
            String month = dateAsString.substring(3, 5);

            return LocalDate.of(today.getYear(), Integer.valueOf(month).intValue(), Integer.valueOf(day).intValue());
        }
        else if (trimmed.length() == 10) {
            String day = dateAsString.substring(0, 2);
            String month = dateAsString.substring(3, 5);
            String year = dateAsString.substring(6, 10);
            return LocalDate.of(Integer.valueOf(year).intValue(), Integer.valueOf(month).intValue(), Integer.valueOf(day).intValue());

        }

        throw new IllegalStateException("Invalid date " + dateAsString + ", Please use date in format DDMM or DD.MM or DD.MM.YYYY");

    }

    /**
     * gets time from string
     *
     * null  .... now
     * 1234  .... 12:34
     * 12:34 .... 12:34
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
