package org.spica.server.demodata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.commons.Role;
import org.spica.server.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class CustomerDemoData extends DemoDataCreator{

  private final static Logger LOGGER = LoggerFactory.getLogger(CustomerDemoData.class);

  private final static String CUSTOMER_DEPARTMENT_1 = "Konzeption";
  private final static String CUSTOMER_DEPARTMENT_2 = "Hotline";

  public void create () {
    LOGGER.info("Start creating customer demo data");


    User dev1 = user("Müller", "Michael", Role.USER);
    User dev2 = user("Klein", "Karin", Role.USER);
    User guest = user("Gast", "Gregor", Role.GUEST);
    User admin = user ("Admin", "Achim", Role.ADMIN);
    User teamlead = user ("Lercher", "Lasse", Role.USER);

    userGroup(CUSTOMER_DEPARTMENT_1, dev1, false);
    userGroup(CUSTOMER_DEPARTMENT_2, dev2, false);

    userGroup(CUSTOMER_DEPARTMENT_1, teamlead, true);
    userGroup(CUSTOMER_DEPARTMENT_2, admin, true);
  }
}
