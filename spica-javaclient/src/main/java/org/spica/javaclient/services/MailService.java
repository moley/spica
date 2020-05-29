package org.spica.javaclient.services;

import java.util.List;
import javax.mail.MessagingException;
import org.spica.commons.mail.MailAdapter;

public class MailService {

  private MailAdapter mailAdapter = new MailAdapter();

  public void sendMail (String subject, final String content, final List<String> to) throws MessagingException {
      mailAdapter.sendMail(subject, content, to);
  }
}
