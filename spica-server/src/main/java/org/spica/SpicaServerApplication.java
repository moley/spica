package org.spica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.demodata.DevelopmentDemoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@Configuration
@ComponentScan
public class SpicaServerApplication {

  private final static Logger LOGGER = LoggerFactory.getLogger(SpicaServerApplication.class);


  public SpicaServerApplication(@Autowired  DevelopmentDemoData developmentDemoData) {


    String demoDataType = System.getProperty("demodata");
    if (demoDataType != null) {
      LOGGER.info("Creating demodata because system property demodata is set to " + demoDataType + " on " + developmentDemoData);
      developmentDemoData.create();
    }

  }

  public static void main (final String [] args) {
    SpringApplication.run(SpicaServerApplication.class, args);
  }


}
