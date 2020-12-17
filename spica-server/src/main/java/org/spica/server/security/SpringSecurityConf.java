package org.spica.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConf extends WebSecurityConfigurerAdapter {

  @Autowired
  private LDAPAuthenticationProvider authProvider;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authProvider);
  }

  @Bean
  public LogoutSuccessHandler logoutSuccessHandler() {
    return new CustomLogoutSuccessHandler();
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
    http.csrf().disable().authorizeRequests()
        .antMatchers( "/*").permitAll()
        .antMatchers( "/app/").authenticated()
        .antMatchers( "/app/admin").authenticated()
        .antMatchers("/api/**").authenticated()
        .and()
        .httpBasic().and().
        logout().logoutSuccessHandler(logoutSuccessHandler()).logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID");
  }


}
