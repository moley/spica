package org.spica.server.user.service;

import java.util.Collection;
import org.spica.server.user.domain.User;

public class LdapUserImporterTester {

  public static void main(String[] args) {

    LdapUserImporter ldapUserImporter = new LdapUserImporter();
    Collection<User> users = ldapUserImporter.getUsers();

    for (User next: users ) {
      System.out.println ("-" + next.getDisplayname() + "-" + next.getEmail() + "-" + next.getUsername());

    }

    System.out.println (users.size() + " users found");
  }
}
