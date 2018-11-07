package org.spica.server.demodata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.commons.Role;
import org.spica.server.user.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DemoDataCreator {


  private final static Logger LOGGER = LoggerFactory.getLogger(DemoDataCreator.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserGroupRepository userGroupRepository;

  @Autowired
  private UserGroupMemberRepository userGroupMemberRepository;

  protected User user (final String name, final String firstname, final Role role) {
    LOGGER.info("Create new user " + name + "," + firstname + " with role " + role.name());
    String username = name.substring(0, 1) + firstname.substring(0,1);
    String mail = firstname.toLowerCase() + "." + name.toLowerCase() + "@somedomain.org";
    User user = User.builder().name(name).firstname(firstname).username(username).createdAt(LocalDateTime.now()).email(mail).role(role).password(username).build();
    userRepository.save(user);
    return user;
  }

  protected UserGroup userGroup(final String name, User user, final boolean groupLead) {
    UserGroup userGroup = userGroupRepository.findByName(name);
    if (userGroup == null) {
      LOGGER.info("Create new usergroup " + name);
      userGroup = new UserGroup();
      userGroup.setName(name);
      userGroupRepository.save(userGroup);
    }

    UserGroupMember userGroupMember = new UserGroupMember();
    userGroupMember.setUser(user);
    userGroupMember.setUserGroup(userGroup);
    userGroupMember.setGroupLead(groupLead);
    userGroupMemberRepository.save(userGroupMember);
    return userGroup;
  }




}
