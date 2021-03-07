package org.spica.server.software.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.spica.commons.SpicaProperties;
import org.spica.server.software.domain.Software;
import org.spica.server.software.domain.TeamMember;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.SoftwareInfo;
import org.spica.server.software.model.TeamMemberInfo;

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
  private final String TEAMMEMBER_NAME = "TEAMMEMBER_NAME";
  private final String TEAMMEMBER_ROLE = "spica.software.role.developer";
  private final Integer TECHNICAL_DEBT = 3;
  private final String TECHNOLOGY1 = "Gradle";
  private final String TECHNOLOGY2 = "Java";
  private final List<String> TECHNOLOGIES = Arrays.asList(TECHNOLOGY1, TECHNOLOGY2);
  private final String VCS = "VCS";




  private SpicaProperties spicaProperties = new SpicaProperties();

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
    softwareInfo.setTeammembers(Arrays.asList(new TeamMemberInfo().user(TEAMMEMBER_NAME).role(TEAMMEMBER_ROLE)));
    softwareInfo.setTechnicalDebt(TECHNICAL_DEBT);
    softwareInfo.setTechnologies(TECHNOLOGIES);
    softwareInfo.setVcs(VCS);

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
    Assert.assertEquals(1, software.getTeamMembers().size());
    Assert.assertEquals(TEAMMEMBER_NAME, software.getTeamMembers().get(0).getUser());
    Assert.assertEquals(TEAMMEMBER_ROLE, software.getTeamMembers().get(0).getRole());
    Assert.assertEquals(TECHNICAL_DEBT, software.getTechnicalDebt());
    Assert.assertEquals(2, software.getTechnologies().size());
    Assert.assertEquals(TECHNOLOGY1, software.getTechnologies().get(0));
    Assert.assertEquals(TECHNOLOGY2, software.getTechnologies().get(1));
    Assert.assertEquals (VCS, software.getVcs());



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
    TeamMember teamMember = new TeamMember();
    teamMember.setUser(TEAMMEMBER_NAME);
    teamMember.setRole(TEAMMEMBER_ROLE);
    software.setTeamMembers(Arrays.asList(teamMember));
    software.setTechnicalDebt(TECHNICAL_DEBT);
    software.setTechnologies(TECHNOLOGIES);
    software.setVcs(VCS);

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
    Assert.assertEquals(1, softwareInfo.getTeammembers().size());
    Assert.assertEquals(TEAMMEMBER_NAME, softwareInfo.getTeammembers().get(0).getUser());
    Assert.assertEquals(TEAMMEMBER_ROLE, softwareInfo.getTeammembers().get(0).getRole());
    Assert.assertEquals(TECHNICAL_DEBT, softwareInfo.getTechnicalDebt());
    Assert.assertEquals(2, softwareInfo.getTechnologies().size());
    Assert.assertEquals(TECHNOLOGY1, softwareInfo.getTechnologies().get(0));
    Assert.assertEquals(TECHNOLOGY2, softwareInfo.getTechnologies().get(1));
    Assert.assertEquals (VCS, softwareInfo.getVcs());



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
