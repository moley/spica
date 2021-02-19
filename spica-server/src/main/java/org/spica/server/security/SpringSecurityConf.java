package org.spica.server.security;

import java.lang.reflect.InvocationTargetException;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Slf4j
public class SpringSecurityConf extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    log.info("Configure authentication provider");
    auth.authenticationProvider(authenticationProvider());
  }

  @Bean
  public LogoutSuccessHandler logoutSuccessHandler() {
    log.info("Configure CustomLogoutSuccessHandler");
    return new CustomLogoutSuccessHandler();
  }

  @Bean
  public AuthenticationProvider authenticationProvider () {
    SpicaProperties spicaProperties = new SpicaProperties();
    String valueNotNull = spicaProperties.getValueNotNull(SecurityConfiguration.PROPERTY_AUTHENTICATION_PROVIDER);
    try {
      Class<? extends AuthenticationProvider> clazz = (Class<? extends AuthenticationProvider>) getClass().getClassLoader().loadClass(valueNotNull);
      AuthenticationProvider authenticationProvider = clazz.getConstructor().newInstance();
      log.info("Initializing authenticationprovider " + clazz.getName());
      return authenticationProvider;
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      log.error("Error when creating authentication provider " + valueNotNull + ": " + e.getLocalizedMessage(), e);
      return null;
    }
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    log.info("Configure cors");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable().authorizeRequests()
        .antMatchers( "/*").permitAll()
        .antMatchers( "/app/").authenticated()
        .antMatchers( "/app/admin").authenticated()
        .antMatchers("/api/**").authenticated()
        .antMatchers("/h2/**").permitAll()
        .and().csrf().ignoringAntMatchers("/h2/**")
        .and().headers().frameOptions().sameOrigin()
        .and()
        .httpBasic().and().
        logout().logoutSuccessHandler(logoutSuccessHandler()).logoutUrl("/api/logout").invalidateHttpSession(true)
        .deleteCookies("JSESSIONID");
  }



}
