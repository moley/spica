package org.spica.server.user.config;

/**
 * Properties which configure the access to a LDAP server to realize authentication
 */
public interface LdapConfiguration {

  /**
   * The host to be accessed on the LDAP request
   */
  String PROPERTY_LDAP_HOST = "spica.ldap.host";

  /**
   * The port to be accessed on the LDAP request
   */
  String PROPERTY_LDAP_PORT = "spica.ldap.port";

  /**
   * The bind dn for the request
   */
  String PROPERTY_LDAP_BIND_DN = "spica.ldap.bind.dn";

  /**
   * The password for the request
   */
  String PROPERTY_LDAP_BIND_PASSWORD = "spica.ldap.bind.password";

  /**
   * The base dn of the request
   */
  String PROPERTY_LDAP_BASE_DN = "spica.ldap.base.dn";

  /**
   * The field to be used as username
   */
  String PROPERTY_LDAP_FIELD_USERNAME = "spica.ldap.field.username";

  /**
   * The field to be used as displayname
   */
  String PROPERTY_LDAP_FIELD_DISPLAYNAME = "spica.ldap.field.displayname";

}
