package org.spica.javaclient.timetracker;

import org.junit.Test;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.TaskInfo;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimetrackerCreationParamTest {

    @Test(expected = IllegalArgumentException.class)
    public void dateIsNull () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromIsNull () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void eventTypeIsNull () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.validate();

    }

    @Test(expected = IllegalArgumentException.class)
    public void topicNoTaskInfo () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.setEventType(EventType.TOPIC);
        timetrackerCreationParam.validate();

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

    @Test(expected = IllegalArgumentException.class)
    public void topicMessageSet () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.setEventType(EventType.TOPIC);
        timetrackerCreationParam.setTaskInfo(new TaskInfo());
        timetrackerCreationParam.setMessageInfo(new MessageInfo());
        timetrackerCreationParam.validate();
    }

    @Test
    public void pause () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.setEventType(EventType.PAUSE);
        timetrackerCreationParam.validate();

    }

    @Test(expected = IllegalArgumentException.class)
    public void messageNoMessage () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.setEventType(EventType.MESSAGE);
        timetrackerCreationParam.validate();

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

    @Test(expected = IllegalArgumentException.class)
    public void messageTaskSet () {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(LocalTime.now());
        timetrackerCreationParam.setTaskInfo(new TaskInfo());
        timetrackerCreationParam.setEventType(EventType.MESSAGE);
        timetrackerCreationParam.validate();
    }
}
