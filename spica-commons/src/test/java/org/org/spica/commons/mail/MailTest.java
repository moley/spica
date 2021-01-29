package org.org.spica.commons.mail;

import java.io.IOException;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spica.commons.mail.Mail;

public class MailTest {

  @Test
  public void asString () throws IOException, MessagingException {
    Message mockedMessage = Mockito.mock(Message.class);
    Mockito.when(mockedMessage.getFrom()).thenReturn(new Address[] {new InternetAddress("Markus.Oley@noventi.de", "Oley, Markus") });
    Mockito.when(mockedMessage.getContent()).thenReturn("Content");
    Mockito.when(mockedMessage.getRecipients(Message.RecipientType.TO)).thenReturn(new Address[] {new InternetAddress("Markus.Oley@noventi.de", "Oley, Markus") });
    Mail mail = new Mail(mockedMessage, "id");
    Assert.assertEquals ("Markus.Oley@noventi.de", mail.getToAsStringList().get(0));
  }
}
