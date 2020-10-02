package org.spica.commons.mail;

public class MailAdapterTester {

  public static void main(String[] args) {

    MailAdapter mailAdapter = new MailAdapter();
    for (Mail next: mailAdapter.recieveMails()) {
      System.out.println ("From      : " + next.getFrom());
      System.out.println ("Subject   : " + next.getSubject());
      System.out.println ("Sent on   : " + next.getSentDate());
      System.out.println ("Content   : " + next.getText());
      System.out.println ("");

    }

  }
}
