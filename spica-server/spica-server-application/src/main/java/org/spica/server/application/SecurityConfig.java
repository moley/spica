package org.spica.server.application;

import org.spica.server.user.service.JwtConfigurer;
import org.spica.server.user.service.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final TokenProvider tokenProvider;

  public SecurityConfig(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.csrf().disable();
    http.headers().frameOptions().disable();
    http.cors()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      //.httpBasic() // optional, if you want to access
      //  .and()     // the services from a browser
      .authorizeRequests()
      .antMatchers("/h2/**").permitAll() //TODO only for development
      .antMatchers("/signup").permitAll()
      .antMatchers("/login").permitAll()
      .antMatchers("/public").permitAll()
      .anyRequest().authenticated()
      .and()
      .apply(new JwtConfigurer(this.tokenProvider));
    // @formatter:on
  }

}