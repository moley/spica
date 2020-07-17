package org.spica.commons.mail;

import javax.mail.MessagingException;

public class MailRecieverStarter {

  public static void main(String[] args) throws MessagingException {


    MailAdapter mailAdapter = new MailAdapter();
    try {
      for (Mail next : mailAdapter.recieveMails()) {
        System.out.println("Number:        " + next.getMessageNumber());
        //System.out.println ("Sent data:     " + next.getSentDate());
        System.out.println("Topic:         " + next.getSubject());
        System.out.println ("From " + next.getFrom());
        System.out.println ("Text " + next.getText());
        System.out.println ("SentDate " + next.getSentDate());
        System.out.println ("CreationDate " + next.getSentDate());
      }
    } catch (Exception e) {

    }

  }
}
