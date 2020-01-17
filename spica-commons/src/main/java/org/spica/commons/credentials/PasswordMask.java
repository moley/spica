package org.spica.commons.credentials;

/**
 * Masks the password
 */
public class PasswordMask {

  /**
   * get the masked password for logging
   * @param unmasked  unmasked password
   * @return masked password
   */
  public String getMaskedPassword (final String unmasked) {
    if (unmasked == null)
      return "<null>";
    else
      return unmasked.replaceAll(".", "*");
  }
}
