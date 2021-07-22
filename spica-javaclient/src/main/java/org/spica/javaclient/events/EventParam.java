package org.spica.javaclient.events;

import lombok.Data;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.TaskInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class EventParam {

    private LocalTime from;

    private LocalTime until;

    private LocalDate date;

    private EventType eventType;

    private TaskInfo taskInfo;

    private MessageInfo messageInfo;

    private String topic;

    public void validate () {
        if (date == null)
            throw new IllegalArgumentException("Parameter date must be set");

        if (from == null)
            throw new IllegalArgumentException("Parameter from must be set");

        if (eventType == null)
            throw new IllegalArgumentException("Parameter eventType must be set");

        if (taskInfo != null && ! eventType.equals(EventType.TASK))
            throw new IllegalArgumentException("Taskinfo must not be set on event type " + eventType.getValue());

        if (messageInfo != null && ! eventType.equals(EventType.MESSAGE))
            throw new IllegalArgumentException("MessageInfo must not be set on event type " + eventType.getValue());


        if (taskInfo == null && eventType.equals(EventType.TASK))
            throw new IllegalArgumentException("TaskInfo must be set on event type " + eventType.getValue());

        if (messageInfo == null && eventType.equals(EventType.MESSAGE))
            throw new IllegalArgumentException("MessageInfo must be set on event type " + eventType.getValue());
    }

    public LocalDateTime getFromAsLocalDateTime () {
        return from != null ? LocalDateTime.of(date, from): null;
    }

    public LocalDateTime getUntilAsLocalDateTime () {
        return until != null ? LocalDateTime.of(date, until): null;
    }

}
