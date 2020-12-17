package org.spica.server.security;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.message.BindRequest;
import org.apache.directory.api.ldap.model.message.BindRequestImpl;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.commons.credentials.PasswordMask;
import org.spica.server.user.config.LdapConfiguration;
import org.spica.server.user.domain.User;
import org.spica.server.user.service.UserProvider;

/**
 */
public class LDAPUserProvider implements UserProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(LDAPUserProvider.class);

  /*
   * creates bind request
   */
  BindRequest createBindRequest () {
    return new BindRequestImpl();
  }

  /**
   * creates network connection
   * @param hostname  hostname
   * @param port      port
   * @param useSsl    flag if using ssl
   * @return connection
   */
  LdapConnection createLdapConnection (final String hostname, int port, final boolean useSsl) {
    return new LdapNetworkConnection(hostname, port, useSsl);
  }

  @Override
  public boolean isLoginNeeded() {
    return true;
  }

  @Override
  public User getUserInfo(String username, final String password) {
    PasswordMask passwordMask = new PasswordMask();
    String maskedPassword = passwordMask.getMaskedPassword(password);

    LOGGER.info("Requesting user info for user <" + username + "> and password <" + maskedPassword + ">");

    SpicaProperties spicaProperties = new SpicaProperties();

    User user = new User();

    String ldapHostname = spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_HOST);
    Integer ldapPort = spicaProperties.getValueAsInt(spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_PORT));
    String ldapBaseDn = spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_BASE_DN);
    String ldapUsernameField = spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_FIELD_USERNAME);
    String ldapDisplaynameField = spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_FIELD_DISPLAYNAME);
    String ldapUserPrefix = spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_USERNAME_PREFIX);


    String completeUsername = ldapUserPrefix + username;
    LdapConnection connection = createLdapConnection(ldapHostname, ldapPort, true);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Created connection with");
      LOGGER.debug("Host              : <" + ldapHostname + ">");
      LOGGER.debug("BaseDn            : <" + ldapBaseDn + ">");
      LOGGER.debug("User prefix       : <" + ldapUserPrefix + ">");
      LOGGER.debug("Username Field    : <" + ldapUsernameField + ">");
      LOGGER.debug("Displayname Field : <" + ldapDisplaynameField + ">");
      LOGGER.debug("Complete username : <" + completeUsername + ">");
      LOGGER.debug("Password          : <" + maskedPassword + ">");
    }

    long time = 0;

    try {
      BindRequest bindRequest = new BindRequestImpl();
      bindRequest.setName(completeUsername); //"INTRA\\ServiceLinux"
      bindRequest.setCredentials(password);
      connection.bind(bindRequest);

      ResultCodeEnum resultCode = bindRequest.getResultResponse().getLdapResult().getResultCode();
      LOGGER.debug("Binding result: " + connection.isAuthenticated() + "(" + resultCode.getMessage() + ")");
      if (connection.isAuthenticated()) {

        long from = System.currentTimeMillis();

        String searchUsersFilter = "(" + ldapUsernameField + "=" + username + ")";

        LOGGER.debug("Bind to user completed");
        LOGGER.debug("Search with baseDn <" + ldapBaseDn + ">, user filter <" + searchUsersFilter + ">");

        EntryCursor cursor = connection.search(ldapBaseDn, searchUsersFilter, SearchScope.SUBTREE, ldapUsernameField, ldapDisplaynameField);

        if (! cursor.next()) {
          LOGGER.warn("No user found for username " + username);
          return null;
        }

        Entry entry = cursor.get();

        String usernameFound = getStringOrNull(entry.get(ldapUsernameField));
        String displaynameFound = getStringOrNull(entry.get(ldapDisplaynameField));
        String cn = entry.getDn().getName();

        if (usernameFound != null && usernameFound.equalsIgnoreCase(username)) {
          user.setUsername(username);
          user.setDisplayname(displaynameFound);
          user.setPassword(password);

          LOGGER.info("Found user " + usernameFound + " with password (" + maskedPassword+ ") and displayname " + displaynameFound);

        } else {
          LOGGER.error("Username invalid, should be " + username + " but was " + usernameFound + "(" + cn + "), check your search query");
          return null;
        }

        time = System.currentTimeMillis() - from;
      } else {
        LOGGER.error("Could not connect to user, authentication error");
        return null;
      }

    } catch (Exception e) {
      LOGGER.error(e.getLocalizedMessage(), e);
      return null;

    } finally {
      LOGGER.info("Unbinding");
      if (connection.isConnected())
        try {
          connection.unBind();
        } catch (LdapException e) {
          LOGGER.error(e.getLocalizedMessage(), e);
          return user;
        }
    }

    LOGGER.info("Search completed with following results in " + time + " ms:");
    LOGGER.info("-" + user.getUsername());
    LOGGER.info("- " + user.getDisplayname());


    return user;
  }

  private String getStringOrNull(Attribute attribute) throws LdapInvalidAttributeValueException {
    return attribute != null ? attribute.getString() : null;
  }


}
