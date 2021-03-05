package org.spica.server.software.service;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.spica.server.software.db.SoftwareRepository;
import org.spica.server.software.domain.Software;
import org.spica.server.software.model.SoftwareInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
public class SoftwareService {

  @Autowired
  private SoftwareRepository softwareRepository;

  @Autowired
  private SoftwareMapper softwareMapper;

  public List<SoftwareInfo> getSoftwareList() {
    List<SoftwareInfo> infos = new ArrayList<>();
    for (Software next: softwareRepository.findByParentIdIsNull()) {
      infos.add(softwareMapper.toSoftwareInfo(next));
    }

    log.info("get software: \n" + infos);
    return infos;
  }

  public SoftwareInfo getSoftwareById (final String id) {
    Software software = softwareRepository.findById(id);
    return softwareMapper.toSoftwareInfo(software);
  }

  public void setSoftwareList(final List<SoftwareInfo> software) {
    softwareRepository.deleteAll();

    List<Software> savedSoftware = new ArrayList<>();
    for (SoftwareInfo next: software) {
      savedSoftware.add(softwareMapper.toSoftwareEntity(next));
    }

    softwareRepository.saveAll(savedSoftware);

  }

  public void updateSoftware (final String id, final SoftwareInfo updatedSoftwareInfo) {
    Software updatedSoftware = softwareMapper.toSoftwareEntity(updatedSoftwareInfo);
    softwareRepository.save(updatedSoftware);

  }
}
