package org.spica.javaclient.events;

import java.time.Duration;

public class EventDetails {

    private Duration durationWork;

    private Duration durationPause;


    public Duration getDurationPause() {
        return durationPause;
    }

    public void setDurationPause(Duration durationPause) {
        this.durationPause = durationPause;
    }

    public Duration getDurationWork() {
        return durationWork;
    }

    public void setDurationWork(Duration durationWork) {
        this.durationWork = durationWork;
    }
}
