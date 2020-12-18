package org.spica.server.software.db;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.mapstruct.factory.Mappers;
import org.spica.server.software.domain.SoftwarePersistencePort;
import org.spica.server.software.model.Software;
import org.spica.server.software.model.SoftwareAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class SoftwarePersistenceAdapter implements SoftwarePersistencePort {

  @Autowired
  private SoftwareRepository softwareRepository;

  private SoftwareMapper softwareMapper = Mappers.getMapper(SoftwareMapper.class);

  @Override public SoftwareAggregate findAll() {
    SoftwareAggregate softwareAggregate = new SoftwareAggregate();
    List<SoftwareDb> allSoftwareDbs = Lists.newArrayList(softwareRepository.findAll().iterator());
    for (SoftwareDb next: allSoftwareDbs) {
      softwareAggregate.allSoftware.add(softwareMapper.softwareDbToSoftware(next));
    }

    return null;
  }
}
