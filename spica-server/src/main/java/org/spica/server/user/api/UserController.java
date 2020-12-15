package org.spica.server.user.api;

import io.swagger.annotations.ApiParam;
import java.util.Collection;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.user.domain.Skill;
import org.spica.server.user.domain.SkillRepository;
import org.spica.server.user.domain.User;
import org.spica.server.user.domain.UserRepository;
import org.spica.server.user.model.SkillInfo;
import org.spica.server.user.model.UserInfo;
import org.spica.server.user.service.LdapUserImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController implements UserApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private LdapUserImporter ldapUserImporter;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    UserRepository userRepository;

    private SkillMapper skillMapper = new SkillMapper();

    private UserMapper userMapper = new UserMapper();

    @Override public ResponseEntity<UserInfo> findUser(@NotNull @Valid String username) {
        return new ResponseEntity<UserInfo>(userMapper.toApi(userRepository.findByUsername(username)), HttpStatus.OK);

    }

    @Override public ResponseEntity<List<SkillInfo>> getUserSkills(@NotNull @Valid String userId) {
        Optional<User> userById = userRepository.findById(userId);
        if (userById.isEmpty())
            throw new IllegalStateException("User with user id " + userId + " not found");

        User user = userById.get();

        List<Skill> allSkills = skillRepository.findAll();
        return new ResponseEntity<List<SkillInfo>>(skillMapper.toApi(allSkills, user.getSkills()), HttpStatus.OK);
    }

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

    @Override
    public ResponseEntity<Void> setUserSkills(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "userId", required = true) String userId,@ApiParam(value = "" ,required=true )  @Valid @RequestBody List<SkillInfo> skillInfo) {
        Collection<String> ids = new ArrayList<String>();
        List<Skill> allSkills = skillRepository.findAll();

        for (SkillInfo next: skillInfo) {
            LOGGER.info("Found skill " + next);
            Skill nextSkill = skillMapper.findSkill(allSkills, next.getId());
            ids.add(String.valueOf(nextSkill.getId()));
        }

        User foundUser = userRepository.findById(userId).get();
        foundUser.setSkills(String.join(",", ids));
        userRepository.save(foundUser);
        LOGGER.info("Set skill " + foundUser.getSkills() + " for user " + userId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
