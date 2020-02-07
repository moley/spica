package org.spica.server.user.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.user.domain.User;
import org.spica.server.user.domain.UserRepository;
import org.spica.server.user.model.UserInfo;
import org.spica.server.user.service.LdapUserImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController implements UserApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private LdapUserImporter ldapUserImporter;

    @Autowired
    UserRepository userRepository;

    private UserMapper userMapper = new UserMapper();

    @Override
    public ResponseEntity<List<UserInfo>> getUsers() {
        LOGGER.info("getUsers called");
        List<User> users = userRepository.findAll();
        LOGGER.info("Found " + users.size() + " users in database");
        List<UserInfo> userInfos = new ArrayList<>();
        for (User nextUser: users) {
            userInfos.add(userMapper.toApi(nextUser));
        }
        LOGGER.info("Return " + userInfos.size() + " users to client");
        return new ResponseEntity<List<UserInfo>>(userInfos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> refreshUsers() {
        LOGGER.info("refreshUsers called");
        ldapUserImporter.getUsers();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
