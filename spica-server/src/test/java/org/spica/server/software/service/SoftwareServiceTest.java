package org.spica.server.software.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spica.server.software.db.SoftwareRepository;
import org.spica.server.software.domain.Software;
import org.spica.server.software.model.SoftwareInfo;

public class SoftwareServiceTest {

  @Test
  public void setSoftware () {

    SoftwareRepository softwareRepository = Mockito.mock(SoftwareRepository.class);


    SoftwareInfo software1 = new SoftwareInfo();
    software1.setName("Software1");


    SoftwareInfo software11 = new SoftwareInfo();
    software11.setName("Software1 - Module1");

    SoftwareInfo software12 = new SoftwareInfo();
    software12.setName("Software1 - Module2");

    SoftwareService softwareService = new SoftwareService();
    softwareService.setSoftwareMapper(new SoftwareMapper());
    softwareService.setSoftwareRepository(softwareRepository);
    softwareService.setSoftwareList(Arrays.asList(software1));

    Mockito.verify(softwareRepository, Mockito.times(1)).deleteAll(Mockito.anyCollection());
    Mockito.verify(softwareRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());

  }

  @Test
  public void getSoftware () {
    Collection<Software> softwareInDatabase = new ArrayList<Software>();
    Software software1 = new Software();
    software1.setName("Software1");
    software1.setId(UUID.randomUUID().toString());


    Software software11 = new Software();
    software11.setParentId(software1.getId());
    software11.setName("Software1 - Module1");

    Software software12 = new Software();
    software12.setParentId(software1.getId());
    software12.setName("Software1 - Module2");

    softwareInDatabase.add(software1);
    SoftwareRepository softwareRepository = Mockito.mock(SoftwareRepository.class);
    Mockito.when(softwareRepository.findAll()).thenReturn(softwareInDatabase);

  }
}
