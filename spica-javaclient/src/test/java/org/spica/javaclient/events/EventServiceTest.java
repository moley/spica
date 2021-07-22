package org.spica.javaclient.events;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.services.ModelCacheService;

public class EventServiceTest {

    private EventService eventService = new EventService();
    private ModelCacheService modelCacheService = new ModelCacheService();


    @BeforeEach
    public void before () {
        modelCacheService.save(new Model(), "create new model");
        eventService.setModelCacheService(modelCacheService);

    }

    private EventInfo createEvent (final LocalDateTime from, final LocalDateTime until) {

        EventInfo eventInfo = new EventInfo().eventType(EventType.PAUSE).id(UUID.randomUUID().toString());
        eventInfo = eventInfo.start(from);
        if (until != null)
            eventInfo = eventInfo.stop(until);
        return eventInfo;
    }

    private EventInfo createEvent (final LocalTime from, final LocalTime until) {

        EventInfo eventInfo = new EventInfo().eventType(EventType.PAUSE).id(UUID.randomUUID().toString());
        eventInfo = eventInfo.start(LocalDateTime.of(LocalDate.now(), from));
        if (until != null)
            eventInfo = eventInfo.stop(LocalDateTime.of(LocalDate.now(), until));
        return eventInfo;
    }

    private EventParam createCreationParam (final LocalTime from, final LocalTime until) {
        EventParam eventParam = new EventParam();
        eventParam.setDate(LocalDate.now());
        eventParam.setFrom(from);
        eventParam.setUntil(until);
        eventParam.setEventType(EventType.TASK);
        eventParam.setTaskInfo(new TaskInfo().name("New event"));
        return eventParam;
    }

    private EventParam createCreationParam (final LocalDateTime from, final LocalDateTime until) {
        if (! from.toLocalDate().equals(until.toLocalDate()))
            throw new IllegalStateException("From and until do not match the same date");

        EventParam eventParam = new EventParam();
        eventParam.setDate(from.toLocalDate());
        eventParam.setFrom(from.toLocalTime());
        eventParam.setUntil(until.toLocalTime());
        eventParam.setEventType(EventType.TASK);
        eventParam.setTaskInfo(new TaskInfo().name("New event"));
        return eventParam;
    }

    @Test
    public void createEventAfterLastFinishedEvent() {

        Model model = modelCacheService.load();
        //08:00 - 09:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), LocalTime.of(9,0)));

        //->09:00 - 10:00
        eventService.createEvent(createCreationParam(LocalTime.of(9,0), LocalTime.of(10,0)));

        //Check
        //08:00 - 09:00
        //09:00 - 10:00
        Assert.assertEquals (2, model.getEventInfosRealToday().size());
        Assert.assertEquals (LocalTime.of(8,0), model.getEventInfosRealToday().get(0).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(1).getStart().toLocalTime());
    }

    @Test
    public void createEventWithPreviousOriginNotFinished () {
        Model model = modelCacheService.load();

        //09:00 -
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), null));

        //->08:00 - 09:00
        eventService.createEvent(createCreationParam(LocalTime.of(8,0), LocalTime.of(9,0)));

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
        Model model = modelCacheService.load();

        //8:00 -
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), null));

        //->09:00 - 10:00
        eventService.createEvent(createCreationParam(LocalTime.of(9,0), LocalTime.of(10,0)));

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
        Model model = modelCacheService.load();
        //09:00 - 10:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0)));


        //->08:00 -
        eventService.createEvent(createCreationParam(LocalTime.of(8,0), null));

        //08:00 - 09:00
        //09:00 - 10:00
        Assert.assertEquals (2, model.getEventInfosRealToday().size());
        Assert.assertEquals (LocalTime.of(8,0), model.getEventInfosRealToday().get(0).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(0).getStop().toLocalTime());
        Assert.assertEquals (LocalTime.of(9,0), model.getEventInfosRealToday().get(1).getStart().toLocalTime());
        Assert.assertEquals (LocalTime.of(10, 0), model.getEventInfosRealToday().get(1).getStop().toLocalTime());

    }

    @Test
    public void createEventBeforeFirstFinishedEventGap () {
        Model model = modelCacheService.load();

        //13.10. 09:00 -
        model.getEventInfosReal().add(createEvent(LocalDateTime.of(2020, 10, 13, 9,0), LocalDateTime.of(2020, 10, 13, 10,0)));

        //->12.10. 08:00 - 08:30
        eventService
            .createEvent(createCreationParam(LocalDateTime.of(2020, 10, 12, 8,0), LocalDateTime.of(2020, 10, 12, 8,30)));

    }

    @Test
    public void createEventBeforeFirstNotFinishedEventGap () {
        Model model = modelCacheService.load();

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);

        //13.10. 09:00 -
        model.getEventInfosReal().add(createEvent(LocalDateTime.of(today, LocalTime.of(9,0)), null));

        //->12.10. 08:00 - 08:30
        eventService.createEvent(createCreationParam(LocalDateTime.of(yesterday, LocalTime.of(8,0)), LocalDateTime.of(yesterday, LocalTime.of(8,30))));

    }

    @Test
    public void createEventBeforeFirstEventGap () {
        Model model = modelCacheService.load();

        //09:00 - 10:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0)));

        //->08:00 - 08:30
        eventService.createEvent(createCreationParam(LocalTime.of(8,0), LocalTime.of(8,30)));
    }

    @Test
    public void createEventBeforeFirstEventHidingOrigin () {
        Assertions.assertThrows(IllegalStateException.class, ()-> {
            Model model = modelCacheService.load();

            //09:00 - 10:00
            model.getEventInfosReal().add(createEvent(LocalTime.of(9, 0), LocalTime.of(10, 0)));

            //->08:00 - 11:30
            eventService.createEvent(createCreationParam(LocalTime.of(8, 0), LocalTime.of(11, 0)));

            //->Error Overlapping
        });

    }

    @Test
    public void createEventSplittingOrigin () {
        Model model = modelCacheService.load();

        //08:00 - 09:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), LocalTime.of(9,0)));


        //->08:30 - 09:00
        eventService.createEvent(createCreationParam(LocalTime.of(8,30), LocalTime.of(9,0)));


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
        Model model = modelCacheService.load();

        //08:00 - 09:00
        //09:00 - 10:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), LocalTime.of(9,0)));
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0)));

        //->08:30 - 09:30
        eventService.createEvent(createCreationParam(LocalTime.of(8,30), LocalTime.of(9,30)));

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

    @Test
    public void createEventOverlappingHidingOtherEvent () {
        Assertions.assertThrows(IllegalStateException.class, () -> {
        Model model = modelCacheService.load();

        //08:00 - 09:00
        //09:00 - 10:00
        //10:00 - 11:00
        model.getEventInfosReal().add(createEvent(LocalTime.of(8,0), LocalTime.of(9,0)));
        model.getEventInfosReal().add(createEvent(LocalTime.of(9,0), LocalTime.of(10,0)));
        model.getEventInfosReal().add(createEvent(LocalTime.of(10,0), LocalTime.of(11,0)));

        //->08:30 - 09:30
        eventService.createEvent(createCreationParam(LocalTime.of(8,30), LocalTime.of(10,30)));
        });
    }
}
