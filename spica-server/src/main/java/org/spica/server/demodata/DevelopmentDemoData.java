package org.spica.server.demodata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.commons.Role;
import org.spica.server.project.domain.Project;
import org.spica.server.project.domain.Topic;
import org.spica.server.user.domain.Skill;
import org.spica.server.user.domain.User;
import org.spica.server.user.domain.UserGroup;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentDemoData extends DemoDataCreator{

  private final static Logger LOGGER = LoggerFactory.getLogger(DevelopmentDemoData.class);

  private final static String CONCEPTION_DEPARTMENT = "Konzeption";
  private final static String HOTLINE_DEPARTMENT = "Hotline";
  private final static String DEV_DEPARTMENT = "Development";

  private final static String PRODUKT_1 = "Produkt 1";
  private final static String PRODUKT_2 = "Produkt 2";

  private final static String ISSUE_PERFORMANCE = "Performance is too bad";
  private final static String ISSUE_USABILITY = "Usability is like Windows 95";
  private final static String ISSUE_STABILITY = "Stability is even worse";

  public void create () {
    LOGGER.info("Start creating development demo data");

    List<Skill> skills = skills("Java", "Junit4", "C#", "Jira", "Python", "Angular", "Agility");


    User dev1 = user("Mayer", "Marc", Role.USER, Arrays.asList(skills.get(0), skills.get(1)));
    User dev2 = user("User", "Uriah", Role.USER, new ArrayList<>());
    User hotliner = user("HotlineMan", "Harry", Role.USER, new ArrayList<>());
    User conception = user("ConceptWriter", "Cade", Role.USER, new ArrayList<>());
    User guest = user("Guest", "George", Role.GUEST, new ArrayList<>());
    User admin = user ("Admin", "Andy", Role.ADMIN, new ArrayList<>());
    User teamlead = user ("Teamlead", "Tom", Role.USER, new ArrayList<>());

    UserGroup departmentConception = userGroup(CONCEPTION_DEPARTMENT, conception, false);
    UserGroup departmentHotline = userGroup(HOTLINE_DEPARTMENT, hotliner, false);
    UserGroup departmentDevelopment = userGroup(DEV_DEPARTMENT, dev1, false);
    userGroup(DEV_DEPARTMENT, dev2, false);

    userGroup(CONCEPTION_DEPARTMENT, teamlead, true);
    userGroup(HOTLINE_DEPARTMENT, admin, true);

    Project product1 = project(PRODUKT_1, admin, null, departmentConception, departmentDevelopment);
    Project product2 = project(PRODUKT_2, admin, null, departmentConception, departmentHotline, departmentDevelopment);

    Topic topicIssueStability = topic(ISSUE_STABILITY, guest, product1);
    Topic topicIssuePerformance = topic(ISSUE_PERFORMANCE, guest, product2);
    Topic topicIssueUsability = topic(ISSUE_USABILITY, teamlead, product2);

    meep(guest, product1, "Hi, I have seen your application on your homepage, can I buy it?");

    meep(guest, topicIssuePerformance, "Why is my application soooooo slow?");
    meep(hotliner, topicIssuePerformance, "Which version do you have?");
    meep(guest, topicIssuePerformance, "0.8.15");
    meep(hotliner, topicIssuePerformance, "@Mayer Can you please take a look?");
    meep(dev1, topicIssuePerformance, "On my machine it is fast, can you try it on new hardware");

    meep(guest, topicIssueStability, "My desk, on which the PC with the application runs, has broken down. And now?");

    meep(conception, topicIssueUsability, "I believe the new version of your application is unusable");



  }
}
