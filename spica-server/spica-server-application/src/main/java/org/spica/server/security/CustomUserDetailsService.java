package org.spica.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.user.UserRoleName;
import org.spica.server.user.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("loadUserByUsername " + username);


        //TODO fetch from database or provider
        User user = new User();
        user.setUsername(username);
        user.setDisplayname(username);
        user.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(UserRoleName.ROLE_USER.name())));
        return user;
    }



}
