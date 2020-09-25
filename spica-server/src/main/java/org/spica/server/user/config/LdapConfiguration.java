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
   * boolean configuration if ssl is used
   */
  String PROPERTY_LDAP_SSL = "spica.ldap.ssl";

  String PROPERTY_LDAP_USERIMPORT_ENABLED = "spica.ldap.userimport.enabled";

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
   * The userfilter to use
   */
  String PROPERTY_LDAP_USERFILTER = "spica.ldap.userfilter";

  /**
   * The field to be used as username
   */
  String PROPERTY_LDAP_FIELD_USERNAME = "spica.ldap.field.username";

  /**
   * The field to be used as displayname
   */
  String PROPERTY_LDAP_FIELD_DISPLAYNAME = "spica.ldap.field.displayname";

  /**
   * The field to be used as first name
   */
  String PROPERTY_LDAP_FIELD_FIRSTNAME = "spica.ldap.field.firstname";

  /**
   * The field to be used as surename
   */
  String PROPERTY_LDAP_FIELD_SURENAME = "spica.ldap.field.surename";

  /**
   * The field to be used as phone
   */
  String PROPERTY_LDAP_FIELD_PHONE = "spica.ldap.field.phone";

  /**
   * The field to be used as mail
   */
  String PROPERTY_LDAP_FIELD_MAIL = "spica.ldap.field.mail";

}
