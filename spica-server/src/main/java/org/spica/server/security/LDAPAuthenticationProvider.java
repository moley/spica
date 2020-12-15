package org.spica.server.security;

import java.util.ArrayList;
import org.spica.server.user.domain.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class LDAPAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {

    String name = authentication.getName();
    String password = authentication.getCredentials().toString();

    LDAPUserProvider ldapUserProvider = new LDAPUserProvider();
    User userInfo = ldapUserProvider.getUserInfo(name, password);
    if (userInfo != null) {
      // use the credentials
      // and authenticate against the third-party system
      return new UsernamePasswordAuthenticationToken(
          name, password, new ArrayList<>());
    } else {
      return null;
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}