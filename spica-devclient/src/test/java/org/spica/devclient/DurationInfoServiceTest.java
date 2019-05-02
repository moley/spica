package org.spica.devclient;

import org.junit.Test;
import org.spica.javaclient.model.EventInfo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DurationInfoServiceTest {

    @Test
    public void getInfo () {

        LocalDateTime start = LocalDateTime.now().minus(100, ChronoUnit.MINUTES);

        EventInfo eventInfo = new EventInfo().start(start);
        DurationInfoService durationInfoService = new DurationInfoService();
        String displayText = durationInfoService.getDisplaytext(eventInfo);
        System.out.println (displayText);

    }
}
