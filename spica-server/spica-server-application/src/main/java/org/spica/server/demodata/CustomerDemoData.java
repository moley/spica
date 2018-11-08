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
public class CustomerDemoData extends DemoDataCreator{

  private final static Logger LOGGER = LoggerFactory.getLogger(CustomerDemoData.class);

  private final static String ASSURANCE_DEPARTMENT = "Abteilung Versicherungen";
  private final static String CAPITAL_DEPARTMENT = "Abteilung Vermoegen";

  private final static String PROJECT_KUNDE1 = "Kunde Michael Mayer";
  private final static String PROJECT_KUNDE2 = "Kunde Karin Klein";
  private final static String PROJECT_KUNDE3 = "Kunde Caro Kloppenhardt";

  private final static String ISSUE_NEW_CAR = "Neues Auto";
  private final static String ISSUE_ANPASSUNG_VWL = "Anpassung VwL";
  private final static String ISSUE_DISMISSAL = "Ich kuendige";

  public void create () {

    User customer1 = user("Mayer", "Michael", Role.USER);
    User customer2 = user("Klein", "Karin", Role.USER);
    User customer3 = user("Cloppenhardt", "Caro", Role.USER);
    User guest = user("Gast", "Gregor", Role.GUEST);
    User admin = user ("Admin", "Achim", Role.ADMIN);
    User service1 = user ("Lercher", "Lasse", Role.USER);
    User service2 = user ("Service", "Simone", Role.USER);

    UserGroup departmentAssurance = userGroup(ASSURANCE_DEPARTMENT, service1, true);
    UserGroup departmentCapital = userGroup(CAPITAL_DEPARTMENT, service2, true);

    Project projectCustomer1 = project(PROJECT_KUNDE1, admin, null, departmentAssurance);
    Project projectCustomer2 = project(PROJECT_KUNDE2, admin, null, departmentAssurance, departmentCapital);
    Project projectCustomer3 = project(PROJECT_KUNDE3, admin, null, departmentAssurance, departmentCapital);

    Topic topicDismissal = topic(ISSUE_DISMISSAL, customer1, projectCustomer3);
    Topic topicNewCar = topic(ISSUE_NEW_CAR, customer1, projectCustomer1);
    Topic topicAnpassungVwl = topic(ISSUE_ANPASSUNG_VWL, service1, projectCustomer2);

    meep(customer1, projectCustomer1, "Hallo, ich bin der Michael, und ich moechte Geld");
    meep(customer1, topicNewCar, "Ich habe mir einen neuen Golf gekauft, koennt ihr mir bitte ein KFZ-Versicherungs Angebot machen?");

    meep(customer2, topicDismissal, "Ich hab was besseres gefunden, ich kuendige");

    meep(service1, topicAnpassungVwl, "Es gibt neue gesetzliche Bestimmungen zu Deinen Vwls, bitte mal bei mir vorbeischauen");
    meep(customer2, topicAnpassungVwl, "Geht klar, bin morgen sowieso in der Gegend");
  }
}
