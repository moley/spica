package org.spica.server.demodata;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.communication.domain.Message;
import org.spica.server.communication.domain.MessageRepository;
import org.spica.server.communication.domain.Messagecontainer;
import org.spica.server.communication.domain.MessagecontainerRepository;
import org.spica.server.commons.Idable;
import org.spica.server.commons.MemberShipType;
import org.spica.server.commons.ReferenceType;
import org.spica.server.commons.Role;
import org.spica.server.project.domain.*;
import org.spica.server.user.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private TopicRepository topicRepository;

  @Autowired
  private ProjectMemberRepository projectMemberRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private SkillRepository skillRepository;

  @Autowired
  private MessagecontainerRepository messageContainerRepository;

  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12); //TODO inject


  protected User user (final String name, final String firstname, final Role role) {
    LOGGER.info("Create new user " + name + "," + firstname + " with role " + role.name());
    String username = name.substring(0, 1).toLowerCase() + firstname.substring(0,1).toLowerCase();
    String mail = firstname.toLowerCase() + "." + name.toLowerCase() + "@somedomain.org";
    String encodedPassword = passwordEncoder.encode(username);
    User user = User.builder().name(name).firstname(firstname).username(username).createdAt(LocalDateTime.now()).email(mail).role(role).password(encodedPassword).build();
    userRepository.save(user);
    return user;
  }

  protected UserGroup userGroup(final String name, User user, final boolean groupLead) {
    LOGGER.info("User " + user.getUsername() + " added to usergroup " + name + " (groupLead " + groupLead + ")");
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

  protected Project project (final String name, final User creator, final Project parentProject, final Idable... members) {
    LOGGER.info("User " + creator.getUsername() + " creates new project " + name + " with parent " + parentProject + " and " + members.length + " members");
    Project project = new Project();
    project.setParentProject(parentProject);
    project.setName(name);
    project.setCreatorID(creator.getId());
    projectRepository.save(project);

    for (Idable nextMember: members) {
      ProjectMember projectMember = new ProjectMember();
      projectMember.setProject(project);
      if (nextMember.equals(members[0]))
        projectMember.setProjectLead(true);

      if (nextMember instanceof User) {
        projectMember.setMemberShipType(MemberShipType.USER);
      }
      else if (nextMember instanceof UserGroup) {
        projectMember.setMemberShipType(MemberShipType.USERGROUP);
      }
      else
        throw new IllegalStateException("Not supported membership type " + nextMember.getClass().getSimpleName());

      projectMember.setMemberID(nextMember.getId());

      projectMemberRepository.save(projectMember);
    }

    return project;
  }

  protected List<Skill> skills (final String ... descriptions) {
    LOGGER.info("Skills <" + descriptions + "> are created");
    List<Skill> skills = new ArrayList<Skill>();
    for (String next: descriptions) {
      Skill skill = new Skill();
      skill.setDescription(next);
      skillRepository.save(skill);
    }
    return skills;

  }

  protected Topic topic (final String name, final User creator, Project project) {
    LOGGER.info("User " + creator.getUsername() + " creates new topic " + name + " in project " + project.getName());

    Topic topic = new Topic();
    topic.setName(name);
    topic.setProjectID(project.getId());
    topic.setCreatorID(creator.getId());
    topicRepository.save(topic);
    return topic;
  }

  protected Message meep (final User creator, Idable reference, final String message) {
    Message meep = new Message();
    meep.setCreationDate(LocalDateTime.now());
    meep.setCreatorId(creator.getId());
    meep.setMessage(message);

    String referenceID = reference.getId();
    ReferenceType referenceType = ReferenceType.valueOf(reference.getClass().getSimpleName().toUpperCase());

    Messagecontainer container = messageContainerRepository.findByReferenceTypeAndReferenceID(referenceType, referenceID);
    if (container == null) {
      container = new Messagecontainer();
      container.setReferenceType(referenceType);
      container.setReferenceID(referenceID);
    }

    container.getMessages().add(meep);
    messageContainerRepository.save(container);

    return meep;



  }


  public PasswordEncoder getPasswordEncoder() {
    return passwordEncoder;
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }
}
