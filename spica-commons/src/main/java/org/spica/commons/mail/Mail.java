package org.spica.commons.mail;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class Mail {

  private final String subject;

  private final Date sentDate;
  private final Date creationDate;
  private final int messageNumber;
  private final String from;
  private final String text;

  public Mail (Message message) throws MessagingException, IOException {
    subject = message.getSubject();
    sentDate = message.getSentDate();
    creationDate = message.getSentDate();
    messageNumber = message.getMessageNumber();
    from = message.getFrom()[0].toString();

    if (message.getContent() instanceof String) {
      text = message.getContent().toString();
    }
    else if (message.getContent() instanceof MimeMultipart) {
      String multiPartText = "";
      String multiPartHtml = "";
      MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
      for (int i = 0; i < mimeMultipart.getCount(); i++) {
        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
        String nextString;
        if (bodyPart instanceof MimeBodyPart) {
          MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyPart;
          nextString =  mimeBodyPart.getContent().toString();
        }
        else
          nextString = mimeMultipart.getBodyPart(i).toString();

        if (nextString.contains("<html"))
          multiPartHtml += nextString;
        else
          multiPartText += nextString;
      }
      text = ! multiPartHtml.isEmpty() ? multiPartHtml : multiPartText;
    }
    else
      throw new IllegalStateException("Message with content of type " + message.getContent().getClass() + " is not supported yet");
  }

  public String getSubject() {
    return subject;
  }

  public Date getSentDate() {
    return sentDate;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public LocalDateTime getCreationDateAsLocalDateTime() {
    return Instant.ofEpochMilli( getCreationDate().getTime()).atZone( ZoneId.systemDefault()).toLocalDateTime();
  }

  public int getMessageNumber() {
    return messageNumber;
  }

  public String getFrom() {
    return from;
  }

  public String getText() {
    return text;
  }

  public String getId() throws MessagingException {
    return getFrom() + "-" + getSentDate();
  }
}
