package org.spica.fx;

import java.util.Collection;
import java.util.Date;
import javafx.application.Platform;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.fx.controllers.AbstractFxController;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.mail.MailImporter;
import org.spica.javaclient.model.Model;

public class RecieveMailsFileThread extends Thread {

  private final static Logger LOGGER = LoggerFactory.getLogger(RecieveMailsFileThread.class);


  private boolean stopped = false;

  private final ActionContext actionContext;

  private final Collection<AbstractFxController> controllers;


  public RecieveMailsFileThread(final ActionContext actionContext, Collection<AbstractFxController> controllers) {
    this.actionContext = actionContext;
    this.controllers = controllers;
  }

  @SneakyThrows public void run(){
    LOGGER.info("ResetModelFileThread running");

    while (! stopped) {
      LOGGER.info("Recieve mails");


      Model model = actionContext.getModel();
      MailImporter mailImporter = new MailImporter();
      boolean modelChanged = mailImporter.importMails(model);
      if (modelChanged) {
        LOGGER.info("Import mail changed model: " + modelChanged);
        actionContext.saveModel("Recieved mails at " + new Date());

        actionContext.reloadModel();
        for (AbstractFxController nextController: controllers) {
          LOGGER.info("Reload actioncontext on " + nextController.getClass().getSimpleName());
          Platform.runLater(new Runnable() {
            @Override public void run() {
              nextController.setActionContext(actionContext);
            }
          });

        }
      }

      Thread.sleep(10000); //wait 1 minute


    }
  }

  public void dispose () {
    stopped = true;
  }
}
