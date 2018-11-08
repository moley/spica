package org.spica.server.demodata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.commons.Role;
import org.spica.server.project.domain.Project;
import org.spica.server.project.domain.Topic;
import org.spica.server.user.domain.User;
import org.spica.server.user.domain.UserGroup;
import org.springframework.stereotype.Component;

@Component
public class SchoolDemoData extends DemoDataCreator {

  private final static Logger LOGGER = LoggerFactory.getLogger(SchoolDemoData.class);

  private final static String CLASS_5D = "Klasse 5d";
  private final static String CLASS_7A = "Klasse 7a";

  private final static String ROBOTIK_PROJECT = "Robotikproject";
  private final static String REFERAT_DEUTSCH = "Referat Max Frisch - Stiller";

  public void create () {
    LOGGER.info("Start creating school demo data");

    User pupil = user("Müller", "Michael", Role.USER);
    User pupil2 = user("Klein", "Karin", Role.USER);
    User pupil3 = user("Otto", "Oskar", Role.USER);
    User guest = user("Gast", "Gregor", Role.GUEST);
    User admin = user ("Admin", "Achim", Role.ADMIN);
    User teacher = user ("Lercher", "Lasse", Role.GUEST);

    UserGroup class5d = userGroup(CLASS_5D, pupil, false);
    UserGroup class7a = userGroup(CLASS_7A, pupil2, false);
    UserGroup class10a = userGroup(CLASS_7A, pupil3, false);

    userGroup(CLASS_5D, teacher, true);
    userGroup(CLASS_7A, admin, true);

    Project project5d = project(CLASS_5D, admin, null, class5d);
    Project project7a = project(CLASS_7A, admin, null, class7a);
    Project projectRobotik = project(ROBOTIK_PROJECT, admin, project5d, pupil3); //same members then class project plus tutor of another class

    Topic topicReferatDeutsch = topic(REFERAT_DEUTSCH, pupil2, project7a);

    meep(pupil, project5d, "Hallo! Willkommen beim Klassenchat");

    meep(pupil2, topicReferatDeutsch, "Ich habe mal alle Dokumente zu meinem Referat hochgeladen");

    meep(admin, projectRobotik, "Zur ersten Robotikstunde bitte jeder einen Roboter mitbringen");

  }
}
