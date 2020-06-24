package org.spica.commons.services.jenkins;

public class JenkinsException extends RuntimeException {

  public JenkinsException (final String message, Throwable cause) {
    super (message, cause);
  }

  public JenkinsException (final String message) {
    super (message);
  }
}
