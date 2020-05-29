package org.spica.server.user.api;

public class Greeting {

  private String greeting;

  public Greeting (final String text) {
    this.greeting = text;
  }

  public String getGreeting() {
    return greeting;
  }

  public void setGreeting(String greeting) {
    this.greeting = greeting;
  }
}
