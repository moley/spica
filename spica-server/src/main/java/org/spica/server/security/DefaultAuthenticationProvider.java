package org.spica.server.security;

import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.credentials.PasswordMask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultAuthenticationProvider implements AuthenticationProvider {

  @Value("${spring.security.user.name}")
  private String username;

  @Value("${spring.security.user.password}")
  private String password;

  private PasswordMask passwordMask = new PasswordMask();

  @Override public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    log.info("Create default authentication provider with username " + username + " and password " + passwordMask.getMaskedPassword(password));

    String maskedPassword = password != null ? passwordMask.getMaskedPassword(password): null;


    if (authentication.getName().equals(username) && authentication.getCredentials().toString().equals(password)) {
      log.info("Check authentication with username " + username + " and password " + maskedPassword + ": OK");
      return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
    }
    else {
      log.info("Check authentication with username " + username + " and password " + maskedPassword + ": FAILED");
      return null;
    }
  }

  @Override public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
