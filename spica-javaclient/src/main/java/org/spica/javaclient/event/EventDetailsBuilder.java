package org.spica.javaclient.event;

import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.ModelCache;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class EventDetailsBuilder {

    private ModelCache modelCache;

    public EventDetails getDurationDetails () {

        Duration durationWork = Duration.of(0, ChronoUnit.MINUTES);
        Duration durationPause = Duration.of(0, ChronoUnit.MINUTES);


        for (EventInfo next: modelCache.getEventInfosRealToday()) {
            LocalDateTime start = next.getStart();
            LocalDateTime stop = next.getStop() != null ? next.getStop() : LocalDateTime.now();

            if (next.getEventType().equals(EventType.PAUSE)) {
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

    public ModelCache getModelCache() {
        return modelCache;
    }

    public void setModelCache(ModelCache modelCache) {
        this.modelCache = modelCache;
    }
}
