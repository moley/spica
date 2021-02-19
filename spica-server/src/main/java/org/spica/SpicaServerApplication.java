package org.spica;

import java.io.File;
import java.io.IOException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;
import org.spica.server.demodata.DemoDataCreator;
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
@Slf4j
@Data
public class SpicaServerApplication {

  @Autowired
  public SpicaServerApplication(DemoDataCreator demoDataCreator) {
    log.info("Starting spica server in path " + new File ("").getAbsolutePath());

    SpicaProperties spicaProperties = new SpicaProperties();
    if (spicaProperties.getValueAsBoolean("demodata")) {
      demoDataCreator.create();
    }
  }

  public static void main (final String [] args) {


    SpringApplication.run(SpicaServerApplication.class, args);
  }


}
