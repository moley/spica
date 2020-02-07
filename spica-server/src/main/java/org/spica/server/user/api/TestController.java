package org.spica.server.user.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/test")
  @CrossOrigin
  public String secretService() {
    return "The spica server is running";
  }
}