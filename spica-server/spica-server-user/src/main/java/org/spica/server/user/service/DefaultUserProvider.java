package org.spica.server.user.service;

import org.spica.server.user.domain.User;

public class DefaultUserProvider implements UserProvider {
  @Override
  public boolean isLoginNeeded() {
    return false;
  }

  @Override
  public User getUserInfo(String username, String password) {
    throw new IllegalStateException("DefaultUserProvider must not ask for user info");
  }
}
