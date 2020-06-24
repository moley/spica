package org.spica.server.demodata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.commons.demodata.DemoDataPropertiesConfigurator;
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
public class DemoDataCreatorServer {


  private final static Logger LOGGER = LoggerFactory.getLogger(DemoDataCreatorServer.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserGroupRepository userGroupRepository;

  @Autowired
  private UserGroupMemberRepository userGroupMemberRepository;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private ProjectMemberRepository projectMemberRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private SkillRepository skillRepository;

  @Autowired
  private MessagecontainerRepository messageContainerRepository;

  private DemoDataPropertiesConfigurator demoDataPropertiesConfigurator = new DemoDataPropertiesConfigurator();

  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12); //TODO inject

  private final static String CONCEPTION_DEPARTMENT = "Konzeption";
  private final static String HOTLINE_DEPARTMENT = "Hotline";
  private final static String DEV_DEPARTMENT = "Development";

  private final static String PRODUKT_1 = "Produkt 1";
  private final static String PRODUKT_2 = "Produkt 2";

  private final static String ISSUE_PERFORMANCE = "Performance is too bad";
  private final static String ISSUE_USABILITY = "Usability is like Windows 95";
  private final static String ISSUE_STABILITY = "Stability is even worse";

  public void create () throws IOException {
    LOGGER.info("Start creating development demo data");

    List<Skill> skills = skills("Java", "Junit4", "C#", "Jira", "Python", "Angular", "Agility");


    User dev1 = user("Mayer", "Marc", Role.USER, Arrays.asList(skills.get(0), skills.get(1)));
    User dev2 = user("User", "Uriah", Role.USER, new ArrayList<>());
    User hotliner = user("HotlineMan", "Harry", Role.USER, new ArrayList<>());
    User conception = user("ConceptWriter", "Cade", Role.USER, new ArrayList<>());
    User guest = user("Guest", "George", Role.GUEST, new ArrayList<>());
    User admin = user ("Admin", "Andy", Role.ADMIN, new ArrayList<>());
    User teamlead = user ("Teamlead", "Tom", Role.USER, new ArrayList<>());

    UserGroup departmentConception = userGroup(CONCEPTION_DEPARTMENT, conception, false);
    UserGroup departmentHotline = userGroup(HOTLINE_DEPARTMENT, hotliner, false);
    UserGroup departmentDevelopment = userGroup(DEV_DEPARTMENT, dev1, false);
    userGroup(DEV_DEPARTMENT, dev2, false);

    userGroup(CONCEPTION_DEPARTMENT, teamlead, true);
    userGroup(HOTLINE_DEPARTMENT, admin, true);

    Project product1 = project(PRODUKT_1, admin, null, departmentConception, departmentDevelopment);
    Project product2 = project(PRODUKT_2, admin, null, departmentConception, departmentHotline, departmentDevelopment);

    Task taskIssueStability = topic(ISSUE_STABILITY, guest, product1);
    Task taskIssuePerformance = topic(ISSUE_PERFORMANCE, guest, product2);
    Task taskIssueUsability = topic(ISSUE_USABILITY, teamlead, product2);

    meep(guest, product1, "Hi, I have seen your application on your homepage, can I buy it?");

    meep(guest, taskIssuePerformance, "Why is my application soooooo slow?");
    meep(hotliner, taskIssuePerformance, "Which version do you have?");
    meep(guest, taskIssuePerformance, "0.8.15");
    meep(hotliner, taskIssuePerformance, "@Mayer Can you please take a look?");
    meep(dev1, taskIssuePerformance, "On my machine it is fast, can you try it on new hardware");

    meep(guest, taskIssueStability, "My desk, on which the PC with the application runs, has broken down. And now?");

    meep(conception, taskIssueUsability, "I believe the new version of your application is unusable");

    demoDataPropertiesConfigurator.configurations(new File(".spica"));
    SpicaProperties.close(); //to enable reloading
  }


  protected User user (final String name, final String firstname, final Role role, final List<Skill> skills) {
    LOGGER.info("Create new user " + name + "," + firstname + " with role " + role.name());
    String username = name.substring(0, 1).toLowerCase() + firstname.substring(0,1).toLowerCase();
    String mail = firstname.toLowerCase() + "." + name.toLowerCase() + "@spica.org";
    String encodedPassword = passwordEncoder.encode(username);
    User user = User.builder().name(name).firstname(firstname).username(username).createdAt(LocalDateTime.now()).email(mail).role(role).password(encodedPassword).build();
    List<String> skillIds = new ArrayList<String>();
    for (Skill next: skills) {
      skillIds.add(String.valueOf(next.getId()));
    }
    user.setSkills(String.join(",", skillIds));
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
      skills.add(skill);
    }
    return skills;

  }

  protected Task topic (final String name, final User creator, Project project) {
    LOGGER.info("User " + creator.getUsername() + " creates new topic " + name + " in project " + project.getName());

    Task task = new Task();
    task.setName(name);
    task.setProjectID(project.getId());
    task.setCreatorID(creator.getId());
    taskRepository.save(task);
    return task;
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
