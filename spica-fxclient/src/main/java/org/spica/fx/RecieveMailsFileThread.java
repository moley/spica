package org.spica.fx;

import java.util.Date;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.mail.MailImporter;
import org.spica.javaclient.model.Model;

public class RecieveMailsFileThread extends Thread {

  private final static Logger LOGGER = LoggerFactory.getLogger(RecieveMailsFileThread.class);


  private boolean stopped = false;

  private final ActionContext actionContext;

  public RecieveMailsFileThread(final ActionContext actionContext) {
    this.actionContext = actionContext;
  }

  @SneakyThrows public void run(){
    LOGGER.info("ResetModelFileThread running");

    while (! stopped) {
      LOGGER.info("Recieve mails");

      Thread.sleep(10000); //wait 1 minute

      Model model = actionContext.getModel();
      MailImporter mailImporter = new MailImporter();
      boolean modelChanged = mailImporter.importMails(model);
      if (modelChanged) {
        LOGGER.info("Import mail changed model: " + modelChanged);
        actionContext.saveModel("Recieved mails at " + new Date());
      }

    }
  }

  public void dispose () {
    stopped = true;
  }
}
