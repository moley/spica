package org.spica.server.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*", maxAge = 3600)
@Slf4j
public class BasicAuthController {

  public BasicAuthController () {
    log.info("Create " + getClass().getName());

  }

  @GetMapping(path = "/basicauth")
  public AuthenticationBean helloWorldBean() {
    log.info("get authentication bean");

    return new AuthenticationBean("You are authenticated");
  }
}
