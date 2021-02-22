package org.spica.javaclient.timetracker;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.TaskInfo;

public class TimetrackerCreationParamTest {

    @Test
    public void dateIsNull () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
            timetrackerCreationParam.validate();
        });
    }

    @Test
    public void fromIsNull () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
            timetrackerCreationParam.setDate(LocalDate.now());
            timetrackerCreationParam.validate();
        });
    }

    public void eventTypeIsNull () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.validate();});

    }

    public void topicNoTaskInfo () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
            timetrackerCreationParam.setDate(LocalDate.now());
            timetrackerCreationParam.setFrom(LocalTime.now());
            timetrackerCreationParam.setEventType(EventType.TOPIC);
            timetrackerCreationParam.validate();
        });

    }

    @Test
    public void topicValid () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.setEventType(EventType.TOPIC);
        timetrackerCreationParam.setTaskInfo(new TaskInfo());
        timetrackerCreationParam.validate();
    }

    @Test
    public void topicMessageSet () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
            timetrackerCreationParam.setDate(LocalDate.now());
            timetrackerCreationParam.setFrom(LocalTime.now());
            timetrackerCreationParam.setEventType(EventType.TOPIC);
            timetrackerCreationParam.setTaskInfo(new TaskInfo());
            timetrackerCreationParam.setMessageInfo(new MessageInfo());
            timetrackerCreationParam.validate();
        });
    }

    @Test
    public void pause () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.setEventType(EventType.PAUSE);
        timetrackerCreationParam.validate();

    }

    @Test
    public void messageNoMessage () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
            timetrackerCreationParam.setDate(LocalDate.now());
            timetrackerCreationParam.setFrom(LocalTime.now());
            timetrackerCreationParam.setEventType(EventType.MESSAGE);
            timetrackerCreationParam.validate();
        });

    }

    @Test
    public void messageValid () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.setMessageInfo(new MessageInfo());
        timetrackerCreationParam.setEventType(EventType.MESSAGE);
        timetrackerCreationParam.validate();
    }

    @Test
    public void messageTaskSet () {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
            timetrackerCreationParam.setDate(LocalDate.now());
            timetrackerCreationParam.setFrom(LocalTime.now());
            timetrackerCreationParam.setTaskInfo(new TaskInfo());
            timetrackerCreationParam.setEventType(EventType.MESSAGE);
            timetrackerCreationParam.validate();
        });
    }
}
