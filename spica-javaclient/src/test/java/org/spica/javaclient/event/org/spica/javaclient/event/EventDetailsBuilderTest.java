package org.spica.javaclient.event.org.spica.javaclient.event;

import org.junit.Assert;
import org.junit.Test;
import org.spica.javaclient.event.EventDetails;
import org.spica.javaclient.event.EventDetailsBuilder;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.Model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class EventDetailsBuilderTest {

    @Test
    public void details () {
        Model model = new Model();
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), LocalTime.of(9,0), EventType.TOPIC));
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0), EventType.PAUSE));
        model.getEventInfosReal().add(createEvent(LocalTime.of(10,0), LocalTime.of(10,30), EventType.MESSAGE));
        EventDetailsBuilder eventDetailsBuilder = new EventDetailsBuilder();
        eventDetailsBuilder.setModel(model);
        EventDetails eventDetails = eventDetailsBuilder.getDurationDetails();
        Assert.assertEquals(Duration.of(1, ChronoUnit.HOURS), eventDetails.getDurationPause());
        Assert.assertEquals(Duration.of(90, ChronoUnit.MINUTES), eventDetails.getDurationWork());
    }

    private EventInfo createEvent (final LocalTime from, final LocalTime until, final EventType eventType) {

        EventInfo eventInfo = new EventInfo().eventType(EventType.PAUSE).id(UUID.randomUUID().toString());
        eventInfo = eventInfo.start(LocalDateTime.of(LocalDate.now(), from));
        if (until != null)
            eventInfo = eventInfo.stop(LocalDateTime.of(LocalDate.now(), until));
        eventInfo.setEventType(eventType);
        return eventInfo;
    }
}
