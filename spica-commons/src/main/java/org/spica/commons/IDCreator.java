package org.spica.commons;

public class IDCreator {

  public String createID (final String name) {
    String lowerName = name.toLowerCase();
    return lowerName.replaceAll(" ", "");
  }
}
