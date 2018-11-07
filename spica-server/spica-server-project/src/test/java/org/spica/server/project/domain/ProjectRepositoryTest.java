package org.spica.server.project.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = {SpringExtension.class})
@DataJpaTest
@EnableJpaRepositories(basePackages = "org.spica.server.project.domain")
@EntityScan(basePackages = "org.spica.server.project.domain")
public class ProjectRepositoryTest {


  @Autowired
  ProjectRepository projectRepository;


  @Test
  public void createProject () {

    Project project = Project.builder().name("Testproject").build();
    projectRepository.save(project);

    Assert.assertEquals (1, projectRepository.findAll().size());
  }
}
