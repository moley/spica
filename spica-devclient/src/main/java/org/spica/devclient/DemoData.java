package org.spica.devclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.devclient.model.DashboardInfo;
import org.spica.javaclient.model.ProjectInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class DemoData {

  private final static Logger LOGGER = LoggerFactory.getLogger(DemoData.class);


  public static Collection<ProjectInfo> projectInfos = new ArrayList<>();
  public static Collection<DashboardInfo> dashboardInfos = new ArrayList<>();

  public static void create () {
    createProjects();
    createDashboardInfos();

  }

  public static void createDashboardInfos () {
    LOGGER.info("Create dashboard infos");
    DashboardInfo dashboardInfo1 = new DashboardInfo();
    dashboardInfo1.setName("CHAT mit Hans Maier");
    DashboardInfo dashboardInfo2 = new DashboardInfo();
    dashboardInfo2.setName("TERMIN Besprechung Releaseplanung");
    DashboardInfo dashboardInfo3 = new DashboardInfo();
    dashboardInfo3.setName("TERMIN Codereview");
    DashboardInfo dashboardInfo4 = new DashboardInfo();
    dashboardInfo4.setName("JIRA Refactoring ChatClient");
    DashboardInfo dashboardInfo5 = new DashboardInfo();
    dashboardInfo5.setName("TODO Zeiten buchen");
    dashboardInfos.addAll(Arrays.asList(dashboardInfo1, dashboardInfo2, dashboardInfo3, dashboardInfo4, dashboardInfo5));

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
