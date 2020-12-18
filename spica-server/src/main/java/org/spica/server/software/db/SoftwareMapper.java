package org.spica.server.software.db;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.spica.server.software.model.Software;

@Mapper
public interface SoftwareMapper {

  SoftwareMapper INSTANCE = Mappers.getMapper(SoftwareMapper.class);

  @Mapping ()
  Software softwareDbToSoftware (SoftwareDb softwareDb);
}
