package org.spica.server.user.service;

import org.spica.server.user.domain.User;

import java.util.Collection;

public interface UserImporter {

    Collection<User> getUsers ();
}
