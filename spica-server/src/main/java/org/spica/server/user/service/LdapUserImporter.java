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
import org.spica.commons.credentials.PasswordMask;
import org.spica.server.user.config.LdapConfiguration;
import org.spica.server.user.domain.User;
import org.spica.server.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 */
@Component
public class LdapUserImporter implements UserImporter {

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserImporter.class);

    @Override
    public Collection<User> getUsers() {

        SpicaProperties spicaProperties = new SpicaProperties();

        Collection<User> users = new ArrayList<User>();

        if (spicaProperties.getValueAsBoolean(LdapConfiguration.PROPERTY_LDAP_USERIMPORT_ENABLED)) {
            String host = spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_HOST);
            final boolean isSSl = spicaProperties.getValueAsBoolean(LdapConfiguration.PROPERTY_LDAP_SSL);
            final String bindDn = spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_BIND_DN);
            final String pw = spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_BIND_PASSWORD);
            String stammDn = spicaProperties.getValueNotNull(LdapConfiguration.PROPERTY_LDAP_BASE_DN);
            String searchUsersFilter =spicaProperties.getValueOrDefault(LdapConfiguration.PROPERTY_LDAP_USERFILTER, "(objectClass=*)");

            String displaynameKey = spicaProperties.getValueOrDefault(LdapConfiguration.PROPERTY_LDAP_FIELD_DISPLAYNAME, "displayname");
            String usernameKey = spicaProperties.getValueOrDefault(LdapConfiguration.PROPERTY_LDAP_FIELD_USERNAME, "username");
            String firstnameKey = spicaProperties.getValueOrDefault(LdapConfiguration.PROPERTY_LDAP_FIELD_FIRSTNAME, "firstname");
            String surenameKey = spicaProperties.getValueOrDefault(LdapConfiguration.PROPERTY_LDAP_FIELD_SURENAME, "sn");
            String mailKey = spicaProperties.getValueOrDefault(LdapConfiguration.PROPERTY_LDAP_FIELD_MAIL, "mail");
            String phoneKey = spicaProperties.getValueOrDefault(LdapConfiguration.PROPERTY_LDAP_FIELD_PHONE, "telephoneNumber");

            int port = Integer.valueOf(spicaProperties.getValueOrDefault(LdapConfiguration.PROPERTY_LDAP_PORT, "3268"));

            LdapConnection connection = new LdapNetworkConnection(host, port, false);

            LOGGER.info("Starting importing users from LDAP with following configurations");
            LOGGER.info("Host              : " + host);
            LOGGER.info("SSL               : " + isSSl);
            LOGGER.info("BindDn            : " + bindDn);
            LOGGER.info("Password          : " + new PasswordMask().getMaskedPassword(pw));
            LOGGER.info("BaseDn            : " + stammDn);
            LOGGER.info("SearchUserFilter  : " + searchUsersFilter);
            LOGGER.info("Port              : " + port);
            LOGGER.info("Field displayname : " + displaynameKey);
            LOGGER.info("Field username    : " + usernameKey);
            LOGGER.info("Field firstname   : " + firstnameKey);
            LOGGER.info("Field surename    : " + surenameKey);
            LOGGER.info("Field mail        : " + mailKey);
            LOGGER.info("Field phone       : " + phoneKey);

            int number = 0;
            long time = 0;

            try {

                BindRequest br = new BindRequestImpl();
                br.setName(bindDn);
                br.setCredentials(pw);
                br.setVersion3(true);

                connection.bind(br);


                LOGGER.info("Authenticated: " + connection.isAuthenticated());
                if (connection.isAuthenticated()) {

                    long from = System.currentTimeMillis();

                    LOGGER.info("Bind to ldap completed");
                    LOGGER.info("Search with baseDn <" + stammDn + ">, user filter <" + searchUsersFilter + ">");
                    EntryCursor cursor = connection.search(stammDn, searchUsersFilter, SearchScope.SUBTREE, "*");

                    while (cursor.next()) {
                        Entry entry = cursor.get();

                        String username = getStringOrNull(entry.get(usernameKey));
                        String firstname = getStringOrNull(entry.get(firstnameKey));
                        String surename = getStringOrNull(entry.get(surenameKey));
                        String mail = getStringOrNull(entry.get(mailKey));
                        String phone = getStringOrNull(entry.get(phoneKey));

                        if (username != null && surename != null && firstname != null ) {

                            if (LOGGER.isDebugEnabled())
                            LOGGER.debug("Import entry with (" +  usernameKey + "=" + username + ", " +
                                firstnameKey + "=" + firstname + "," +
                                surenameKey + "=" + surename + "," +
                                mailKey + "=" + mail + "," +
                                phoneKey + "=" + phone + ")");

                            User user = userRepository.findByUsername(username);
                            if (user == null) {
                                user = new User();
                                user.setUsername(username);
                                user.setCreatedAt(LocalDateTime.now());
                            }

                            user.setEnabled(true);
                            user.setEmail(mail);
                            user.setFirstname(firstname);
                            user.setName(surename);
                            user.setPhone(phone);
                            users.add(user);
                            userRepository.save(user);

                            number++;
                            if (LOGGER.isDebugEnabled())
                              LOGGER.debug("Imported user " +  usernameKey);
                        }
                        else {
                            if (mail != null || phone != null || username != null || phone != null || firstname != null || surename != null)
                                LOGGER.warn("Cannot import entry with (" +  usernameKey + "=" + username + ", " +
                                  firstnameKey + "=" + firstname + "," +
                                  surenameKey + "=" + surename + "," +
                                  mailKey + "=" + mail + "," +
                                  phoneKey + "=" + phone + ")");
                        }

                    }

                    time = System.currentTimeMillis() - from;
                } else
                    throw new IllegalStateException("Invalid binding");

            } catch (Exception e) {
                throw new IllegalStateException(e);

            } finally {
                LOGGER.info("Unbinding");
                if (connection.isConnected())
                    try {
                        connection.unBind();
                    } catch (LdapException e) {
                        throw new IllegalStateException(e);
                    }
            }

            LOGGER.info("Search completed with " + number + " results in " + time + " ms");
        }
        else
            LOGGER.warn("Importing LDAP Users is disabled by configuration");

        return users;
    }

    private String getStringOrNull (Attribute attribute) throws LdapInvalidAttributeValueException {
        return attribute != null ? attribute.getString() : null;
    }





}
