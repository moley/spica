package org.spica.javaclient.services;

import org.spica.javaclient.model.UserInfo;

public class UserDisplayName {

  public String getDisplayname (final UserInfo userInfo) {
    //TODO make configurable
    return userInfo.getName() + ", " + userInfo.getFirstname();
  }
}
