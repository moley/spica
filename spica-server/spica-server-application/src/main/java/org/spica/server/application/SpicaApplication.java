package org.spica.server.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("org.spica.server")
public class SpicaApplication {

  public static void main (final String [] args) {
    SpringApplication.run(SpicaApplication.class, args);
  }
}
