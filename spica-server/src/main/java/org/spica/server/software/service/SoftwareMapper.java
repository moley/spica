package org.spica.server.software.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.spica.commons.DateUtil;
import org.spica.commons.KeyValue;
import org.spica.commons.SpicaProperties;
import org.spica.server.software.domain.Contact;
import org.spica.server.software.domain.Relation;
import org.spica.server.software.domain.Software;
import org.spica.server.software.model.ContactInfo;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.RelationInfo;
import org.spica.server.software.model.SoftwareInfo;
import org.springframework.stereotype.Service;

@Service
public class SoftwareMapper {

  private SpicaProperties spicaProperties = new SpicaProperties();

  private DateUtil dateUtil = new DateUtil();

  public Software toSoftwareEntity(final SoftwareInfo softwareInfo) {
    if (softwareInfo == null)
      throw new IllegalArgumentException("Parameter softwareInfo must not be null");

    Software software = new Software();
    software.setId(softwareInfo.getId() != null ? softwareInfo.getId() : UUID.randomUUID().toString());

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
    software.setBugtracking(softwareInfo.getBugtracking());
    software.setBuildsystem(softwareInfo.getBuildsystem());
    if (softwareInfo.getContactsTeam() != null) {
      List<Contact> contactsTeam = new ArrayList<>();

      for (ContactInfo next : softwareInfo.getContactsTeam())
        contactsTeam.add(toContactEntity(next));
      software.setContactsTeam(contactsTeam);
    }

    if (softwareInfo.getContactsUser() != null) {
      List<Contact> contactsUser = new ArrayList<>();

      for (ContactInfo next : softwareInfo.getContactsUser())
        contactsUser.add(toContactEntity(next));
      software.setContactsUser(contactsUser);
    }

    software.setTechnicalDebt(softwareInfo.getTechnicalDebt());
    List<String> technologies = new ArrayList<>();
    if (softwareInfo.getTechnologies() != null) {
      for (IdAndDisplaynameInfo next: softwareInfo.getTechnologies()) {
        technologies.add(next.getId());
      }
      software.setTechnologies(technologies);
    }
    software.setVcs(softwareInfo.getVcs());
    software.setFitsArchitecture(softwareInfo.getFitsArchitecture());
    software.setArchitectureExceptions(softwareInfo.getArchitectureExceptions());
    software.setChangeFrequency(softwareInfo.getChangeFrequency());
    if (softwareInfo.getTargetDate() != null)
      software.setTargetDate(dateUtil.getDate(softwareInfo.getTargetDate()));

    software.setWithOnlineHelp(softwareInfo.getWithOnlineHelp());
    software.setWithPersistence(softwareInfo.getWithPersistence());
    software.setWithUi(softwareInfo.getWithUi());
    software.setWithMonitoring(softwareInfo.getWithMonitoring());
    software.setWithSecurity(softwareInfo.getWithSecurity());
    software.setDeploymentName(softwareInfo.getDeploymentName());


    return software;

  }



  public SoftwareInfo toSoftwareInfo(final Software software) {
    if (software == null)
      throw new IllegalArgumentException("Argument software must not be null");

    SoftwareInfo softwareInfo = new SoftwareInfo();
    softwareInfo.setDescription(software.getDescription());
    softwareInfo.setName(software.getName());
    softwareInfo.setId(software.getId() != null ? software.getId() : UUID.randomUUID().toString());
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
    softwareInfo.setBuildsystem(software.getBuildsystem());
    softwareInfo.setBugtracking(software.getBugtracking());
    if (software.getContactsUser() != null) {
      List<ContactInfo> contactsUser = new ArrayList<>();
      for (Contact nextTeammember: software.getContactsUser()) {
        contactsUser.add(toContactInfo(nextTeammember));
      }
      softwareInfo.setContactsUser(contactsUser);
    }

    if (software.getContactsTeam() != null) {
      List<ContactInfo> contactsTeams = new ArrayList<>();
      for (Contact nextTeammember: software.getContactsTeam()) {
        contactsTeams.add(toContactInfo(nextTeammember));
      }
      softwareInfo.setContactsTeam(contactsTeams);
    }
    softwareInfo.setTechnicalDebt(software.getTechnicalDebt());
    if (software.getTechnologies() != null) {
      List<IdAndDisplaynameInfo> technologyInfos = new ArrayList<>();
      for (String next: software.getTechnologies()) {
        technologyInfos.add(new IdAndDisplaynameInfo().displayname(next).id(next));
      }
      softwareInfo.setTechnologies(technologyInfos);

    }
    softwareInfo.setVcs(software.getVcs());

    if (software.getChildren() != null) {
      for (Software children : software.getChildren()) {
        SoftwareInfo nextChildrenInfo = toSoftwareInfo(children);
        softwareInfo.addChildrenItem(nextChildrenInfo);
      }
    }

    softwareInfo.setFitsArchitecture(software.getFitsArchitecture());
    softwareInfo.setArchitectureExceptions(software.getArchitectureExceptions());
    softwareInfo.setChangeFrequency(software.getChangeFrequency());
    if (software.getTargetDate() != null)
      softwareInfo.setTargetDate(dateUtil.getDateAsString(software.getTargetDate()));
    softwareInfo.setWithOnlineHelp(software.getWithOnlineHelp());
    softwareInfo.setWithPersistence(software.getWithPersistence());
    softwareInfo.setWithUi(software.getWithUi());
    softwareInfo.setWithMonitoring(software.getWithMonitoring());
    softwareInfo.setWithSecurity(software.getWithSecurity());
    softwareInfo.setDeploymentName(software.getDeploymentName());
    return softwareInfo;
  }

  public Contact toContactEntity(final ContactInfo contactInfo) {
    Contact contact = new Contact();
    contact.setId(contactInfo.getId() != null ? contactInfo.getId() : UUID.randomUUID().toString());
    contact.setContactId(contactInfo.getContactId());
    contact.setRole(contactInfo.getRole() != null ? contactInfo.getRole().getId() : null);
    return contact;
  }

  public ContactInfo toContactInfo (final Contact contact) {
    ContactInfo teamMemberInfo = new ContactInfo();
    teamMemberInfo.setId(contact.getId() != null ? contact.getId(): UUID.randomUUID().toString());
    teamMemberInfo.setContactId(contact.getContactId());
    teamMemberInfo.setRole(toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(contact.getRole())));
    return teamMemberInfo;
  }

  public Relation toRelationEntity (final RelationInfo relationInfo) {
    Relation relation = new Relation();
    relation.setId(relationInfo.getId() != null ? relationInfo.getId() : UUID.randomUUID().toString());

    relation.setSource(toSoftwareEntity(relationInfo.getSource()));
    relation.setTarget(toSoftwareEntity(relationInfo.getTarget()));
    if (relationInfo.getState() != null)
      relation.setState(relationInfo.getState().getId());

    return relation;
  }

  public RelationInfo toRelationInfo (final Relation relation) {
    RelationInfo relationInfo = new RelationInfo();
    relationInfo.setId(relation.getId() != null ? relation.getId(): UUID.randomUUID().toString());
    relationInfo.setSource(toSoftwareInfo(relation.getSource()));
    relationInfo.setTarget(toSoftwareInfo(relation.getTarget()));
    relationInfo.setState(toIdAndDisplaynameInfo(spicaProperties.getKeyValuePair(relation.getState())));

    return relationInfo;
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
