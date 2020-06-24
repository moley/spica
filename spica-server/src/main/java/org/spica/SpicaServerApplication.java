package org.spica;

import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.demodata.DemoDataCreatorServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@Configuration
@ComponentScan
public class SpicaServerApplication {

  private final static Logger LOGGER = LoggerFactory.getLogger(SpicaServerApplication.class);


  public SpicaServerApplication(@Autowired DemoDataCreatorServer developmentDemoData) throws IOException {

    LOGGER.info("Starting spica server in path " + new File ("").getAbsolutePath());


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
