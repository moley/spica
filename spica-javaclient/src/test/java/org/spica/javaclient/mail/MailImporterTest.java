package org.spica.javaclient.mail;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.spica.commons.mail.MailReciever;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;

public class MailImporterTest {

  @Test
  public void reimportMessageFromDifferentUserToExistingMessageContainer () throws IOException, MessagingException {

    String topic = "topic";
    String mail = "hans@spica.org";
    String mail2 = "admin@spica.org";
    LocalDateTime localDateTime = LocalDateTime.now();
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().topic(topic);
    MessageInfo messageInfo = new MessageInfo().message("Hello world").creator(mail).type(MessageType.MAIL).creationtime(localDateTime);
    messagecontainerInfo.addMessageItem(messageInfo);
    Model model = new Model();
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    MailImporter mailImporter = new MailImporter();
    MailReciever mailReciever = Mockito.mock(MailReciever.class);

    Message message1 = Mockito.mock(Message.class);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFrom()).thenReturn(new Address[]{new InternetAddress(mail) });
    Mockito.when(message1.getContent()).thenReturn("Hello world");


    Message message2 = Mockito.mock(Message.class);
    Mockito.when(message2.getSentDate()).thenReturn(localDate);
    Mockito.when(message2.getSubject()).thenReturn(topic);
    Mockito.when(message2.getFrom()).thenReturn(new Address[]{new InternetAddress(mail2) });
    Mockito.when(message2.getContent()).thenReturn("Hello world");


    Mockito.when(mailReciever.recieveMails()).thenReturn(Arrays.asList(message1, message2));
    mailImporter.setMailReciever(mailReciever);
    String message1Id = mailImporter.getId(message1);
    messageInfo.setId(message1Id);
    mailImporter.importMails(model);
    Assert.assertEquals ("Number of message containers invalid:" + model.getMessagecontainerInfos(), 1, model.getMessagecontainerInfos().size());
    Assert.assertEquals ("Number of messages invalid:" + model.getMessagecontainerInfos(), 2, model.getMessagecontainerInfos().get(0).getMessage().size());

  }

  @Test
  public void reimportLaterMessageToExistingMessageContainer () throws IOException, MessagingException {

    String topic = "topic";
    String mail = "hans@spica.org";
    LocalDateTime localDateTime = LocalDateTime.now();
    LocalDateTime laterLocalDateTime = LocalDateTime.now().plus(10, ChronoUnit.MINUTES);
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());
    Date laterDate = Date.from( laterLocalDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().topic(topic);
    MessageInfo messageInfo = new MessageInfo().message("Hello world").creator(mail).type(MessageType.MAIL).creationtime(localDateTime);
    messagecontainerInfo.addMessageItem(messageInfo);
    Model model = new Model();
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    MailImporter mailImporter = new MailImporter();
    MailReciever mailReciever = Mockito.mock(MailReciever.class);

    Message message1 = Mockito.mock(Message.class);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFrom()).thenReturn(new Address[]{new InternetAddress(mail) });
    Mockito.when(message1.getContent()).thenReturn("Hello world");


    Message message2 = Mockito.mock(Message.class);
    Mockito.when(message2.getSentDate()).thenReturn(laterDate);
    Mockito.when(message2.getSubject()).thenReturn(topic);
    Mockito.when(message2.getFrom()).thenReturn(new Address[]{new InternetAddress(mail) });
    Mockito.when(message2.getContent()).thenReturn("Hello world");


    Mockito.when(mailReciever.recieveMails()).thenReturn(Arrays.asList(message1, message2));
    mailImporter.setMailReciever(mailReciever);
    String message1Id = mailImporter.getId(message1);
    messageInfo.setId(message1Id);
    mailImporter.importMails(model);
    Assert.assertEquals ("Number of message containers invalid:" + model.getMessagecontainerInfos(), 1, model.getMessagecontainerInfos().size());
    Assert.assertEquals ("Number of messages invalid:" + model.getMessagecontainerInfos(), 2, model.getMessagecontainerInfos().get(0).getMessage().size());

  }

  @Test
  public void importDifferentTopic () throws MessagingException, IOException {
    String topic = "topic";
    String topic2 = "topic2";
    String mail = "hans@spica.org";
    LocalDateTime localDateTime = LocalDateTime.now();
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().topic(topic);
    MessageInfo messageInfo = new MessageInfo().message("Hello world").creator(mail).type(MessageType.MAIL).creationtime(localDateTime);
    messagecontainerInfo.addMessageItem(messageInfo);
    Model model = new Model();
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    MailImporter mailImporter = new MailImporter();
    MailReciever mailReciever = Mockito.mock(MailReciever.class);

    Message message1 = Mockito.mock(Message.class);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFrom()).thenReturn(new Address[]{new InternetAddress(mail) });
    Mockito.when(message1.getContent()).thenReturn("Hello world");


    Message message2 = Mockito.mock(Message.class);
    Mockito.when(message2.getSentDate()).thenReturn(localDate);
    Mockito.when(message2.getSubject()).thenReturn(topic2);
    Mockito.when(message2.getFrom()).thenReturn(new Address[]{new InternetAddress(mail) });
    Mockito.when(message2.getContent()).thenReturn("Hello world");


    Mockito.when(mailReciever.recieveMails()).thenReturn(Arrays.asList(message1, message2));
    mailImporter.setMailReciever(mailReciever);
    String message1Id = mailImporter.getId(message1);
    messageInfo.setId(message1Id);
    mailImporter.importMails(model);
    Assert.assertEquals ("Number of message containers invalid:" + model.getMessagecontainerInfos(), 2, model.getMessagecontainerInfos().size());
  }
}
