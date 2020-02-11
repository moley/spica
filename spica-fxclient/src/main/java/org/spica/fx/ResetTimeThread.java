package org.spica.fx;

import java.time.LocalDateTime;
import java.util.Collection;
import javafx.application.Platform;
import lombok.SneakyThrows;
import org.spica.fx.controllers.AbstractFxController;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.utils.DateUtil;

public class ResetTimeThread extends Thread {

  private boolean stopped = false;

  private final ActionContext actionContext;
  private final Collection<AbstractFxController> controllers;

  private DateUtil dateUtil = new DateUtil();

  private String currentTime = "";

  public ResetTimeThread(final ActionContext actionContext, Collection<AbstractFxController> controllers) {
    this.actionContext = actionContext;
    this.controllers = controllers;
  }

  @SneakyThrows public void run(){
    System.out.println("ResetClockThread running");

    while (! stopped) {

      if (! currentTime.equals(dateUtil.getTimeAsString(LocalDateTime.now()))){
        System.out.println ("ResetClockThread recieved time changed event");
        currentTime = dateUtil.getTimeAsString(LocalDateTime.now());
        Platform.runLater(new Runnable() {
          @Override public void run() {
            for (AbstractFxController nextController: controllers) {
              nextController.setTiming();
            }
          }
        });
      }
      sleep(100);
    }
  }

  public void dispose () {
    stopped = true;
  }
}
