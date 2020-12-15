package org.spica.server.user.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins="*")
public class BasicAuthController {

  @GetMapping(path = "/basicauth")
  public AuthenticationBean helloWorldBean() {
    return new AuthenticationBean("You are authenticated");
  }
}
