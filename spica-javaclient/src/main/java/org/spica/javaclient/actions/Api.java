package org.spica.javaclient.actions;

import org.spica.javaclient.api.UserApi;

public class Api {

  private UserApi userApi;

  public UserApi getUserApi () {
    if (userApi == null)
      userApi = new UserApi();
    return userApi;
  }
  public String getCurrentServer () {
    return getUserApi().getApiClient().getBasePath();
  }
}
