package org.spica.server.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.server.config.ServerConfiguration;
import org.spica.server.user.service.UserProvider;
import org.spica.server.user.service.UserProviderFactory;
import org.spica.server.user.domain.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SpicaAuthenticationProvider implements AuthenticationProvider {


  private static final Logger LOGGER = LoggerFactory.getLogger(SpicaAuthenticationProvider.class);


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    LOGGER.info("authenticate " + authentication.getName());

    SpicaProperties spic = new SpicaProperties();
    String userProviderImplementation = spic.getValueNotNull(ServerConfiguration.PROPERTY_USERPROVIDER);

    try {
      UserProviderFactory userProviderFactory = new UserProviderFactory();
      UserProvider userProvider = userProviderFactory.create(userProviderImplementation);

      LOGGER.info("Using user provider " + userProvider.getClass().getName());

      String name = authentication.getName();
      String password = authentication.getCredentials().toString();

      User user = userProvider.getUserInfo(name, password);
      if (user != null)
        return new UsernamePasswordAuthenticationToken(user, password, new ArrayList<>());
      else
        throw new BadCredentialsException("Bad credentials for user " + name);

    } catch (Exception e ) {
      LOGGER.error("Error while login", e);
      throw new BadCredentialsException("Login error " + e.getLocalizedMessage());
    }

  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
