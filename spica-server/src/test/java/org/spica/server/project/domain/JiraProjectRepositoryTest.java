package org.spica.server.project.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = {SpringExtension.class})
@DataJpaTest
@AutoConfigurationPackage
@ComponentScan("org.spica.server")
public class JiraProjectRepositoryTest {


  @Autowired
  ProjectRepository projectRepository;


  @Test
  public void findAll () {

    Project project = Project.builder().name("Testproject").build();
    projectRepository.save(project);

    Assert.assertEquals (1, projectRepository.findAll().size());
  }
}
