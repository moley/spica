package org.spica.fx;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimerTask;
import javafx.application.Platform;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.mail.MailImporter;
import org.spica.javaclient.model.MessagecontainerInfo;

@Slf4j
public class AutoImportMailsTask extends TimerTask {

  private ActionContext actionContext;

  private Reload reload;


  public AutoImportMailsTask(final ActionContext actionContext, Reload reload) {
    this.actionContext = actionContext;
    this.reload = reload;

  }
  @Override public void run() {
    log.info("Import mails started");

    MailImporter mailImporter = new MailImporter();
    try {
      if (mailImporter.importMails(actionContext.getModel())) {
        Collections.sort(actionContext.getModel().getMessagecontainerInfos(), new Comparator<MessagecontainerInfo>() {
          @Override public int compare(MessagecontainerInfo o1, MessagecontainerInfo o2) {
            return o2.getMessage().get(0).getCreationtime().compareTo(o1.getMessage().get(0).getCreationtime());
          }
        });
        if (reload != null)
          reload.reload();
        actionContext.saveModel("Mails imported");
      }
    } catch (MessagingException e) {
      log.error("Error importing mails: " + e.getLocalizedMessage(), e);
    } catch (IOException e) {
      log.error("Error importing mails: " + e.getLocalizedMessage(), e);
    } finally {
      log.info("Import mails finished");
    }

  }
}
