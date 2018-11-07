package org.spica.server.demodata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.commons.Role;
import org.spica.server.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class SchoolDemoData extends DemoDataCreator {

  private final static Logger LOGGER = LoggerFactory.getLogger(SchoolDemoData.class);

  private final static String CLASS_5D = "Klasse 5d";
  private final static String CLASS_7A = "Klasse 7a";

  public void create () {
    LOGGER.info("Start creating school demo data");

    User pupil = user("Müller", "Michael", Role.USER);
    User pupil2 = user("Klein", "Karin", Role.USER);
    User guest = user("Gast", "Gregor", Role.GUEST);
    User admin = user ("Admin", "Achim", Role.ADMIN);
    User teacher = user ("Lämpel", "Lehrer", Role.GUEST);

    userGroup(CLASS_5D, pupil, false);
    userGroup(CLASS_7A, pupil2, false);

    userGroup(CLASS_5D, teacher, true);
    userGroup(CLASS_7A, admin, true);


  }
}
