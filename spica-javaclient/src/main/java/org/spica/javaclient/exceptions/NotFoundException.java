package org.spica.javaclient.exceptions;

public class NotFoundException extends Exception {

  public NotFoundException (String message) {
    super (message);
  }

  public NotFoundException (String message, Exception e) {
    super (message, e);
  }
}
