package org.spica.javaclient.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.DashboardItemType;
import org.spica.commons.mail.Mail;
import org.spica.commons.mail.MailAdapter;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;

@Slf4j
public class MailImporter {


  private MailAdapter mailAdapter = new MailAdapter();

  private String extractMailAdresse (final String from) {
    try {
      return from.substring(from.indexOf("<") + 1, from.indexOf(">"));
    } catch (Exception e) {
      return from;
    }
  }

  public boolean importMails(Model model) throws MessagingException, IOException {

    boolean modelChanged = false;
    Collection<String> ids = new ArrayList<String>();

    for (Mail nextMail : mailAdapter.recieveMails()) {
      log.info("Checking mail " + nextMail.getSubject() + " - " + nextMail.getFrom() + "-" + nextMail.getId());

      ids.add(nextMail.getId());

      MessagecontainerInfo messagecontainerInfo = getMessageContainer(model, nextMail);
      if (messagecontainerInfo == null) {
        messagecontainerInfo = new MessagecontainerInfo().message(new ArrayList<MessageInfo>());
        messagecontainerInfo.setTopic(normalizeMailSubject(nextMail.getSubject()));
        model.getMessagecontainerInfos().add(messagecontainerInfo);
        modelChanged = true;
      }
      MessageInfo messageInfo = getMessage(messagecontainerInfo, nextMail);
      if (messageInfo == null) {
        messageInfo = new MessageInfo();
        messageInfo.setId(nextMail.getId());
        messageInfo.setType(MessageType.MAIL);
        messageInfo.setCreationtime(nextMail.getCreationDateAsLocalDateTime());
        messagecontainerInfo.getMessage().add(messageInfo);
        modelChanged = true;

        DashboardItemInfo dashboardItemInfo = model.findDashboardItemInfo(DashboardItemType.MAIL, messageInfo.getId());
        if (dashboardItemInfo == null) {
          dashboardItemInfo = new DashboardItemInfo();
          dashboardItemInfo.setId(UUID.randomUUID().toString());
          dashboardItemInfo.setCreated(messageInfo.getCreationtime());
          dashboardItemInfo.setOpen(Boolean.TRUE);
          dashboardItemInfo.setItemType(DashboardItemType.MAIL.toString());
          dashboardItemInfo.setItemReference(messageInfo.getId());
          dashboardItemInfo.setDescription(messagecontainerInfo.getTopic());
          model.getDashboardItemInfos().add(dashboardItemInfo);
        }

      }

      String mailFrom = extractMailAdresse(nextMail.getFrom());
      UserInfo userInfo = model.findUserByMail(mailFrom);
      messageInfo.setCreatorId(userInfo != null ? userInfo.getId(): null);
      messageInfo.setCreatorMailadresse(mailFrom);
      messageInfo.setMessage(nextMail.getText());

    }

    //close open mails if not imported anymore
    for (DashboardItemInfo nextDashboardItem: model.getDashboardItemInfos() ) {
      if (nextDashboardItem.getItemType().equals(DashboardItemType.MAIL.name())) {
        Boolean open = ids.contains(nextDashboardItem.getItemReference());
        if (! open.equals(nextDashboardItem.getOpen())) {
          nextDashboardItem.setOpen(open);
          modelChanged = true;
        }
      }
    }
    return modelChanged;
  }

  private MessageInfo getMessage(final MessagecontainerInfo messagecontainerInfo, final Mail mail)
      throws MessagingException {
    for (MessageInfo nextMessageInfo : messagecontainerInfo.getMessage()) {
      if (nextMessageInfo.getId().equals(mail.getId())) { //Update
        return nextMessageInfo;
      }
    }

    return null;

  }


  public String normalizeMailSubject (final String subject) {
    return subject.replaceAll("Re:", "").replaceAll("AW:", "").trim();
  }


  private MessagecontainerInfo getMessageContainer(final Model model, Mail mail) throws MessagingException {
    for (MessagecontainerInfo nextInfo : model.getMessagecontainerInfos()) {
      if (nextInfo.getTopic() == null)
        continue;

      String normalizedTopic = normalizeMailSubject(nextInfo.getTopic());
      String normalizedSubject = normalizeMailSubject(mail.getSubject());
      if (normalizedTopic.equals(normalizedSubject))
        return nextInfo;
    }

    return null;
  }

  public MailAdapter getMailAdapter() {
    return mailAdapter;
  }

  public void setMailAdapter(MailAdapter mailAdapter) {
    this.mailAdapter = mailAdapter;
  }
}
