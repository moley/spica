package org.spica.javaclient.mail;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.mail.MessagingException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.spica.commons.DashboardItemType;
import org.spica.commons.mail.Mail;
import org.spica.commons.mail.MailAdapter;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;

public class MailImporterTest {

  @Test
  public void closeOldMails () throws MessagingException, IOException {
    String topic = "topic";
    String mail = "hans@spica.org";
    Model model = new Model();

    LocalDateTime localDateTime = LocalDateTime.now();
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MailImporter mailImporter = new MailImporter();
    MailAdapter mailAdapter = Mockito.mock(MailAdapter.class);

    Mail message1 = Mockito.mock(Mail.class);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFrom()).thenReturn(mail);
    Mockito.when(message1.getText()).thenReturn("Hello world");

    Mockito.when(mailAdapter.recieveMails()).thenReturn(Arrays.asList(message1));
    mailImporter.setMailAdapter(mailAdapter);
    mailImporter.importMails(model);
    Assert.assertEquals ("Number of message containers invalid:" + model.getMessagecontainerInfos(), 1, model.getMessagecontainerInfos().size());
    Assert.assertEquals ("Number of messages invalid:" + model.getMessagecontainerInfos(), 1, model.getMessagecontainerInfos().get(0).getMessage().size());
    String messageID = model.getMessagecontainerInfos().get(0).getMessage().get(0).getId();
    DashboardItemInfo dashboardItemInfo = model.findDashboardItemInfo(DashboardItemType.MAIL, messageID);
    Assert.assertTrue("DashboardItem must be open at first", dashboardItemInfo.isOpen());

    MailAdapter mailAdapter2 = Mockito.mock(MailAdapter.class);
    Mockito.when(mailAdapter.recieveMails()).thenReturn(new ArrayList<>());
    mailImporter.setMailAdapter(mailAdapter2);
    mailImporter.importMails(model);
    Assert.assertFalse("DashboardItem was not closed when mail is no longer available", dashboardItemInfo.isOpen());



  }

  @Test
  public void reimportMessageFromDifferentUserToExistingMessageContainer () throws IOException, MessagingException {

    String topic = "topic";
    String mail = "hans@spica.org";
    String mail2 = "admin@spica.org";
    LocalDateTime localDateTime = LocalDateTime.now();
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().topic(topic).type(MessageType.MAIL);
    MessageInfo messageInfo = new MessageInfo().message("Hello world").creatorId(mail).type(MessageType.MAIL).creationtime(localDateTime);
    messagecontainerInfo.addMessageItem(messageInfo);
    Model model = new Model();
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    MailImporter mailImporter = new MailImporter();
    MailAdapter mailAdapter = Mockito.mock(MailAdapter.class);

    Mail message1 = Mockito.mock(Mail.class);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFrom()).thenReturn(mail);
    Mockito.when(message1.getText()).thenReturn("Hello world");


    Mail message2 = Mockito.mock(Mail.class);
    Mockito.when(message2.getSentDate()).thenReturn(localDate);
    Mockito.when(message2.getSubject()).thenReturn(topic);
    Mockito.when(message2.getFrom()).thenReturn(mail2);
    Mockito.when(message2.getText()).thenReturn("Hello world");


    Mockito.when(mailAdapter.recieveMails()).thenReturn(Arrays.asList(message1, message2));
    mailImporter.setMailAdapter(mailAdapter);
    String message1Id = message1.getId();
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

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().topic(topic).type(MessageType.MAIL);
    MessageInfo messageInfo = new MessageInfo().message("Hello world").creatorId(mail).type(MessageType.MAIL).creationtime(localDateTime);
    messagecontainerInfo.addMessageItem(messageInfo);
    Model model = new Model();
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    MailImporter mailImporter = new MailImporter();
    MailAdapter mailAdapter = Mockito.mock(MailAdapter.class);

    Mail message1 = Mockito.mock(Mail.class);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFrom()).thenReturn(mail);
    Mockito.when(message1.getText()).thenReturn("Hello world");


    Mail message2 = Mockito.mock(Mail.class);
    Mockito.when(message2.getSentDate()).thenReturn(laterDate);
    Mockito.when(message2.getSubject()).thenReturn(topic);
    Mockito.when(message2.getFrom()).thenReturn(mail);
    Mockito.when(message2.getText()).thenReturn("Hello world");


    Mockito.when(mailAdapter.recieveMails()).thenReturn(Arrays.asList(message1, message2));
    mailImporter.setMailAdapter(mailAdapter);
    String message1Id = message1.getId();
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

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().topic(topic).type(MessageType.MAIL);
    MessageInfo messageInfo = new MessageInfo().message("Hello world").creatorId(mail).type(MessageType.MAIL).creationtime(localDateTime);
    messagecontainerInfo.addMessageItem(messageInfo);
    Model model = new Model();
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    MailImporter mailImporter = new MailImporter();
    MailAdapter mailAdapter = Mockito.mock(MailAdapter.class);

    Mail message1 = Mockito.mock(Mail.class);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFrom()).thenReturn(mail);
    Mockito.when(message1.getText()).thenReturn("Hello world");


    Mail message2 = Mockito.mock(Mail.class);
    Mockito.when(message2.getSentDate()).thenReturn(localDate);
    Mockito.when(message2.getSubject()).thenReturn(topic2);
    Mockito.when(message2.getFrom()).thenReturn(mail);
    Mockito.when(message2.getText()).thenReturn("Hello world");


    Mockito.when(mailAdapter.recieveMails()).thenReturn(Arrays.asList(message1, message2));
    mailImporter.setMailAdapter(mailAdapter);
    String message1Id = message1.getId();
    messageInfo.setId(message1Id);
    mailImporter.importMails(model);
    Assert.assertEquals ("Number of message containers invalid:" + model.getMessagecontainerInfos(), 2, model.getMessagecontainerInfos().size());
  }
}
