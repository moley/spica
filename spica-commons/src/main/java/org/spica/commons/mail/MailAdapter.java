package org.spica.commons.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;

public class MailAdapter {

  private final static Logger LOGGER = LoggerFactory.getLogger(MailAdapter.class);


  public final static String PROPERTY_MAIL_POP_HOST = "spica.mail.pop.host";
  public final static String PROPERTY_MAIL_POP_PORT = "spica.mail.pop.port";
  public final static String PROPERTY_MAIL_POP_USERNAME = "spica.mail.pop.user";
  public final static String PROPERTY_MAIL_POP_PASSWORD = "spica.mail.pop.password";

  private Session createSession (SpicaProperties spicaProperties) {
    Properties mailProperties = new Properties();
    mailProperties.setProperty("mail.pop3.host", spicaProperties.getValue(PROPERTY_MAIL_POP_HOST));
    mailProperties.setProperty("mail.pop3.port", spicaProperties.getValue(PROPERTY_MAIL_POP_PORT));
    mailProperties.setProperty("mail.pop3.ssl.enable",  Boolean.toString(true));
    //mailProperties.setProperty("mail.debug", Boolean.toString(true));

    String username = spicaProperties.getValue(PROPERTY_MAIL_POP_USERNAME);
    String password = spicaProperties.getValue(PROPERTY_MAIL_POP_PASSWORD);

    //Session: steht f�r die Verbindung mit dem Mail-Server
    Session session = Session.getDefaultInstance(mailProperties, new Authenticator() {
      @Override protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
    return session;
  }

  public void deleteMail (final String id) {
    try{


      SpicaProperties spicaProperties = new SpicaProperties();
      Session session = createSession(spicaProperties);
      //Gibt in der Console Debug-Meldungen zum Verlauf aus
      //session.setDebug(true);

      //Store: dient dem zum Ablegen der Nachrichten
      Store store = session.getStore("pop3");
      store.connect();
      //Folder: ist ein Ordner-Object f�r Mails
      Folder folder = store.getFolder("INBOX");
      folder.open(Folder.READ_WRITE);

      if (!folder.isOpen())
        throw new IllegalStateException("Folder could not be opened");

      Message message[] = folder.getMessages();
      for(int i=0;i<message.length;i++){
        Message m = message[i];
        Mail mail = new Mail(m);
        if (mail.getId().equals(id)) {
          LOGGER.info("Set deleted flag on id " + mail.getId());
          m.setFlag(Flags.Flag.DELETED, true);
        }
      }

      folder.close( true );
      store.close();

    }catch(Exception err){
      throw new IllegalStateException("Error fetching mails: " + err.toString(), err);
    }

  }

  public List<Mail> recieveMails () {


    try{
      //Get system properties
      SpicaProperties spicaProperties = new SpicaProperties();
      Session session = createSession(spicaProperties);
      //Gibt in der Console Debug-Meldungen zum Verlauf aus
      //session.setDebug(true);

      //Store: dient dem zum Ablegen der Nachrichten
      Store store = session.getStore("pop3");
      store.connect();
      //Folder: ist ein Ordner-Object f�r Mails
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
      throw new IllegalStateException("Error fetching mails: " + err.toString(), err);
    }

  }
}
