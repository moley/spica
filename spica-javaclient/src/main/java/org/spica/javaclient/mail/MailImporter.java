package org.spica.javaclient.mail;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.mail.MessagingException;
import org.spica.commons.DashboardItemType;
import org.spica.commons.mail.Mail;
import org.spica.commons.mail.MailReciever;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;

public class MailImporter {



  private MailReciever mailReciever = new MailReciever();

  public boolean importMails(Model model) throws MessagingException, IOException {

    boolean modelChanged = false;

    for (Mail nextMail : mailReciever.recieveMails()) {
      MessagecontainerInfo messagecontainerInfo = getMessageContainer(model, nextMail);
      if (messagecontainerInfo == null) {
        messagecontainerInfo = new MessagecontainerInfo().message(new ArrayList<MessageInfo>());
        messagecontainerInfo.setTopic(nextMail.getSubject());
        model.getMessagecontainerInfos().add(messagecontainerInfo);
        modelChanged = true;
      }
      MessageInfo messageInfo = getMessage(messagecontainerInfo, nextMail);
      if (messageInfo == null) {
        messageInfo = new MessageInfo();
        messageInfo.setId(getId(nextMail));
        messageInfo.setCreator(nextMail.getFrom());
        messageInfo.setMessage(nextMail.getText());
        messageInfo.setType(MessageType.MAIL);
        messageInfo.setCreationtime(nextMail.getCreationDateAsLocalDateTime());
        messagecontainerInfo.getMessage().add(messageInfo);
        modelChanged = true;

        DashboardItemInfo dashboardItemInfo = model.findDashboardItemInfo(DashboardItemType.MAIL, messageInfo.getId());
        if (dashboardItemInfo == null) {
          dashboardItemInfo = new DashboardItemInfo();
          dashboardItemInfo.setCreated(messageInfo.getCreationtime());
          dashboardItemInfo.setItemType(DashboardItemType.MAIL.toString());
          dashboardItemInfo.setItemReference(messageInfo.getId());
          dashboardItemInfo.setDescription(messagecontainerInfo.getTopic());
          model.getDashboardItemInfos().add(dashboardItemInfo);
        }

      }
    }
    return modelChanged;
  }

  private MessageInfo getMessage(final MessagecontainerInfo messagecontainerInfo, final Mail mail)
      throws MessagingException {
    for (MessageInfo nextMessageInfo : messagecontainerInfo.getMessage()) {
      if (nextMessageInfo.getId().equals(getId(mail))) { //Update
        return nextMessageInfo;
      }
    }

    return null;

  }

  String getId(final Mail message) throws MessagingException {
    return message.getFrom() + "-" + message.getSentDate();
  }

  private MessagecontainerInfo getMessageContainer(final Model model, Mail mail) throws MessagingException {
    for (MessagecontainerInfo nextInfo : model.getMessagecontainerInfos()) {
      if (nextInfo.getTopic() == null)
        continue;

      if (nextInfo.getTopic().equals(mail.getSubject()))
        return nextInfo;
    }

    return null;
  }

  public MailReciever getMailReciever() {
    return mailReciever;
  }

  public void setMailReciever(MailReciever mailReciever) {
    this.mailReciever = mailReciever;
  }
}
