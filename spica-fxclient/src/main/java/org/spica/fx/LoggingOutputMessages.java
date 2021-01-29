package org.spica.fx;

import java.util.ArrayList;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingOutputMessages implements Messages {

  private Collection<String> logged = new ArrayList<>();

  private String text;
  public LoggingOutputMessages() {

  }

  @Override public Messages text(String s) {
    this.text = s;
    return this;
  }

  @Override public void showInformation() {
    logged.add("INFO: " + text);
    log.info(text);


  }

  @Override public void showError() {
    logged.add("ERROR: " + text);
    log.error(text);
  }

  public Collection<String> getLogged() {
    return logged;
  }
}
