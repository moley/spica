package org.spica.devclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.model.DevClientDemoDataCreator;

import java.io.File;

public class DevApplication  {

  private final static Logger LOGGER = LoggerFactory.getLogger(DevApplication.class);

  public static void main(String[] args) {

    LOGGER.info("Started in path " + new File ("").getAbsolutePath());
    File buildPath = new File("build");
    if (buildPath.exists()) {

      System.setProperty("spica.home", buildPath.getAbsolutePath());
      LOGGER.info("Buildpath " + buildPath.getAbsolutePath() + " exists, create demo data in " + System.getProperty("spica.home"));
      DevClientDemoDataCreator devClientDemoDataCreator = new DevClientDemoDataCreator();
      devClientDemoDataCreator.main(args);
    }


    DevApplicationInternal.main(args);
  }

}