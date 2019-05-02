package org.spica.javaclient.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class DevClientDemoDataCreator {

    private final static Logger LOGGER = LoggerFactory.getLogger(DevClientDemoDataCreator.class);


    public final static void main (final String [] args) {

        SpicaProperties.setSpicaHome(new File("").getAbsoluteFile());
        LocalDate today = LocalDate.now();

        ModelCacheService modelCacheService = new ModelCacheService();
        ModelCache modelCache = modelCacheService.get();
        EventInfo task1 = new EventInfo().name("Task 1").start(LocalDateTime.of(today, LocalTime.of(8,0))).stop(LocalDateTime.of(today, LocalTime.of(10,0))).eventType(EventType.TOPIC);
        EventInfo pause1 = new EventInfo().name("Pause").start(LocalDateTime.of(today, LocalTime.of(10,0))).stop(LocalDateTime.of(today, LocalTime.of(10,30))).eventType(EventType.PAUSE);
        EventInfo task2 = new EventInfo().name("Task 2").start(LocalDateTime.of(today, LocalTime.of(10,30))).stop(LocalDateTime.of(today, LocalTime.of(12,0))).eventType(EventType.TOPIC);
        EventInfo pause2 = new EventInfo().name("Pause2").start(LocalDateTime.of(today, LocalTime.of(12,0))).stop(LocalDateTime.of(today, LocalTime.of(13,30))).eventType(EventType.PAUSE);
        EventInfo task3 = new EventInfo().name("Task 3").start(LocalDateTime.of(today, LocalTime.of(13,0))).stop(LocalDateTime.of(today, LocalTime.of(17,0))).eventType(EventType.TOPIC);

        modelCache.setEventInfosReal(new ArrayList<>(Arrays.asList(task1, pause1, task2, pause2, task3)));

        TopicInfo topicInfo1 = new TopicInfo().name("Performance Server").id("1").description("Measure and improve the performance of the server").externalSystemID("JIRA").externalSystemKey("PROJECT-123");
        TopicInfo topicInfo2 = new TopicInfo().name("Make tests").id("2").description("Unittests, Integrationtests, and so on...").externalSystemID("JIRA").externalSystemKey("PROJECT-145");
        TopicInfo topicInfo3= new TopicInfo().name("Long topic name lorem ipsum blabla huedlschnue and so on").id("3").description("Make very much very important stuff").externalSystemID("JIRA").externalSystemKey("PROJECT-145");
        modelCache.setTopicInfos(new ArrayList<>(Arrays.asList(topicInfo1, topicInfo2, topicInfo3)));

        UserInfo userInfo1 = new UserInfo().name("User1").firstname("Peter").id("1").phone("0815").email("peter.user1@spica.org");
        UserInfo userInfo2 = new UserInfo().name("User2").firstname("Corinna").id("2").phone("0816").email("corinna.user2@spica.org");

        modelCache.setUserInfos(new ArrayList<>(Arrays.asList(userInfo1, userInfo2)));





        modelCacheService.set(modelCache);

    }
}
