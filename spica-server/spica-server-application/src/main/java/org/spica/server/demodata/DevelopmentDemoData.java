package org.spica.server.demodata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentDemoData extends DemoDataCreator{

  private final static Logger LOGGER = LoggerFactory.getLogger(DevelopmentDemoData.class);

  public void create () {
    LOGGER.info("Start creating development demo data");



  }
}
