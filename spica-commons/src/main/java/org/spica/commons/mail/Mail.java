package org.spica.commons.mail;

import com.sun.mail.util.BASE64DecoderStream;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Mail {

  private final String subject;

  private final String id;
  private final Date sentDate;
  private final Date creationDate;
  private final int messageNumber;
  private final String from;
  private final String text;

  public Mail (Message message, final String id) throws MessagingException, IOException {
    this.id = id;
    this.subject = message.getSubject();
    this.sentDate = message.getSentDate();
    this.creationDate = message.getSentDate();
    this.messageNumber = message.getMessageNumber();
    this.from = message.getFrom()[0].toString();
    StringBuilder builderHtml = new StringBuilder();
    StringBuilder builderPlainText = new StringBuilder();
    readContent(builderPlainText, builderHtml, message.getContent());
    log.info("Recieved message " + subject + " from " + sentDate + " from " + from + " with html " + ! builderHtml.toString().isEmpty());
    this.text = ! builderHtml.toString().isEmpty() ? builderHtml.toString() : builderPlainText.toString();
  }

  public void readContent (final StringBuilder plainText, final StringBuilder htmlText, Object contentObject) throws MessagingException, IOException {

    if (contentObject instanceof String) {
      String contentAsString = contentObject.toString();
      if (contentAsString.contains("<html")) {
        log.info("Found html string");
        htmlText.append(contentAsString);
      }
      else {
        log.info("Found plain text string");
        plainText.append(contentAsString);
      }
    }
    else if (contentObject instanceof MimeMultipart) {
      log.info("Found multipart");
      MimeMultipart mimeMultipart = (MimeMultipart) contentObject;
      for (int i = 0; i < mimeMultipart.getCount(); i++) {
        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
        if (bodyPart.getFileName() != null) {
          log.info("Found attachment");
        }
        if (bodyPart instanceof MimeBodyPart) {
          MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyPart;
          readContent(plainText, htmlText, mimeBodyPart.getContent());
        }
        else
          readContent(plainText, htmlText, mimeMultipart.getBodyPart(i).toString());
      }
    }
    else if (contentObject instanceof BASE64DecoderStream) {
      //ignore
      System.out.println ("");

    } else
      throw new IllegalStateException("Message with content of type " + contentObject.getClass() + " is not supported yet");

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

  public String getId() {
    return id;
  }
}
