package org.spica.javaclient.event;

import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.Model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class EventDetailsBuilder {

    private Model model;

    public EventDetails getDurationDetails () {

        Duration durationWork = Duration.of(0, ChronoUnit.MINUTES);
        Duration durationPause = Duration.of(0, ChronoUnit.MINUTES);


        for (EventInfo next: model.getEventInfosRealToday()) {
            LocalDateTime start = next.getStart();
            LocalDateTime stop = next.getStop() != null ? next.getStop() : LocalDateTime.now();

            if (next.getEventType() != null && next.getEventType().equals(EventType.PAUSE)) {
                durationPause = durationPause.plus(Duration.between(start, stop));
            }
            else {
                durationWork = durationWork.plus(Duration.between(start, stop));
            }
        }

        EventDetails eventDetails = new EventDetails();
        eventDetails.setDurationPause(durationPause);
        eventDetails.setDurationWork(durationWork);

        return eventDetails;

    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
