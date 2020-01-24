package org.spica.javaclient.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ModelTest {

    private LocalDate today = LocalDate.now();

    @Test
    public void findEventBefore () {
        EventInfo event1 = new EventInfo().eventType(EventType.TOPIC).start(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 8,0));
        Model model = new Model();
        model.getEventInfosReal().add(event1);

        Assert.assertNull (
            model.findEventBefore(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 7, 0)));
        Assert.assertNull (
            model.findEventBefore(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 8, 0)));
        Assert.assertNotNull (
            model.findEventBefore(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 9, 0)));
    }

    @Test
    public void findEventAfter () {
        EventInfo event1 = new EventInfo().eventType(EventType.TOPIC).start(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 8,0));
        Model model = new Model();
        model.getEventInfosReal().add(event1);

        Assert.assertNotNull (
            model.findEventAfter(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 7, 0)));
        Assert.assertNull (
            model.findEventAfter(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 8, 0)));
        Assert.assertNull (
            model.findEventAfter(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 9, 0)));
    }

}
