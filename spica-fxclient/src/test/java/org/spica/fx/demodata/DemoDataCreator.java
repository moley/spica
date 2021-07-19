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
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.services.ModelCacheService;

@Slf4j
public class DemoDataCreator {

  public DemoDataCreator () {

    log.info("Starting to create demo data in folder " + new File("").getAbsolutePath());

    Model model = new Model();

    createBookings(model);
    createUsers(model);
    createTasks (model);
    createProjects (model);



    ModelCacheService modelCacheService = new ModelCacheService();
    FileUtils.deleteQuietly(new File (".spica"));
    modelCacheService.setConfigFile(new File (".spica/config.xml"));
    modelCacheService.save(model, "Created with DemoDataCreator");

  }

  private void createTasks(Model model) {
    TaskInfo taskInfo1 = new TaskInfo();
    taskInfo1.setId(UUID.randomUUID().toString());
    taskInfo1.setName("Implement feature 1");

    TaskInfo subTask1 = new TaskInfo();
    subTask1.setId(UUID.randomUUID().toString());
    subTask1.setName("Implement subfeature 1-1");
    subTask1.setPlannedDate(LocalDate.now());
    subTask1.setParentId(taskInfo1.getId());

    TaskInfo subTask2 = new TaskInfo();
    subTask2.setId(UUID.randomUUID().toString());
    subTask2.setName("Implement subfeature 1-2");
    subTask2.setPlannedDate(LocalDate.now().plusDays(10)); //next week
    subTask2.setParentId(taskInfo1.getId());

    model.getTaskInfos().addAll(Arrays.asList(taskInfo1, subTask1, subTask2));
  }

  private void createProjects(Model model) {
    ProjectInfo projectInfo1 = new ProjectInfo();
    projectInfo1.setId(UUID.randomUUID().toString());
    projectInfo1.setName("Project 1");

    ProjectInfo subProject1 = new ProjectInfo();
    subProject1.setId(UUID.randomUUID().toString());
    subProject1.setName("SubProject 1-1");
    subProject1.setParentId(projectInfo1.getId());

    ProjectInfo subProject2 = new ProjectInfo();
    subProject2.setId(UUID.randomUUID().toString());
    subProject2.setName("SubProject 1-1");
    subProject2.setParentId(projectInfo1.getId());

    model.getProjectInfos().addAll(Arrays.asList(projectInfo1, subProject1, subProject2));

  }

  public static void main(String[] args) {
    new DemoDataCreator();



  }

  private void createBookings (final Model model) {
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

  }

  private void createUsers (final Model model) {
    UserInfo userInfo1 = new UserInfo();
    userInfo1.setName("Name1");
    userInfo1.setUsername("username1");
    userInfo1.setFirstname("Firestname2");
    userInfo1.setId(UUID.randomUUID().toString());

    UserInfo userInfo2 = new UserInfo();
    userInfo2.setName("Name2");
    userInfo2.setUsername("username2");
    userInfo2.setFirstname("Firstname2");
    userInfo2.setId(UUID.randomUUID().toString());

    model.getUserInfos().addAll(Arrays.asList(userInfo1, userInfo2));
  }
}
