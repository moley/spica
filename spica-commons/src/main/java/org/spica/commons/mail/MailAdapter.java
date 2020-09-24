package org.spica.commons.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;

@Slf4j
public class MailAdapter {


  public final static String PROPERTY_MAIL_POP_HOST = "spica.mail.pop.host";
  public final static String PROPERTY_MAIL_POP_PORT = "spica.mail.pop.port";
  public final static String PROPERTY_MAIL_POP_USERNAME = "spica.mail.pop.user";
  public final static String PROPERTY_MAIL_POP_PASSWORD = "spica.mail.pop.password";
  public final static String PROPERTY_MAIL_SMTP_HOST = "spica.mail.smtp.host";
  public final static String PROPERTY_MAIL_SMTP_PORT = "spica.mail.smtp.port";
  public final static String PROPERTY_MAIL_SMTP_SENDER = "spica.mail.smtp.sender";

  private Session createSession (SpicaProperties spicaProperties)  {
    Properties mailProperties = new Properties();
    mailProperties.setProperty("mail.pop3.host", spicaProperties.getValueNotNull(PROPERTY_MAIL_POP_HOST));
    mailProperties.setProperty("mail.pop3.port", spicaProperties.getValueNotNull(PROPERTY_MAIL_POP_PORT));
    mailProperties.setProperty("mail.pop3.ssl.enable",  Boolean.toString(true));

    mailProperties.setProperty("mail.smtp.host", spicaProperties.getValueNotNull(PROPERTY_MAIL_SMTP_HOST));
    mailProperties.setProperty("mail.smtp.port", spicaProperties.getValueNotNull(PROPERTY_MAIL_SMTP_PORT));
    mailProperties.setProperty("mail.smtp.auth",  Boolean.toString(true));
    mailProperties.setProperty("mail.smtp.starttls.enable",  Boolean.toString(true));

    MailSSLSocketFactory sf = null;
    try {
      sf = new MailSSLSocketFactory();
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    }
    sf.setTrustAllHosts(true);
    mailProperties.put("mail.pop3.ssl.trust", "*");
    mailProperties.put("mail.pop3.ssl.socketFactory", sf);
    mailProperties.put("mail.smtp.ssl.trust", "*");
    mailProperties.put("mail.smtp.ssl.socketFactory", sf);

    log.info("MailProperties: " + mailProperties.toString().replace(",", "\n"));

    String username = spicaProperties.getValue(PROPERTY_MAIL_POP_USERNAME);
    String password = spicaProperties.getValue(PROPERTY_MAIL_POP_PASSWORD);


    Session mailserverSession = Session.getDefaultInstance(mailProperties, new Authenticator() {
      @Override protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
    return mailserverSession;
  }

  public void deleteMail (final String id) {
    try{


      SpicaProperties spicaProperties = new SpicaProperties();
      Session session = createSession(spicaProperties);
      //Enable debug message for the communication with the mail server
      //session.setDebug(true);

      Store store = session.getStore("pop3");
      store.connect();
      Folder folder = store.getFolder("INBOX");
      folder.open(Folder.READ_WRITE);

      if (!folder.isOpen())
        throw new IllegalStateException("Folder could not be opened");

      Message message[] = folder.getMessages();
      for(int i=0;i<message.length;i++){
        Message m = message[i];
        Mail mail = new Mail(m);
        if (mail.getId().equals(id)) {
          log.info("Set deleted flag on id " + mail.getId());
          m.setFlag(Flags.Flag.DELETED, true);
        }
      }

      folder.close( true );
      store.close();

    }catch(Exception err){
      throw new IllegalStateException("Error fetching mails: " + err.toString(), err);
    }

  }

  public void sendMail (final String subject, String content, final List<String> to, final List<File> files) throws MessagingException {
    SpicaProperties spicaProperties = new SpicaProperties();
    Session session = createSession(spicaProperties);

    MimeMessage message = new MimeMessage( session );
    message.setFrom( new InternetAddress( spicaProperties.getValue(PROPERTY_MAIL_SMTP_SENDER)));

    for (String nextTo: to) {
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(nextTo));
    }

    message.setSubject( subject, "ISO-8859-1" );

    MimeMultipart mimeMultipart = new MimeMultipart();
    BodyPart bodyPart = new MimeBodyPart();
    bodyPart.setContent(content, "text/html");
    mimeMultipart.addBodyPart(bodyPart);

    for (File nextFile: files) {
      MimeBodyPart messageBodyPart = new MimeBodyPart();
      String filename = nextFile.getAbsolutePath();
      DataSource source = new FileDataSource(filename);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(filename);
      mimeMultipart.addBodyPart(messageBodyPart);
    }

    message.setContent(mimeMultipart);

    Transport.send( message );

  }

  public List<Mail> recieveMails () {
    log.info("recieveMails started");


    try{
      //Get system properties
      SpicaProperties spicaProperties = new SpicaProperties();
      Session session = createSession(spicaProperties);
      //Enable debug message for the communication with the mail server
      //ession.setDebug(true);

      Store store = session.getStore("pop3");
      store.connect();
      Folder folder = store.getFolder("INBOX");
      folder.open(Folder.READ_ONLY);

      if (!folder.isOpen())
        throw new IllegalStateException("Folder could not be opened");

      Message message[] = folder.getMessages();
      List<Mail> mailCollection = new ArrayList<Mail>();

      for(int i=0;i<message.length;i++){
        Message m = message[i];
        mailCollection.add(new Mail(m));
      }

      folder.close( false );
      store.close();

      return mailCollection;


    }catch(Exception err){
      log.error(err.getLocalizedMessage(), err);
      throw new IllegalStateException("Error fetching mails: " + err.toString(), err);
    } finally  {
      log.info("recieveMails finished");
    }


  }
}
