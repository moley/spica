package org.spica.commons.mail;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.mail.Message;
import javax.mail.MessagingException;

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
    text = message.getContent().toString();
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
}
