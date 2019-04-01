package org.spica;

import org.spica.server.demodata.CustomerDemoData;
import org.spica.server.demodata.DemoDataType;
import org.spica.server.demodata.DevelopmentDemoData;
import org.spica.server.demodata.SchoolDemoData;
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

  @Autowired
  SchoolDemoData schoolDemoData;

  @Autowired
  CustomerDemoData customerDemoData;

  @Autowired
  DevelopmentDemoData developmentDemoData;

  public SpicaServerApplication() {

    String demoDataType = System.getProperty("demodata");
    if (demoDataType != null) {
      DemoDataType demoDataTypeAsEnum = DemoDataType.valueOf(demoDataType.toUpperCase());
      switch (demoDataTypeAsEnum) {
        case SCHOOL:
          schoolDemoData.create();
          break;
        case CUSTOMER:
          customerDemoData.create();
          break;
        case DEVELOPMENT:
          developmentDemoData.create();
          break;
        default:
      }
    }

  }

  public static void main (final String [] args) {
    SpringApplication.run(SpicaServerApplication.class, args);
  }


}
