package org.spica.server.security;

public interface LDAPConfiguration {
  /**
   * The host to be accessed on the LDAP request
   */
  String PROPERTY_LDAP_HOST = "eplanner.ldap.host";

  /**
   * boolean configuration if ssl is used
   */
  String PROPERTY_LDAP_SSL = "eplanner.ldap.ssl";

  String PROPERTY_LDAP_USERIMPORT_ENABLED = "eplanner.ldap.userimport.enabled";

  /**
   * The port to be accessed on the LDAP request
   */
  String PROPERTY_LDAP_PORT = "eplanner.ldap.port";

  /**
   * The bind dn for the request
   */
  String PROPERTY_LDAP_BIND_DN = "eplanner.ldap.bind.dn";

  /**
   * The password for the request
   */
  String PROPERTY_LDAP_BIND_PASSWORD = "eplanner.ldap.bind.password";

  /**
   * The base dn of the request
   */
  String PROPERTY_LDAP_BASE_DN = "eplanner.ldap.base.dn";

  /**
   * The userfilter to use
   */
  String PROPERTY_LDAP_USERFILTER = "eplanner.ldap.userfilter";

  /**
   * The field to be used as username
   */
  String PROPERTY_LDAP_FIELD_USERNAME = "eplanner.ldap.field.username";

  /**
   * The field to be used as displayname
   */
  String PROPERTY_LDAP_FIELD_DISPLAYNAME = "eplanner.ldap.field.displayname";

  /**
   * The field to be used as first name
   */
  String PROPERTY_LDAP_FIELD_FIRSTNAME = "eplanner.ldap.field.firstname";

  /**
   * The field to be used as surename
   */
  String PROPERTY_LDAP_FIELD_SURENAME = "eplanner.ldap.field.surename";

  /**
   * The field to be used as phone
   */
  String PROPERTY_LDAP_FIELD_PHONE = "eplanner.ldap.field.phone";

  /**
   * The field to be used as mail
   */
  String PROPERTY_LDAP_FIELD_MAIL = "eplanner.ldap.field.mail";
}
