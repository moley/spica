package org.spica.server.software.service;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.spica.server.software.db.SoftwareRepository;
import org.spica.server.software.domain.Software;
import org.spica.server.software.model.SoftwareInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class SoftwareService {

  @Autowired
  private SoftwareRepository softwareRepository;

  @Autowired
  private SoftwareMapper softwareMapper;

  public List<SoftwareInfo> getSoftware () {
    List<SoftwareInfo> infos = new ArrayList<>();
    for (Software next: softwareRepository.findAll()) {
      infos.add(softwareMapper.toSoftwareInfo(next));
    }

    return infos;
  }

  public void setSoftware (final List<SoftwareInfo> software) {
    softwareRepository.deleteAll();

    List<Software> savedSoftware = new ArrayList<>();
    for (SoftwareInfo next: software) {
      savedSoftware.add(softwareMapper.toSoftwareEntity(next));
    }

    softwareRepository.saveAll(savedSoftware);

  }
}
