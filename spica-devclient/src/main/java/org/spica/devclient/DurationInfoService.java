package org.spica.devclient;

import org.spica.javaclient.model.EventInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DurationInfoService {

    public String getDisplaytext (final EventInfo eventInfo) {
        LocalDateTime startDateTime = eventInfo.getStart();
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime( FormatStyle.MEDIUM ).withLocale( Locale.UK ).withZone( ZoneId.systemDefault());

        String startTime = formatter.format(startDateTime);

        Duration duration = Duration.between(startDateTime, now);

        if (duration.get(ChronoUnit.SECONDS) < 60) {
            String info = "started some moments ago";
            return info;
        }
        else {
            String durationString = AmountFormats.wordBased(duration, Locale.UK);
            String info = "started at " + startTime + ", running for " + durationString;
            return info;
        }





    }
}
