package org.spica.javaclient.mail;

import java.io.IOException;
import java.util.ArrayList;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.spica.commons.mail.MailReciever;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;

public class MailImporter {



  private MailReciever mailReciever = new MailReciever();

  public boolean importMails(Model model) throws MessagingException, IOException {

    boolean modelChanged = false;

    for (Message nextMessage : mailReciever.recieveMails()) {
      MessagecontainerInfo messagecontainerInfo = getMessageContainer(model, nextMessage);
      if (messagecontainerInfo == null) {
        messagecontainerInfo = new MessagecontainerInfo().message(new ArrayList<MessageInfo>());
        model.getMessagecontainerInfos().add(messagecontainerInfo);
        modelChanged = true;
      }
      MessageInfo messageInfo = getMessage(messagecontainerInfo, nextMessage);
      if (messageInfo == null) {
        messageInfo = new MessageInfo();
        messageInfo.setId(getId(nextMessage));
        messageInfo.setCreator(nextMessage.getFrom()[0].toString());
        messageInfo.setMessage(nextMessage.getContent().toString());
        messageInfo.setType(MessageType.MAIL);
        messagecontainerInfo.getMessage().add(messageInfo);
        modelChanged = true;
      }
    }
    return modelChanged;
  }

  private MessageInfo getMessage(final MessagecontainerInfo messagecontainerInfo, final Message message)
      throws MessagingException {
    for (MessageInfo nextMessageInfo : messagecontainerInfo.getMessage()) {
      if (nextMessageInfo.getId().equals(getId(message))) { //Update
        return nextMessageInfo;
      }
    }

    return null;

  }

  String getId(final Message message) throws MessagingException {
    return message.getFrom()[0].toString() + "-" + message.getSentDate();
  }

  private MessagecontainerInfo getMessageContainer(final Model model, Message message) throws MessagingException {
    for (MessagecontainerInfo nextInfo : model.getMessagecontainerInfos()) {
      if (nextInfo.getTopic().equals(message.getSubject()))
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
