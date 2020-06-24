package org.spica.javaclient.timetracker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spica.javaclient.model.*;

import java.time.*;
import java.util.UUID;
import org.spica.javaclient.services.ModelCacheService;

public class TimetrackerServiceTest {

    private TimetrackerService timetrackerService = new TimetrackerService();
    private ModelCacheService modelCacheService = new ModelCacheService();


    @Before
    public void before () {
        modelCacheService.close();
        modelCacheService.set(new Model(), "create new model");
        timetrackerService.setModelCacheService(modelCacheService);

    }

    private EventInfo createEvent (final LocalTime from, final LocalTime until) {

        EventInfo eventInfo = new EventInfo().eventType(EventType.PAUSE).id(UUID.randomUUID().toString());
        eventInfo = eventInfo.start(LocalDateTime.of(LocalDate.now(), from));
        if (until != null)
            eventInfo = eventInfo.stop(LocalDateTime.of(LocalDate.now(), until));
        return eventInfo;
    }

    private TimetrackerCreationParam createCreationParam (final LocalTime from, final LocalTime until) {
        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setFrom(from);
        timetrackerCreationParam.setUntil(until);
        timetrackerCreationParam.setEventType(EventType.TOPIC);
        timetrackerCreationParam.setTaskInfo(new TaskInfo().name("New event"));
        return timetrackerCreationParam;
    }

    @Test
    public void createEventAfterLastFinishedEvent() {

        Model model = modelCacheService.get();
        //08:00 - 09:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), LocalTime.of(9,0)));

        //->09:00 - 10:00
        timetrackerService.createEvent(createCreationParam(LocalTime.of(9,0), LocalTime.of(10,0)));

        //Check
        //08:00 - 09:00
        //09:00 - 10:00
        Assert.assertEquals (2, model.getEventInfosRealToday().size());
        Assert.assertEquals (LocalTime.of(8,0), model.getEventInfosRealToday().get(0).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(1).getStart().toLocalTime());
    }

    @Test
    public void createEventWithPreviousOriginNotFinished () {
        Model model = modelCacheService.get();

        //09:00 -
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), null));

        //->08:00 - 09:00
        timetrackerService.createEvent(createCreationParam(LocalTime.of(8,0), LocalTime.of(9,0)));

        System.out.println (model.getEventInfosRealToday());

        //Check
        //08:00 - 09:00
        //09:00 -
        Assert.assertEquals (2, model.getEventInfosRealToday().size());
        Assert.assertEquals (LocalTime.of(8,0), model.getEventInfosRealToday().get(0).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(0).getStop().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(1).getStart().toLocalTime());
        Assert.assertNull (model.getEventInfosRealToday().get(1).getStop());
    }

    @Test
    public void createEventAfterLastNotFinishedEvent () {
        Model model = modelCacheService.get();

        //8:00 -
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), null));

        //->09:00 - 10:00
        timetrackerService.createEvent(createCreationParam(LocalTime.of(9,0), LocalTime.of(10,0)));

        //Check
        //08:00 - 09:00
        //09:00 - 10:00
        Assert.assertEquals (2, model.getEventInfosRealToday().size());
        Assert.assertEquals (LocalTime.of(8,0), model.getEventInfosRealToday().get(0).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(0).getStop().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(1).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(10,0), model.getEventInfosRealToday().get(1).getStop().toLocalTime());
    }

    @Test
    public void createEventBeforeFirstEvent () {
        Model model = modelCacheService.get();
        //09:00 - 10:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0)));


        //->08:00 -
        timetrackerService.createEvent(createCreationParam(LocalTime.of(8,0), null));

        //08:00 - 09:00
        //09:00 - 10:00
        Assert.assertEquals (2, model.getEventInfosRealToday().size());
        Assert.assertEquals (LocalTime.of(8,0), model.getEventInfosRealToday().get(0).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(0).getStop().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(1).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(10, 0), model.getEventInfosRealToday().get(1).getStop().toLocalTime());

    }

    @Test(expected = IllegalStateException.class)
    public void createEventBeforeFirstEventGap () {
        Model model = modelCacheService.get();

        //09:00 - 10:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0)));

        //->08:00 - 08:30
        timetrackerService.createEvent(createCreationParam(LocalTime.of(8,0), LocalTime.of(8,30)));

        //->Error Gap
    }

    @Test(expected = IllegalStateException.class)
    public void createEventBeforeFirstEventHidingOrigin () {
        Model model = modelCacheService.get();

        //09:00 - 10:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0)));

        //->08:00 - 11:30
        timetrackerService.createEvent(createCreationParam(LocalTime.of(8,0), LocalTime.of(11,0)));


        //->Error Overlapping

    }

    @Test
    public void createEventSplittingOrigin () {
        Model model = modelCacheService.get();

        //08:00 - 09:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), LocalTime.of(9,0)));


        //->08:30 - 09:00
        timetrackerService.createEvent(createCreationParam(LocalTime.of(8,30), LocalTime.of(9,0)));


        //08:00 - 08:30
        //08:30 - 09:00
        Assert.assertEquals (2, model.getEventInfosRealToday().size());
        Assert.assertEquals (LocalTime.of(8,0), model.getEventInfosRealToday().get(0).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(8,30), model.getEventInfosRealToday().get(0).getStop().toLocalTime());
        Assert.assertEquals (LocalTime.of(8,30), model.getEventInfosRealToday().get(1).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(9, 0), model.getEventInfosRealToday().get(1).getStop().toLocalTime());

    }

    @Test
    public void createEventOverlapping () {
        Model model = modelCacheService.get();

        //08:00 - 09:00
        //09:00 - 10:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), LocalTime.of(9,0)));
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0)));

        //->08:30 - 09:30
        timetrackerService.createEvent(createCreationParam(LocalTime.of(8,30), LocalTime.of(9,30)));

        //08:00 - 08:30
        //08:30 - 09:30
        //09:30 - 10:00
        Assert.assertEquals (3, model.getEventInfosRealToday().size());
        Assert.assertEquals (LocalTime.of(8,0), model.getEventInfosRealToday().get(0).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(8,30), model.getEventInfosRealToday().get(0).getStop().toLocalTime());
        Assert.assertEquals (LocalTime.of(8,30), model.getEventInfosRealToday().get(1).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(9, 30), model.getEventInfosRealToday().get(1).getStop().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,30), model.getEventInfosRealToday().get(2).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(10, 0), model.getEventInfosRealToday().get(2).getStop().toLocalTime());
    }

    @Test(expected = IllegalStateException.class)
    public void createEventOverlappingHidingOtherEvent () {
        Model model = modelCacheService.get();

        //08:00 - 09:00
        //09:00 - 10:00
        //10:00 - 11:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), LocalTime.of(9,0)));
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0)));
        model.getEventInfosReal().add(createEvent(LocalTime.of(10,0), LocalTime.of(11,0)));

        //->08:30 - 09:30
        timetrackerService.createEvent(createCreationParam(LocalTime.of(8,30), LocalTime.of(10,30)));
    }
}
