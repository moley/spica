package org.spica.javaclient.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class DevClientDemoDataCreator {

    private final static Logger LOGGER = LoggerFactory.getLogger(DevClientDemoDataCreator.class);


    public final static void main (final String [] args) {
        LocalDate today = LocalDate.now();

        ModelCacheService modelCacheService = new ModelCacheService();
        ModelCache modelCache = modelCacheService.get();
        EventInfo task1 = new EventInfo().name("Task 1").start(LocalDateTime.of(today, LocalTime.of(8,0))).stop(LocalDateTime.of(today, LocalTime.of(10,0))).eventType(EventType.TOPIC);
        EventInfo pause1 = new EventInfo().name("Pause").start(LocalDateTime.of(today, LocalTime.of(10,0))).stop(LocalDateTime.of(today, LocalTime.of(10,30))).eventType(EventType.PAUSE);
        EventInfo task2 = new EventInfo().name("Task 2").start(LocalDateTime.of(today, LocalTime.of(10,30))).stop(LocalDateTime.of(today, LocalTime.of(12,0))).eventType(EventType.TOPIC);
        EventInfo pause2 = new EventInfo().name("Pause2").start(LocalDateTime.of(today, LocalTime.of(12,0))).stop(LocalDateTime.of(today, LocalTime.of(13,30))).eventType(EventType.PAUSE);
        EventInfo task3 = new EventInfo().name("Task 3").start(LocalDateTime.of(today, LocalTime.of(13,0))).stop(LocalDateTime.of(today, LocalTime.of(17,0))).eventType(EventType.TOPIC);

        modelCache.setEventInfosReal(Arrays.asList(task1, pause1, task2, pause2, task3));





        modelCacheService.set(modelCache);

    }
}
