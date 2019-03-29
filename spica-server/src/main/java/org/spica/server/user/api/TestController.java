package org.spica.server.user.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


  public TestController () {
    System.out.println ("");
  }
  @GetMapping("/secret")
  @CrossOrigin
  public String secretService() {
    return "A secret message";
  }
}