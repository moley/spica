package org.spica.server.software.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.spica.commons.KeyValue;
import org.spica.commons.SpicaProperties;
import org.spica.server.software.domain.Software;
import org.spica.server.software.domain.TeamMember;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.SoftwareInfo;
import org.spica.server.software.model.TeamMemberInfo;
import org.springframework.stereotype.Service;

@Service
public class SoftwareMapper {

  private SpicaProperties spicaProperties = new SpicaProperties();

  public Software toSoftwareEntity(final SoftwareInfo softwareInfo) {
    Software software = new Software();
    if (softwareInfo.getId() == null)
      throw new IllegalStateException("Software " + softwareInfo.toString() + " does not defined an ID");

    software.setId(softwareInfo.getId());
    software.setName(softwareInfo.getName());
    software.setDescription(softwareInfo.getDescription());
    software.setParentId(softwareInfo.getParentId());
    if (softwareInfo.getDeployment() != null)
      software.setDeployment(softwareInfo.getDeployment().getId());
    if (softwareInfo.getGroup() != null)
      software.setSoftwaregroup(softwareInfo.getGroup().getId());
    if (softwareInfo.getState() != null)
      software.setState(softwareInfo.getState().getId());
    if (softwareInfo.getType() != null)
      software.setType(softwareInfo.getType().getId());

    List<Software> children = new ArrayList<>();
    if (softwareInfo.getChildren() != null) {
      for (SoftwareInfo next : softwareInfo.getChildren()) {
        Software nextChildren = toSoftwareEntity(next);
        children.add(nextChildren);
      }
    }
    software.setChildren(children);
    software.setActive(softwareInfo.getActive());
    software.setComplexity(softwareInfo.getComplexity());
    software.setFormat(softwareInfo.getFormat());
    software.setLocation(softwareInfo.getLocation());
    software.setMaintainability(softwareInfo.getMaintainability());
    software.setNeedsAction(softwareInfo.getNeedsAction());
    software.setNeedsActionDescription(softwareInfo.getNeedsActionDescription());
    software.setRequirement(softwareInfo.getRequirement());
    List<TeamMember> teamMembers = new ArrayList<>();
    if (softwareInfo.getTeammembers() != null) {
      for (TeamMemberInfo next : softwareInfo.getTeammembers())
        teamMembers.add(toTeamMemberEntity(next));
    }
    software.setTeamMembers(teamMembers);
    software.setTechnicalDebt(softwareInfo.getTechnicalDebt());
    software.setTechnologies(softwareInfo.getTechnologies());
    software.setVcs(softwareInfo.getVcs());
    return software;

  }

  public TeamMember toTeamMemberEntity (final TeamMemberInfo teamMemberInfo) {
    TeamMember teamMember = new TeamMember();
    teamMember.setUser(teamMemberInfo.getUser());
    teamMember.setRole(teamMemberInfo.getRole());
    return teamMember;
  }

  public TeamMemberInfo toTeamMemberInfo (final TeamMember teamMember) {
    TeamMemberInfo teamMemberInfo = new TeamMemberInfo();
    teamMemberInfo.setUser(teamMember.getUser());
    teamMemberInfo.setRole(teamMember.getRole());
    return teamMemberInfo;
  }

  public SoftwareInfo toSoftwareInfo(final Software software) {
    if (software == null)
      throw new IllegalArgumentException("Argument software must not be null");

    SoftwareInfo softwareInfo = new SoftwareInfo();
    softwareInfo.setDescription(software.getDescription());
    softwareInfo.setName(software.getName());
    softwareInfo.setId(software.getId());
    softwareInfo.setParentId(software.getParentId());
    softwareInfo.setDeployment(toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(software.getDeployment())));
    softwareInfo.setGroup(toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(software.getSoftwaregroup())));
    softwareInfo.setState(toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(software.getState())));
    softwareInfo.setType(toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(software.getType())));
    softwareInfo.setDeployment(toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(software.getDeployment())));
    softwareInfo.setActive(software.getActive());
    softwareInfo.setComplexity(software.getComplexity());
    softwareInfo.setFormat(software.getFormat());
    softwareInfo.setLocation(software.getLocation());
    softwareInfo.setMaintainability(software.getMaintainability());
    softwareInfo.setNeedsAction(software.getNeedsAction());
    softwareInfo.setNeedsActionDescription(software.getNeedsActionDescription());
    softwareInfo.setRequirement(software.getRequirement());
    if (software.getTeamMembers() != null) {
      List<TeamMemberInfo> teamMemberInfoCollection = new ArrayList<>();
      for (TeamMember nextTeammember: software.getTeamMembers()) {
        teamMemberInfoCollection.add(toTeamMemberInfo(nextTeammember));
      }
      softwareInfo.setTeammembers(teamMemberInfoCollection);
    }
    softwareInfo.setTechnicalDebt(software.getTechnicalDebt());
    softwareInfo.setTechnologies(software.getTechnologies());
    softwareInfo.setVcs(software.getVcs());

    if (software.getChildren() != null) {
      for (Software children : software.getChildren()) {
        SoftwareInfo nextChildrenInfo = toSoftwareInfo(children);
        softwareInfo.addChildrenItem(nextChildrenInfo);
      }
    }
    return softwareInfo;
  }

  public List<IdAndDisplaynameInfo> toIdAndDisplaynameInfos (final List<KeyValue> keyValues) {
    List<IdAndDisplaynameInfo> infos = new ArrayList<>();
    for (KeyValue next: keyValues) {
      infos.add(toIdAndDisplaynameInfo(next));
    }
    return infos;
  }

  public IdAndDisplaynameInfo toIdAndDisplaynameInfo (KeyValue keyValues) {
    if (keyValues == null)
      return null;
    else
      return new IdAndDisplaynameInfo().id(keyValues.getKey()).displayname(keyValues.getValue());
  }
}
