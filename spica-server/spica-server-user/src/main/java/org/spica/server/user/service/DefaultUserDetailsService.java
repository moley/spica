package org.spica.server.user.service;

import org.spica.server.user.domain.User;
import org.spica.server.user.domain.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DefaultUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  public DefaultUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null)
      throw new UsernameNotFoundException("User " + username + " not found");

    return org.springframework.security.core.userdetails.User.withUsername(username)
      .password(user.getPassword()).authorities(Collections.emptyList())
      .accountExpired(false).accountLocked(false).credentialsExpired(false)
      .disabled(false).build();
  }
}
