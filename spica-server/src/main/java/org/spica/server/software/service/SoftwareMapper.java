package org.spica.server.software.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.spica.commons.KeyValue;
import org.spica.server.software.domain.Software;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.SoftwareInfo;
import org.springframework.stereotype.Service;

@Service
public class SoftwareMapper {

  public Software toSoftwareEntity(final SoftwareInfo softwareInfo, final Software parent) {
    Software software = new Software();

    software.setId(UUID.randomUUID().toString());
    software.setName(softwareInfo.getName());
    software.setDescription(softwareInfo.getDescription());
    if (parent != null)
      software.setParentId(parent.getId());

    List<Software> children = new ArrayList<>();
    if (softwareInfo.getChildren() != null) {
      for (SoftwareInfo next : softwareInfo.getChildren()) {
        Software nextChildren = toSoftwareEntity(next, software);
        children.add(nextChildren);
      }
    }
    software.setChildren(children);

    return software;

  }

  public SoftwareInfo toSoftwareInfo(final Software software) {
    SoftwareInfo softwareInfo = new SoftwareInfo();
    softwareInfo.setDescription(software.getDescription());
    softwareInfo.setName(software.getName());

    for (Software children: software.getChildren()) {
      SoftwareInfo nextChildrenInfo = toSoftwareInfo(children);
      softwareInfo.addChildrenItem(nextChildrenInfo);
    }
    return softwareInfo;
  }

  public List<IdAndDisplaynameInfo> toIdAndDisplaynameInfos (final List<KeyValue> keyValues) {
    List<IdAndDisplaynameInfo> infos = new ArrayList<>();
    for (KeyValue next: keyValues) {
      infos.add(new IdAndDisplaynameInfo().id(next.getKey()).displayname(next.getValue()));
    }
    return infos;
  }
}
