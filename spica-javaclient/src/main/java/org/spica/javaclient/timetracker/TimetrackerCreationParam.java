package org.spica.javaclient.timetracker;

import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.TopicInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimetrackerCreationParam {

    private LocalTime from;

    private LocalTime until;

    private LocalDate date;

    private EventType eventType;

    private TopicInfo topicInfo;


    private MessageInfo messageInfo;

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getUntil() {
        return until;
    }

    public void setUntil(LocalTime until) {
        this.until = until;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public void setTopicInfo(TopicInfo topicInfo) {
        this.topicInfo = topicInfo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void validate () {
        if (date == null)
            throw new IllegalArgumentException("Parameter date must be set");

        if (from == null)
            throw new IllegalArgumentException("Parameter from must be set");

        if (eventType == null)
            throw new IllegalArgumentException("Parameter eventType must be set");

        if (topicInfo != null && ! eventType.equals(EventType.TOPIC))
            throw new IllegalArgumentException("Topicinfo must not be set on event type " + eventType.getValue());

        if (messageInfo != null && ! eventType.equals(EventType.MESSAGE))
            throw new IllegalArgumentException("MessageInfo must not be set on event type " + eventType.getValue());


        if (topicInfo == null && eventType.equals(EventType.TOPIC))
            throw new IllegalArgumentException("TopicInfo must be set on event type " + eventType.getValue());

        if (messageInfo == null && eventType.equals(EventType.MESSAGE))
            throw new IllegalArgumentException("MessageInfo must be set on event type " + eventType.getValue());
    }

    public LocalDateTime getFromAsLocalDateTime () {
        return from != null ? LocalDateTime.of(date, from): null;
    }

    public LocalDateTime getUntilAsLocalDateTime () {
        return until != null ? LocalDateTime.of(date, until): null;
    }

    public void setMessageInfo(MessageInfo messageInfo) {
        this.messageInfo = messageInfo;
    }

    public MessageInfo getMessageInfo() {
        return messageInfo;
    }

}
