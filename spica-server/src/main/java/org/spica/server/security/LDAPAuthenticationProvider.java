package org.spica.server.security;

import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.credentials.PasswordMask;
import org.spica.server.user.domain.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LDAPAuthenticationProvider implements AuthenticationProvider {

  private PasswordMask passwordMask = new PasswordMask();

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {

    String name = authentication.getName();
    String password = authentication.getCredentials().toString();
    String maskedPassword = passwordMask.getMaskedPassword(password);

    LDAPUserProvider ldapUserProvider = new LDAPUserProvider();
    User userInfo = ldapUserProvider.getUserInfo(name, password);
    if (userInfo != null) {
      // use the credentials
      // and authenticate against the third-party system
      log.info("Check authentication with username " + name + " and password " + maskedPassword + ": OK");

      return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
    } else {
      log.info("Check authentication with username " + name + " and password " + maskedPassword + ": FAILED");
      return null;
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}