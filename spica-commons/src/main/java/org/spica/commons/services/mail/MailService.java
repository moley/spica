package org.spica.commons.services.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import org.spica.commons.mail.MailAdapter;

public class MailService {

  private MailAdapter mailAdapter = new MailAdapter();

  public void sendMail(String subject, final String content, final List<String> to) throws MessagingException {
    sendMail(subject, content, to, new ArrayList<>());
  }

  public void sendMail(String subject, final String content, final List<String> to, final List<File> files)
      throws MessagingException {
    mailAdapter.sendMail(subject, content, to, files);
  }
}
