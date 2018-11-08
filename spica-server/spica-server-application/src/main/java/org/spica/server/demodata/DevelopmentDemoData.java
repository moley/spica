package org.spica.server.demodata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.commons.Role;
import org.spica.server.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentDemoData extends DemoDataCreator{

  private final static Logger LOGGER = LoggerFactory.getLogger(DevelopmentDemoData.class);

  private final static String DEVELOPEMENT_DEPARTMENT_1 = "Entwickler Berlin";
  private final static String DEVELOPMEN_DEPARTMENT_2 = "Entwickler Frankfurt";

  public void create () {
    LOGGER.info("Start creating development demo data");

    User dev1 = user("Müller", "Michael", Role.USER);
    User dev2 = user("Klein", "Karin", Role.USER);
    User guest = user("Gast", "Gregor", Role.GUEST);
    User admin = user ("Admin", "Achim", Role.ADMIN);
    User teamlead = user ("Lercher", "Lasse", Role.USER);

    userGroup(DEVELOPEMENT_DEPARTMENT_1, dev1, false);
    userGroup(DEVELOPMEN_DEPARTMENT_2, dev2, false);

    userGroup(DEVELOPEMENT_DEPARTMENT_1, teamlead, true);
    userGroup(DEVELOPMEN_DEPARTMENT_2, admin, true);



  }
}
