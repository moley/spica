package org.spica.server.user.service;

import org.spica.server.user.domain.User;

public interface UserProvider {

  boolean isLoginNeeded ();

  User getUserInfo(String username, String password);
}
