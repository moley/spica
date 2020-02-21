package org.spica.fx;

import java.time.LocalDateTime;
import java.util.Collection;
import javafx.application.Platform;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.fx.controllers.AbstractFxController;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.utils.DateUtil;

public class ResetTimeThread extends Thread {

  private final static Logger LOGGER = LoggerFactory.getLogger(ResetTimeThread.class);


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
    LOGGER.info("ResetTimeThread running");

    while (! stopped) {

      if (! currentTime.equals(dateUtil.getTimeAsString(LocalDateTime.now()))){
        LOGGER.info("ResetTimeThread recieved time changed event");
        currentTime = dateUtil.getTimeAsString(LocalDateTime.now());
        Platform.runLater(new Runnable() {
          @Override public void run() {
            for (AbstractFxController nextController: controllers) {
              Platform.runLater(new Runnable() {
                @Override public void run() {
                  nextController.setTiming();
                }
              });

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
