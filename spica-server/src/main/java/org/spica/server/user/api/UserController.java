package org.spica.server.user.api;

import org.spica.server.user.service.LdapUserImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    @Autowired
    private LdapUserImporter ldapUserImporter;

    @Override
    public ResponseEntity<Void> refreshUsers() {
        ldapUserImporter.getUsers();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
