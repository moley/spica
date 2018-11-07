package org.spica.server.demodata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerDemoData extends DemoDataCreator{

  private final static Logger LOGGER = LoggerFactory.getLogger(CustomerDemoData.class);

  public void create () {
    LOGGER.info("Start creating customer demo data");

  }
}
