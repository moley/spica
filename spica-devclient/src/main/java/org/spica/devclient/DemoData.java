package org.spica.devclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.model.BookingInfo;
import org.spica.javaclient.model.ProjectInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class DemoData {

  private final static Logger LOGGER = LoggerFactory.getLogger(DemoData.class);


  public static Collection<ProjectInfo> projectInfos = new ArrayList<>();

  public static Collection<BookingInfo> realEntries = new ArrayList<>();
  public static Collection<BookingInfo> plannedEntries = new ArrayList<>();

  public static void create () {
    createProjects();
    createCalenderEntries();

  }

  private static void createCalenderEntries() {
    LOGGER.info("Create calender entries");
    LocalDate today = LocalDate.now();
    //TODO
    // BookingInfo bookingInfo = new BookingInfo().name("TERMIN Codereview").start()
    /**plannedEntries.add(new Entry("TERMIN Codereview", new Interval(today, LocalTime.of(8,0), today, LocalTime.of(9,0))));
    plannedEntries.add(new Entry("JIRA Refactoring ChatClient", new Interval(today, LocalTime.of(9,0), today, LocalTime.of(14,0))));
    plannedEntries.add(new Entry("JIRA Bug 1234", new Interval(today, LocalTime.of(15,0), today, LocalTime.of(18,0))));

    realEntries.add(new Entry("TERMIN Codereview", new Interval(today, LocalTime.of(8,0), today, LocalTime.of(10,0))));
    realEntries.add(new Entry("JIRA Refactoring ChatClient", new Interval(today, LocalTime.of(10,0), today, LocalTime.of(14,0))));
    realEntries.add(new Entry("JIRA Bug 1234", new Interval(today, LocalTime.of(15,0), today, LocalTime.of(19,0))));**/
  }

  public static void createProjects () {
    LOGGER.info("Create project infos");

    ProjectInfo projectInfo1 = new ProjectInfo();
    projectInfo1.setId("1");
    projectInfo1.setName("Jenkins");

    ProjectInfo projectInfo11 = new ProjectInfo();
    projectInfo11.setId("11");
    projectInfo11.setName("Migrate CI to docker");

    ProjectInfo projectInfo2 = new ProjectInfo();
    projectInfo2.setId("2");
    projectInfo2.setName("Spica");

    ProjectInfo projectInfo21 = new ProjectInfo();
    projectInfo21.setId("21");
    projectInfo21.setName("Create javafx client");

    projectInfos = Arrays.asList(projectInfo11, projectInfo21);

  }
}
