package org.spica.server.application;

import org.spica.server.demodata.CustomerDemoData;
import org.spica.server.demodata.DemoDataType;
import org.spica.server.demodata.DevelopmentDemoData;
import org.spica.server.demodata.SchoolDemoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages={"org.spica.server"})
@EntityScan(basePackages = {"org.spica.server"})
@EnableJpaRepositories(basePackages = {"org.spica.server"})
public class SpicaApplication {


  public SpicaApplication (SchoolDemoData schoolDemoData, CustomerDemoData customerDemoData, DevelopmentDemoData developmentDemoData) {

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

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  public static void main (final String [] args) {
    SpringApplication.run(SpicaApplication.class, args);
  }


}
