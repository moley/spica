package org.spica.javaclient.mail;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.mail.MessagingException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spica.commons.DashboardItemType;
import org.spica.commons.SpicaProperties;
import org.spica.commons.mail.Attachment;
import org.spica.commons.mail.Mail;
import org.spica.commons.mail.MailAdapter;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.utils.TestUtils;

public class MailImporterTest {

  private TestUtils testUtils = new TestUtils();


  @BeforeEach
  public void before () throws IOException {
    testUtils.setupJunitTestWorkspace(getClass().getSimpleName());
    SpicaProperties.setSpicaHome(Files.createTempDir());
  }

  @AfterEach
  public void after () {
    SpicaProperties.close();

  }

  @Test
  public void reimportExistingAttachment () throws IOException, MessagingException {
    String topic = "topic";
    String mail = "hans@spica.org";
    Model model = new Model();
    LocalDateTime localDateTime = LocalDateTime.now();
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MailImporter mailImporter = new MailImporter();
    MailAdapter mailAdapter = Mockito.mock(MailAdapter.class);

    Attachment attachment = Mockito.mock(Attachment.class);
    Mockito.when(attachment.getFilename()).thenReturn("hello world");

    Mail message1 = Mockito.mock(Mail.class);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getId()).thenReturn("007");
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFromAsStringList()).thenReturn(Arrays.asList(mail));
    Mockito.when(message1.getText()).thenReturn("Hello world");
    Mockito.when(message1.getAttachmentList()).thenReturn(Arrays.asList(attachment));

    //File exists
    File existingAttachment = new File (SpicaProperties.getSpicaHome(), "filestore/mailattachment_007_hello_world");
    existingAttachment.getParentFile().mkdirs();
    Assert.assertTrue (existingAttachment.createNewFile());

    Mockito.when(mailAdapter.recieveMails()).thenReturn(Arrays.asList(message1));
    mailImporter.setMailAdapter(mailAdapter);
    mailImporter.importMails(model);

    MessagecontainerInfo messagecontainerInfo = model.getMessagecontainerInfos().get(0);
    MessageInfo messageInfo = messagecontainerInfo.getMessage().get(0);
    Assert.assertEquals (1, messageInfo.getDocuments().size());
    Assert.assertEquals ("mailattachment_007_hello_world", messageInfo.getDocuments().get(0));
  }

  @Test
  public void closeOldMails () throws MessagingException, IOException {
    String id = "id";
    String topic = "topic";
    String mail = "hans@spica.org";
    Model model = new Model();

    LocalDateTime localDateTime = LocalDateTime.now();
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MailImporter mailImporter = new MailImporter();
    MailAdapter mailAdapter = Mockito.mock(MailAdapter.class);

    Mail message1 = Mockito.mock(Mail.class);
    Mockito.when(message1.getId()).thenReturn(id);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFromAsStringList()).thenReturn(Arrays.asList(mail));
    Mockito.when(message1.getText()).thenReturn("Hello world");

    Mockito.when(mailAdapter.recieveMails()).thenReturn(Arrays.asList(message1));
    mailImporter.setMailAdapter(mailAdapter);
    mailImporter.importMails(model);
    Assert.assertEquals ("Number of message containers invalid:" + model.getMessagecontainerInfos(), 1, model.getMessagecontainerInfos().size());
    Assert.assertEquals ("Number of messages invalid:" + model.getMessagecontainerInfos(), 1, model.getMessagecontainerInfos().get(0).getMessage().size());
    String messageID = model.getMessagecontainerInfos().get(0).getMessage().get(0).getId();
    DashboardItemInfo dashboardItemInfo = model.findDashboardItemInfo(DashboardItemType.MAIL, messageID);
    Assert.assertTrue("DashboardItem must be open at first", dashboardItemInfo.getOpen());

    MailAdapter mailAdapter2 = Mockito.mock(MailAdapter.class);
    Mockito.when(mailAdapter.recieveMails()).thenReturn(new ArrayList<>());
    mailImporter.setMailAdapter(mailAdapter2);
    mailImporter.importMails(model);
    Assert.assertFalse("DashboardItem was not closed when mail is no longer available", dashboardItemInfo.getOpen());



  }

  @Test
  public void reimportMessageFromDifferentUserToExistingMessageContainer () throws IOException, MessagingException {

    String id = "id";
    String id2 = "id2";
    String topic = "topic";
    String mail = "hans@spica.org";
    LocalDateTime localDateTime = LocalDateTime.now();
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().topic(topic);
    MessageInfo messageInfo = new MessageInfo().message("Hello world").creator("1").type(MessageType.MAIL).creationtime(localDateTime);
    messagecontainerInfo.addMessageItem(messageInfo);
    Model model = new Model();
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    MailImporter mailImporter = new MailImporter();
    MailAdapter mailAdapter = Mockito.mock(MailAdapter.class);

    Mail message1 = Mockito.mock(Mail.class);
    Mockito.when(message1.getId()).thenReturn(id);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFromAsStringList()).thenReturn(Arrays.asList(mail));
    Mockito.when(message1.getText()).thenReturn("Hello world");


    Mail message2 = Mockito.mock(Mail.class);
    Mockito.when(message2.getId()).thenReturn(id2);
    Mockito.when(message2.getSentDate()).thenReturn(localDate);
    Mockito.when(message2.getSubject()).thenReturn(topic);
    Mockito.when(message2.getFromAsStringList()).thenReturn(Arrays.asList(mail));
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

    String id = "id";
    String id2 = "id2";
    String topic = "topic";
    String mail = "hans@spica.org";
    LocalDateTime localDateTime = LocalDateTime.now();
    LocalDateTime laterLocalDateTime = LocalDateTime.now().plus(10, ChronoUnit.MINUTES);
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());
    Date laterDate = Date.from( laterLocalDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().topic(topic);
    MessageInfo messageInfo = new MessageInfo().message("Hello world").creator("1").type(MessageType.MAIL).creationtime(localDateTime);
    messagecontainerInfo.addMessageItem(messageInfo);
    Model model = new Model();
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    MailImporter mailImporter = new MailImporter();
    MailAdapter mailAdapter = Mockito.mock(MailAdapter.class);

    Mail message1 = Mockito.mock(Mail.class);
    Mockito.when(message1.getId()).thenReturn(id);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFromAsStringList()).thenReturn(Arrays.asList(mail));
    Mockito.when(message1.getText()).thenReturn("Hello world");


    Mail message2 = Mockito.mock(Mail.class);
    Mockito.when(message2.getId()).thenReturn(id2);
    Mockito.when(message2.getSentDate()).thenReturn(laterDate);
    Mockito.when(message2.getSubject()).thenReturn(topic);
    Mockito.when(message2.getFromAsStringList()).thenReturn(Arrays.asList(mail));
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
    String id = "id";
    String id2 = "id2";
    String topic = "topic";
    String topic2 = "topic2";
    String mail = "hans@spica.org";
    LocalDateTime localDateTime = LocalDateTime.now();
    Date localDate = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());

    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().topic(topic);
    MessageInfo messageInfo = new MessageInfo().message("Hello world").creator("1").type(MessageType.MAIL).creationtime(localDateTime);
    messagecontainerInfo.addMessageItem(messageInfo);
    Model model = new Model();
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    MailImporter mailImporter = new MailImporter();
    MailAdapter mailAdapter = Mockito.mock(MailAdapter.class);

    Mail message1 = Mockito.mock(Mail.class);
    Mockito.when(message1.getId()).thenReturn(id);
    Mockito.when(message1.getSentDate()).thenReturn(localDate);
    Mockito.when(message1.getSubject()).thenReturn(topic);
    Mockito.when(message1.getFromAsStringList()).thenReturn(Arrays.asList(mail));
    Mockito.when(message1.getText()).thenReturn("Hello world");


    Mail message2 = Mockito.mock(Mail.class);
    Mockito.when(message2.getSentDate()).thenReturn(localDate);
    Mockito.when(message2.getSubject()).thenReturn(topic2);
    Mockito.when(message1.getId()).thenReturn(id2);
    Mockito.when(message2.getFromAsStringList()).thenReturn(Arrays.asList(mail));
    Mockito.when(message2.getText()).thenReturn("Hello world");


    Mockito.when(mailAdapter.recieveMails()).thenReturn(Arrays.asList(message1, message2));
    mailImporter.setMailAdapter(mailAdapter);
    String message1Id = message1.getId();
    messageInfo.setId(message1Id);
    mailImporter.importMails(model);
    Assert.assertEquals ("Number of message containers invalid:" + model.getMessagecontainerInfos(), 2, model.getMessagecontainerInfos().size());
  }
}
