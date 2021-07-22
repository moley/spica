package org.spica.javaclient.events;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.TaskInfo;

public class EventParamTest {

    @Test
    public void dateIsNull () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            EventParam eventParam = new EventParam();
            eventParam.validate();
        });
    }

    @Test
    public void fromIsNull () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            EventParam eventParam = new EventParam();
            eventParam.setDate(LocalDate.now());
            eventParam.validate();
        });
    }

    @Test
    public void eventTypeIsNull () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            EventParam eventParam = new EventParam();
        eventParam.setDate(LocalDate.now());
        eventParam.setFrom(LocalTime.now());
        eventParam.validate();});

    }

    @Test
    public void topicNoTaskInfo () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EventParam eventParam = new EventParam();
            eventParam.setDate(LocalDate.now());
            eventParam.setFrom(LocalTime.now());
            eventParam.setEventType(EventType.TASK);
            eventParam.validate();
        });

    }

    @Test
    public void topicValid () {
        EventParam eventParam = new EventParam();
        eventParam.setDate(LocalDate.now());
        eventParam.setFrom(LocalTime.now());
        eventParam.setEventType(EventType.TASK);
        eventParam.setTaskInfo(new TaskInfo());
        eventParam.validate();
    }

    @Test
    public void topicMessageSet () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            EventParam eventParam = new EventParam();
            eventParam.setDate(LocalDate.now());
            eventParam.setFrom(LocalTime.now());
            eventParam.setEventType(EventType.TASK);
            eventParam.setTaskInfo(new TaskInfo());
            eventParam.setMessageInfo(new MessageInfo());
            eventParam.validate();
        });
    }

    @Test
    public void pause () {
        EventParam eventParam = new EventParam();
        eventParam.setDate(LocalDate.now());
        eventParam.setFrom(LocalTime.now());
        eventParam.setEventType(EventType.PAUSE);
        eventParam.validate();

    }

    @Test
    public void messageNoMessage () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EventParam eventParam = new EventParam();
            eventParam.setDate(LocalDate.now());
            eventParam.setFrom(LocalTime.now());
            eventParam.setEventType(EventType.MESSAGE);
            eventParam.validate();
        });

    }

    @Test
    public void messageValid () {
        EventParam eventParam = new EventParam();
        eventParam.setDate(LocalDate.now());
        eventParam.setFrom(LocalTime.now());
        eventParam.setMessageInfo(new MessageInfo());
        eventParam.setEventType(EventType.MESSAGE);
        eventParam.validate();
    }

    @Test
    public void messageTaskSet () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EventParam eventParam = new EventParam();
            eventParam.setDate(LocalDate.now());
            eventParam.setFrom(LocalTime.now());
            eventParam.setTaskInfo(new TaskInfo());
            eventParam.setEventType(EventType.MESSAGE);
            eventParam.validate();
        });
    }
}
