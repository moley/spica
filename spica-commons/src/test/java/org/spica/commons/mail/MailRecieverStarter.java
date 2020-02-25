package org.spica.commons.mail;

import javax.mail.MessagingException;

public class MailRecieverStarter {

  public static void main(String[] args) throws MessagingException {

    MailAdapter mailAdapter = new MailAdapter();
    for (Mail next: mailAdapter.recieveMails()) {
      System.out.println ("Number:        " + next.getMessageNumber());
      //System.out.println ("Sent data:     " + next.getSentDate());
      System.out.println ("Topic:         " + next.getSubject());
    }

  }
}
