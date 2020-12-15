package org.spica.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConf extends WebSecurityConfigurerAdapter {

  @Autowired
  private LDAPAuthenticationProvider authProvider;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authProvider);
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests()
        .antMatchers( "/*").permitAll()
        .antMatchers( "/app/").authenticated()
        .antMatchers( "/app/admin").authenticated()
        .antMatchers("/api/**").authenticated()
        .and()
        .httpBasic();
  }


}
