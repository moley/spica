package org.spica.commons.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import org.spica.commons.SpicaProperties;

public class MailReciever {

  public final static String PROPERTY_MAIL_POP_HOST = "spica.mail.pop.host";
  public final static String PROPERTY_MAIL_POP_PORT = "spica.mail.pop.port";
  public final static String PROPERTY_MAIL_POP_USERNAME = "spica.mail.pop.user";
  public final static String PROPERTY_MAIL_POP_PASSWORD = "spica.mail.pop.password";

  public List<Message> recieveMails () {


    try{
      List<Message> mails = new ArrayList<Message>();
      //Get system properties
      SpicaProperties spicaProperties = new SpicaProperties();

      Properties mailProperties = new Properties();
      mailProperties.setProperty("mail.pop3.host", spicaProperties.getValue(PROPERTY_MAIL_POP_HOST));
      mailProperties.setProperty("mail.pop3.port", spicaProperties.getValue(PROPERTY_MAIL_POP_PORT));
      mailProperties.setProperty("mail.pop3.ssl.enable",  Boolean.toString(true));

      String username = spicaProperties.getValue(PROPERTY_MAIL_POP_USERNAME);
      String password = spicaProperties.getValue(PROPERTY_MAIL_POP_PASSWORD);

      System.out.println (mailProperties);
          System.out.println ("Username " + username);
      System.out.println ("Password " + password);

          //Session: steht für die Verbindung mit dem Mail-Server
      Session session = Session.getDefaultInstance(mailProperties, new Authenticator() {
        @Override protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });
      //Gibt in der Console Debug-Meldungen zum Verlauf aus
      session.setDebug(false);



      //Store: dient dem zum Ablegen der Nachrichten
      Store store = session.getStore("pop3");
      store.connect();
      //Folder: ist ein Ordner-Object für Mails
      Folder folder = store.getFolder("INBOX");
      folder.open(Folder.READ_ONLY);

      if (!folder.isOpen())
        throw new IllegalStateException("Folder could not be opened");

      Message message[] = folder.getMessages();

      for(int i=0;i<message.length;i++){
        Message m = message[i];
        /**m.getReceivedDate();

        System.out.println( "-----------------------\nNachricht: " + i );
        System.out.println ("Klasse:" + m.getClass().getName());
        System.out.println( "Von: " + Arrays.toString(m.getFrom()) );
        System.out.println( "Betreff: " + m.getSubject() );
        System.out.println ("Disposition: " + m.getDisposition());
        System.out.println( "Gesendet am: " + m.getSentDate() );
        System.out.println( "ContentType: " + new ContentType(m.getContentType()) );
        //System.out.println( "Content: " + m.getContent() );

        //Nachricht ist eine einfache Text- bzw. HTML-Nachricht
        /**if( m.isMimeType("text/plain") ){
          System.out.println( m.getContent() );
        }

        //Nachricht ist eine Multipart-Nachricht (besteht aus mehreren Teilen)
        if( m.isMimeType("multipart/*") ){
          Multipart mp = (Multipart) m.getContent();

          for( int j=0;j<mp.getCount();j++ ){
            Part part = mp.getBodyPart(j);
            String disposition = part.getDisposition();

            if( disposition == null ){
              MimeBodyPart mimePart = (MimeBodyPart) part;

              if( mimePart.isMimeType("text/plain") ){
                BufferedReader in = new BufferedReader( new InputStreamReader(mimePart.getInputStream()) );

                for( String line; (line=in.readLine()) != null; ){
                  System.out.println( line );
                }
              }
            }
          }
        }//if Multipart**/
        mails.add(m);

      }

      folder.close( false );
      store.close();

      return mails;


    }catch(Exception err){
      throw new IllegalStateException("Error fetching mails: " + err.toString(), err);
    }

  }
}
