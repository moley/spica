package org.spica.server.software.service;

import java.util.ArrayList;
import java.util.List;
import org.spica.commons.KeyValue;
import org.spica.commons.SpicaProperties;
import org.spica.server.software.domain.Software;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.SoftwareInfo;
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

    return software;

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


    for (Software children: software.getChildren()) {
      SoftwareInfo nextChildrenInfo = toSoftwareInfo(children);
      softwareInfo.addChildrenItem(nextChildrenInfo);
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
