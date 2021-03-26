package org.spica.javaclient.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.spica.commons.DashboardItemType;
import org.spica.commons.filestore.FilestoreItem;
import org.spica.commons.filestore.FilestoreService;
import org.spica.commons.mail.Attachment;
import org.spica.commons.mail.Mail;
import org.spica.commons.mail.MailAdapter;
import org.spica.javaclient.exceptions.NotFoundException;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;

@Slf4j public class MailImporter {

  private FilestoreService filestoreService = new FilestoreService();

  private MailAdapter mailAdapter = new MailAdapter();

  public boolean importMails(Model model) throws MessagingException, IOException {

    boolean modelChanged = false;
    Collection<String> ids = new ArrayList<String>();

    for (Mail nextMail : mailAdapter.recieveMails()) {
      log.info("Checking mail " + nextMail.getSubject() + " - " + nextMail.getFrom() + "-" + nextMail.getId());

      ids.add(nextMail.getId());

      MessagecontainerInfo messagecontainerInfo = getMessageContainer(model, nextMail);
      if (messagecontainerInfo == null) {
        messagecontainerInfo = model.createNewMessageContainer();
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

      for (Attachment attachment : nextMail.getAttachmentList()) {
        String completeFilename = "mailattachment_" + nextMail.getId() + "_" + attachment.getFilename().replaceAll(" ", "_");

        FilestoreItem file = filestoreService.file(completeFilename);
        log.info("Add attachment to " + file.getAbsolutePath());
        if (!file.exists()) {
          FileOutputStream fos = new FileOutputStream(file.getFile());
          IOUtils.copy(attachment.getInputStream(), fos);
        }

        if (messageInfo.getDocuments() == null)
          messageInfo.setDocuments(new ArrayList<>());

        if (!messageInfo.getDocuments().contains(completeFilename))
          messageInfo.getDocuments().add(completeFilename);

      }

      log.info("Set attachments of message " + messageInfo.getId() + " to " + messageInfo.getDocuments());

      String mailFrom = nextMail.getFromAsStringList().get(0);
      UserInfo userInfo = null;
      try {
        userInfo = model.getUserNotNull(mailFrom);
        messageInfo.setCreator(userInfo.getId());


      } catch (NotFoundException e) {
        log.error(e.getLocalizedMessage(), e);
      }

      messageInfo.setRecieversTo(getIdsForUsers(model, nextMail.getToAsStringList()));
      messageInfo.setRecieversCC(getIdsForUsers(model, nextMail.getCCAsStringList()));
      messageInfo.setRecieversBCC(getIdsForUsers(model, nextMail.getBCCAsStringList()));
      messageInfo.setMessage(nextMail.getText());

    }

    //close open mails if not imported anymore
    for (DashboardItemInfo nextDashboardItem : model.getDashboardItemInfos()) {
      if (nextDashboardItem.getItemType().equals(DashboardItemType.MAIL.name())) {
        Boolean open = ids.contains(nextDashboardItem.getItemReference());
        if (!open.equals(nextDashboardItem.getOpen())) {
          nextDashboardItem.setOpen(open);
          modelChanged = true;
        }
      }
    }
    return modelChanged;
  }

  public List<String> getIdsForUsers (final Model model, final List<String> mails) {
    Collection<String> ids = new HashSet<>();
    for (String next: mails) {
      try {
        UserInfo userInfo = model.getUserNotNull(next);
        ids.add(userInfo.getId());
      } catch (NotFoundException e) {
        throw new IllegalStateException("Cannot add on demand user with searchstring " + next);
      }
    }

    return new ArrayList<>(ids);

  }

  private MessageInfo getMessage(final MessagecontainerInfo messagecontainerInfo, final Mail mail)
      throws MessagingException {
    for (MessageInfo nextMessageInfo : messagecontainerInfo.getMessage()) {
      if (nextMessageInfo.getId() == null)
        continue;

      if (nextMessageInfo.getId().equals(mail.getId())) { //Update
        return nextMessageInfo;
      }
    }

    return null;

  }

  public String normalizeMailSubject(final String subject) {
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
