package org.spica.commons.mail;

import com.sun.mail.util.BASE64DecoderStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.minidns.record.A;

@Slf4j
@Data
public class Mail {

  private final String subject;

  private final String id;
  private final Date sentDate;
  private final Date creationDate;
  private final int messageNumber;

  private final String text;

  private final List<Attachment> attachmentList = new ArrayList<>();
  private final List<Address> from = new ArrayList<>();
  private final List<Address> to = new ArrayList<>();
  private final List<Address> cc = new ArrayList<>();
  private final List<Address> bcc = new ArrayList<>();

  public Mail (Message message, final String id) throws MessagingException, IOException {
    this.id = id;
    this.subject = message.getSubject();
    this.sentDate = message.getSentDate();
    this.creationDate = message.getSentDate();
    this.to.addAll(message.getRecipients(Message.RecipientType.TO) != null ? Arrays.asList(message.getRecipients(Message.RecipientType.TO)): Arrays.asList());
    this.cc.addAll(message.getRecipients(Message.RecipientType.CC) != null ? Arrays.asList(message.getRecipients(Message.RecipientType.CC)): Arrays.asList());
    this.bcc.addAll(message.getRecipients(Message.RecipientType.BCC) != null ? Arrays.asList(message.getRecipients(Message.RecipientType.BCC)): Arrays.asList());

    this.messageNumber = message.getMessageNumber();
    this.from.addAll(Arrays.asList(message.getFrom()));
    StringBuilder builderHtml = new StringBuilder();
    StringBuilder builderPlainText = new StringBuilder();
    readContent(builderPlainText, builderHtml, message.getContent());
    log.info("Recieved message " + subject + " from " + sentDate + " from " + from + " with html " + ! builderHtml.toString().isEmpty());
    this.text = ! builderHtml.toString().isEmpty() ? builderHtml.toString() : builderPlainText.toString();
  }

  public List<String> toStringList (final List<Address> adresses) {
    List<String> asString = new ArrayList<>(adresses.size());
    for (Address next: adresses) {
      if (next instanceof InternetAddress) {
        String addressString = ((InternetAddress) next).getAddress();
        if (! asString.contains(addressString))
        asString.add(addressString);
      }
      else
        throw new IllegalStateException("Only InternetAdress is supported as address of mail recipients");
    }

    return asString;
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
          Attachment attachment = new Attachment();
          attachment.setFilename(bodyPart.getFileName());
          attachment.setInputStream(bodyPart.getInputStream());
          attachmentList.add(attachment);
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

  public LocalDateTime getCreationDateAsLocalDateTime() {
    return Instant.ofEpochMilli( getCreationDate().getTime()).atZone( ZoneId.systemDefault()).toLocalDateTime();
  }

  public List<String> getFromAsStringList () {
    return toStringList(from);
  }

  public List<String> getToAsStringList () {
    return toStringList(to);
  }

  public List<String> getCCAsStringList () {
    return toStringList(cc);
  }

  public List<String> getBCCAsStringList () {
    return toStringList(bcc);
  }

}
