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
public class DevelopmentDemoData extends DemoDataCreator{

  private final static Logger LOGGER = LoggerFactory.getLogger(DevelopmentDemoData.class);

  private final static String CONCEPTION_DEPARTMENT = "Konzeption";
  private final static String HOTLINE_DEPARTMENT = "Hotline";
  private final static String DEV_DEPARTMENT = "Development";

  private final static String PRODUKT_1 = "Produkt 1";
  private final static String PRODUKT_2 = "Produkt 2";

  private final static String ISSUE_PERFORMANCE = "Performance ist zu schlecht";
  private final static String ISSUE_USABILITY = "Usability ist wie Windows 95";
  private final static String ISSUE_STABILITY = "Stabilitaet ist schlecht";

  public void create () {
    LOGGER.info("Start creating development demo data");

    User dev1 = user("Mayer", "Michael", Role.USER);
    User dev2 = user("Klein", "Karin", Role.USER);
    User hotliner = user("Hotliner", "Holger", Role.USER);
    User conception = user("Konzept", "Karin", Role.USER);
    User guest = user("Gast", "Gregor", Role.GUEST);
    User admin = user ("Admin", "Achim", Role.ADMIN);
    User teamlead = user ("Lercher", "Lasse", Role.USER);

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

    meep(guest, product1, "Hallo, ich habe Euer Produkt im Internet gesehen, kann ich das haben?");

    meep(guest, topicIssuePerformance, "Warum ist mein Programm so langsam");
    meep(hotliner, topicIssuePerformance, "Welche Version haben Sie?");
    meep(guest, topicIssuePerformance, "0.8.15");
    meep(hotliner, topicIssuePerformance, "@Mayer Kannst Du mal bitte schauen?");
    meep(dev1, topicIssuePerformance, "Also auf meinem Rechner ist es rasendschnell, stellt ihm mal neue Hardware hin");

    meep(guest, topicIssueStability, "Mein Tisch, auf dem mein Rechner mit dem Programm lief, ist zusammengebrochen. Was tun?");

    meep(conception, topicIssueUsability, "Ich glaube die neue Version von unserem Produkt ist schlecht zu bedienen");




  }
}
