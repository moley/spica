package org.spica.server.software.service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spica.commons.SpicaProperties;
import org.spica.server.software.domain.Contact;
import org.spica.server.software.domain.Software;
import org.spica.server.software.model.ContactInfo;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.SoftwareInfo;

@Slf4j
public class SoftwareMapperTest {

  private SoftwareMapper softwareMapper = new SoftwareMapper();

  private final String ID = "ID";
  private final String ID_PARENT = "ID_PARENT";
  private final String NAME = "NAME";
  private final String STATE = "spica.software.state.productive";
  private final String TYPE = "spica.software.type.soap";
  private final String GROUP = "spica.software.group.automation";
  private final String DEPLOYMENT = "spica.software.deployment.cloud";
  private final String PARENT_ID = "PARENT_ID";
  private final String DESCRIPTION = "DESCRIPTION";
  private final Integer COMPLEXITY = 5;
  private final String FORMAT = "FORMAT";
  private final String LOCATION = "LOCATION";
  private final Integer MAINTAINABILITY = 2;
  private final String NEEDS_ACTION = "NEEDS_ACTION";
  private final String REQUIREMENT = "REQUIREMENT";
  private final String BUGTRACKING = "BUGTRACKING";
  private final String BUILDSYSTEM = "BUILDSYSTEM";
  private final String CONTACT_USER_NAME = "CONTACT_USER_NAME";
  private final String CONTACT_USER_ROLE = "spica.software.role.productowner";
  private final String CONTACT_TEAM_NAME = "CONTACT_TEAM_NAME";
  private final String CONTACT_TEAM_ROLE = "spica.software.role.developer";
  private final Integer TECHNICAL_DEBT = 3;
  private final Integer CHANGE_FREQUENCY = 2;
  private final String TECHNOLOGY1 = "Gradle";
  private final String TECHNOLOGY2 = "Java";
  private final List<String> TECHNOLOGIES = Arrays.asList(TECHNOLOGY1, TECHNOLOGY2);
  private final String VCS = "VCS";
  private final String ARCHITECTURE_EXCEPTIONS = "ARCHITECTURE_EXCEPTIONS";
  private final String DEPLOYMENT_NAME = "DEPLOYMENT_NAME";




  private SpicaProperties spicaProperties = new SpicaProperties();


  @Test
  public void toSoftwareEntityInvalidDeployment () {
    SoftwareInfo softwareInfo = new SoftwareInfo();
    softwareInfo.setDeployment(new IdAndDisplaynameInfo().displayname("something").id("something"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> softwareMapper.toSoftwareEntity(softwareInfo));
  }

  @Test
  public void toSoftwareEntity () {
    log.info("Read from " + new File("").getAbsolutePath());

    SoftwareInfo softwareInfo = new SoftwareInfo();
    softwareInfo.setId(ID);
    softwareInfo.setName(NAME);
    softwareInfo.setState(softwareMapper.toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(STATE)));
    softwareInfo.setType(softwareMapper.toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(TYPE)));
    softwareInfo.setGroup(softwareMapper.toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(GROUP)));
    softwareInfo.setDeployment(softwareMapper.toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(DEPLOYMENT)));
    softwareInfo.setParentId(PARENT_ID);
    softwareInfo.setDescription(DESCRIPTION);
    softwareInfo.setActive(true);
    softwareInfo.setChildren(Arrays.asList(new SoftwareInfo().id(ID_PARENT)));
    softwareInfo.setComplexity(COMPLEXITY);
    softwareInfo.setFormat(FORMAT);
    softwareInfo.setLocation(LOCATION);
    softwareInfo.setMaintainability(MAINTAINABILITY);
    softwareInfo.setNeedsAction(true);
    softwareInfo.setNeedsActionDescription(NEEDS_ACTION);
    softwareInfo.setRequirement(REQUIREMENT);
    softwareInfo.setContactsUser(Arrays.asList(new ContactInfo().contactId(CONTACT_USER_NAME).role(new IdAndDisplaynameInfo().displayname(
        CONTACT_USER_ROLE).id(CONTACT_USER_ROLE))));
    softwareInfo.setContactsTeam(Arrays.asList(new ContactInfo().contactId(CONTACT_TEAM_NAME).role(new IdAndDisplaynameInfo().displayname(
        CONTACT_TEAM_ROLE).id(CONTACT_TEAM_ROLE))));
    softwareInfo.setTechnicalDebt(TECHNICAL_DEBT);
    softwareInfo.setTechnologies(Arrays.asList(new IdAndDisplaynameInfo().displayname(TECHNOLOGY1).id(TECHNOLOGY1),
                                               new IdAndDisplaynameInfo().displayname(TECHNOLOGY2).id(TECHNOLOGY2)));
    softwareInfo.setVcs(VCS);
    softwareInfo.setFitsArchitecture(Boolean.TRUE);
    softwareInfo.setArchitectureExceptions(ARCHITECTURE_EXCEPTIONS);
    softwareInfo.setBugtracking(BUGTRACKING);
    softwareInfo.setBuildsystem(BUILDSYSTEM);
    softwareInfo.setChangeFrequency(CHANGE_FREQUENCY);
    softwareInfo.setWithMonitoring(true);
    softwareInfo.setWithOnlineHelp(true);
    softwareInfo.setWithPersistence(true);
    softwareInfo.setWithUi(true);
    softwareInfo.setWithSecurity(true);
    softwareInfo.setDeploymentName(DEPLOYMENT_NAME);
    softwareInfo.setTargetDate("12.10.2012");


    Software software = softwareMapper.toSoftwareEntity(softwareInfo);

    Assert.assertEquals(ID, software.getId());
    Assert.assertEquals(NAME, software.getName());
    Assert.assertEquals(STATE, software.getState());
    Assert.assertEquals(TYPE, software.getType());
    Assert.assertEquals(GROUP, software.getSoftwaregroup());
    Assert.assertEquals(DEPLOYMENT, software.getDeployment());
    Assert.assertEquals(PARENT_ID, software.getParentId());
    Assert.assertEquals (DESCRIPTION, software.getDescription());
    Assert.assertTrue(software.getActive());
    Assert.assertEquals (1, software.getChildren().size());
    Assert.assertEquals(ID_PARENT, software.getChildren().get(0).getId());
    Assert.assertEquals (COMPLEXITY, software.getComplexity());
    Assert.assertEquals (FORMAT, software.getFormat());
    Assert.assertEquals(LOCATION, software.getLocation());
    Assert.assertEquals (MAINTAINABILITY, software.getMaintainability());
    Assert.assertTrue(software.getNeedsAction());
    Assert.assertEquals(NEEDS_ACTION, software.getNeedsActionDescription());
    Assert.assertEquals(REQUIREMENT, software.getRequirement());

    Assert.assertEquals(1, software.getContactsTeam().size());
    Assert.assertNotNull (software.getContactsTeam().get(0).getId());
    Assert.assertEquals(CONTACT_TEAM_NAME, software.getContactsTeam().get(0).getContactId());
    Assert.assertEquals(CONTACT_TEAM_ROLE, software.getContactsTeam().get(0).getRole());

    Assert.assertEquals(1, software.getContactsUser().size());
    Assert.assertNotNull (software.getContactsUser().get(0).getId());
    Assert.assertEquals(CONTACT_USER_NAME, software.getContactsUser().get(0).getContactId());
    Assert.assertEquals(CONTACT_USER_ROLE, software.getContactsUser().get(0).getRole());

    Assert.assertEquals(TECHNICAL_DEBT, software.getTechnicalDebt());
    Assert.assertEquals(2, software.getTechnologies().size());
    Assert.assertEquals(TECHNOLOGY1, software.getTechnologies().get(0));
    Assert.assertEquals(TECHNOLOGY2, software.getTechnologies().get(1));
    Assert.assertEquals (VCS, software.getVcs());
    Assert.assertTrue (software.getFitsArchitecture());
    Assert.assertEquals (ARCHITECTURE_EXCEPTIONS, software.getArchitectureExceptions());
    Assert.assertEquals(BUGTRACKING, software.getBugtracking());
    Assert.assertEquals(BUILDSYSTEM, software.getBuildsystem());
    Assert.assertEquals (CHANGE_FREQUENCY, software.getChangeFrequency());

    Assert.assertTrue (software.getWithMonitoring());
    Assert.assertTrue (software.getWithPersistence());
    Assert.assertTrue (software.getWithOnlineHelp());
    Assert.assertTrue (software.getWithSecurity());
    Assert.assertTrue (software.getWithUi());

    Assert.assertEquals(DEPLOYMENT_NAME, software.getDeploymentName());
    Assert.assertEquals ("2012-10-12", software.getTargetDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
  }

  @Test
  public void toSoftwareInfo () {
    Software software = new Software();
    software.setId(ID);
    software.setName(NAME);
    software.setState(STATE);
    software.setType(TYPE);
    software.setSoftwaregroup(GROUP);
    software.setDeployment(spicaProperties.getKeyValuePair(DEPLOYMENT).getKey());
    software.setParentId(PARENT_ID);
    software.setDescription(DESCRIPTION);
    software.setActive(true);
    Software childSoftware = new Software();
    childSoftware.setId(ID_PARENT);
    software.setChildren(Arrays.asList(childSoftware));
    software.setComplexity(COMPLEXITY);
    software.setFormat(FORMAT);
    software.setLocation(LOCATION);
    software.setMaintainability(MAINTAINABILITY);
    software.setNeedsAction(true);
    software.setNeedsActionDescription(NEEDS_ACTION);
    software.setRequirement(REQUIREMENT);

    Contact contactTeam = new Contact();
    contactTeam.setContactId(CONTACT_TEAM_NAME);
    contactTeam.setRole(CONTACT_TEAM_ROLE);
    software.setContactsTeam(Arrays.asList(contactTeam));

    Contact contactUser = new Contact();
    contactUser.setContactId(CONTACT_USER_NAME);
    contactUser.setRole(CONTACT_USER_ROLE);
    software.setContactsUser(Arrays.asList(contactUser));

    software.setTechnicalDebt(TECHNICAL_DEBT);
    software.setTechnologies(TECHNOLOGIES);
    software.setVcs(VCS);
    software.setFitsArchitecture(true);
    software.setArchitectureExceptions(ARCHITECTURE_EXCEPTIONS);
    software.setBuildsystem(BUILDSYSTEM);
    software.setBugtracking(BUGTRACKING);

    software.setChangeFrequency(CHANGE_FREQUENCY);
    software.setWithMonitoring(true);
    software.setWithOnlineHelp(true);
    software.setWithPersistence(true);
    software.setWithUi(true);
    software.setWithSecurity(true);
    software.setDeploymentName(DEPLOYMENT_NAME);
    software.setTargetDate(LocalDate.of(2020, 12, 10));

    SoftwareInfo softwareInfo = softwareMapper.toSoftwareInfo(software);

    Assert.assertEquals(ID, softwareInfo.getId());
    Assert.assertEquals(NAME, softwareInfo.getName());
    Assert.assertEquals(STATE, softwareInfo.getState().getId());
    Assert.assertEquals(TYPE, softwareInfo.getType().getId());
    Assert.assertEquals(GROUP, softwareInfo.getGroup().getId());
    Assert.assertEquals(DEPLOYMENT, softwareInfo.getDeployment().getId());
    Assert.assertEquals(PARENT_ID, softwareInfo.getParentId());
    Assert.assertEquals (DESCRIPTION, softwareInfo.getDescription());
    Assert.assertTrue(softwareInfo.getActive());
    Assert.assertEquals (1, softwareInfo.getChildren().size());
    Assert.assertEquals(ID_PARENT, softwareInfo.getChildren().get(0).getId());
    Assert.assertEquals (COMPLEXITY, softwareInfo.getComplexity());
    Assert.assertEquals (FORMAT, softwareInfo.getFormat());
    Assert.assertEquals(LOCATION, softwareInfo.getLocation());
    Assert.assertEquals (MAINTAINABILITY, softwareInfo.getMaintainability());
    Assert.assertTrue(software.getNeedsAction());
    Assert.assertEquals(NEEDS_ACTION, softwareInfo.getNeedsActionDescription());
    Assert.assertEquals(REQUIREMENT, softwareInfo.getRequirement());

    Assert.assertEquals(1, softwareInfo.getContactsTeam().size());
    Assert.assertEquals(CONTACT_TEAM_NAME, softwareInfo.getContactsTeam().get(0).getContactId());
    Assert.assertEquals(CONTACT_TEAM_ROLE, softwareInfo.getContactsTeam().get(0).getRole().getId());

    Assert.assertEquals(1, softwareInfo.getContactsUser().size());
    Assert.assertEquals(CONTACT_USER_NAME, softwareInfo.getContactsUser().get(0).getContactId());
    Assert.assertEquals(CONTACT_USER_ROLE, softwareInfo.getContactsUser().get(0).getRole().getId());

    Assert.assertEquals(TECHNICAL_DEBT, softwareInfo.getTechnicalDebt());
    Assert.assertEquals(2, softwareInfo.getTechnologies().size());
    Assert.assertEquals(TECHNOLOGY1, softwareInfo.getTechnologies().get(0).getId());
    Assert.assertEquals(TECHNOLOGY2, softwareInfo.getTechnologies().get(1).getDisplayname());
    Assert.assertEquals (VCS, softwareInfo.getVcs());
    Assert.assertTrue (softwareInfo.getFitsArchitecture());
    Assert.assertEquals (ARCHITECTURE_EXCEPTIONS, softwareInfo.getArchitectureExceptions());
    Assert.assertEquals (BUGTRACKING, softwareInfo.getBugtracking());
    Assert.assertEquals (BUILDSYSTEM, softwareInfo.getBuildsystem());
    Assert.assertEquals (CHANGE_FREQUENCY, softwareInfo.getChangeFrequency());
    Assert.assertTrue (softwareInfo.getWithMonitoring());
    Assert.assertTrue (softwareInfo.getWithPersistence());
    Assert.assertTrue (softwareInfo.getWithOnlineHelp());
    Assert.assertTrue (softwareInfo.getWithSecurity());
    Assert.assertTrue (softwareInfo.getWithUi());
    Assert.assertEquals (DEPLOYMENT_NAME, softwareInfo.getDeploymentName());
    Assert.assertEquals ("10.12.2020", softwareInfo.getTargetDate());


  }

  @Test
  public void toIdAndDisplaynameInfo () {
    IdAndDisplaynameInfo idAndDisplaynameInfo = softwareMapper.toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(STATE));
    Assert.assertEquals(STATE, idAndDisplaynameInfo.getId());
    Assert.assertEquals("Productive", idAndDisplaynameInfo.getDisplayname());
  }

  @Test
  public void toIdAndDisplaynameInfoNull () {
    IdAndDisplaynameInfo idAndDisplaynameInfo = softwareMapper.toIdAndDisplaynameInfo(null);
    Assert.assertNull (idAndDisplaynameInfo);

  }


}
