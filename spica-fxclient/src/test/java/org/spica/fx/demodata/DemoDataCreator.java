package org.spica.fx.demodata;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.services.ModelCacheService;

@Slf4j
public class DemoDataCreator {

  public static void main(String[] args) {

    log.info("Starting to create demo data in folder " + new File("").getAbsolutePath());

    Model model = new Model();

    EventInfo eventInfoMeeting = new EventInfo();
    eventInfoMeeting.setId(UUID.randomUUID().toString());
    eventInfoMeeting.setStart(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
    eventInfoMeeting.setStop(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
    eventInfoMeeting.setEventType(EventType.MEETING);
    eventInfoMeeting.setName("Very important meeting");

    EventInfo evenInfoPause = new EventInfo();
    evenInfoPause.setId(UUID.randomUUID().toString());
    evenInfoPause.setStart(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
    evenInfoPause.setStop(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0)));
    evenInfoPause.setEventType(EventType.PAUSE);

    EventInfo eventInfoTask = new EventInfo();
    eventInfoTask.setId(UUID.randomUUID().toString());
    eventInfoTask.setStart(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0)));
    eventInfoTask.setStop(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)));
    eventInfoTask.setEventType(EventType.TASK);
    eventInfoTask.setName("Did some work on JIRA-1");

    model.getEventInfosReal().addAll(Arrays.asList(eventInfoMeeting, evenInfoPause, eventInfoTask));

    ModelCacheService modelCacheService = new ModelCacheService();
    FileUtils.deleteQuietly(new File (".spica"));
    modelCacheService.setConfigFile(new File (".spica/config.xml"));
    modelCacheService.save(model, "Created with DemoDataCreator");


  }
}
