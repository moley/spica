package org.spica.server.user.service;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.message.BindRequest;
import org.apache.directory.api.ldap.model.message.BindRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.server.user.config.LdapConfiguration;
import org.spica.server.user.domain.User;

/**
 * Created by OleyMa on 03.07.15.
 */
public class LdapUserProvider implements UserProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserProvider.class);



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
    LOGGER.info("Requesting user info for user " + username + " and password (" + password.length() + ")");

    SpicaProperties leguanProperties = new SpicaProperties();

    User user = new User();

    String ldapHostname = leguanProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_HOST);
    Integer ldapPort = leguanProperties.getValueAsInt(leguanProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_PORT));
    String ldapBaseDn = leguanProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_BASE_DN);
    String ldapUsernameField = leguanProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_FIELD_USERNAME);
    String ldapDisplaynameField = leguanProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_FIELD_DISPLAYNAME);


    LdapConnection connection = createLdapConnection(ldapHostname, ldapPort, false);

    LOGGER.info("Created connection with");
    LOGGER.info("Host              : " + ldapHostname);
    LOGGER.info("BaseDn            : " + ldapBaseDn);
    LOGGER.info("Username Field    : " + ldapUsernameField);
    LOGGER.info("Displayname Field : " + ldapDisplaynameField);

    long time = 0;

    try {

      BindRequest br = createBindRequest();
      //br.setName(ldapBindDn);
      //br.setCredentials(ldapBindPassword);
      br.setName("INTRA\\" + username);
      br.setCredentials(password);
      br.setVersion3(true);

      connection.bind(br);


      LOGGER.info("Authenticated with name " + br.getName() + " and credentials " + br.getCredentials() + ": " + connection.isAuthenticated());
      if (connection.isAuthenticated()) {

        long from = System.currentTimeMillis();

        String searchUsersFilter = "(" + ldapUsernameField + "=" + username + ")";

        LOGGER.info("Bind to user completed");
        LOGGER.info("Search with baseDn <" + ldapBaseDn + ">, user filter <" + searchUsersFilter + ">");

        EntryCursor cursor = connection.search(ldapBaseDn, searchUsersFilter, SearchScope.SUBTREE, ldapUsernameField, ldapDisplaynameField);

        if (! cursor.next()) {
          LOGGER.error("No user found for username " + username);
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

          LOGGER.info("Found user " + usernameFound + " with password (" + password.length() + ") and displayname " + displaynameFound);

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
