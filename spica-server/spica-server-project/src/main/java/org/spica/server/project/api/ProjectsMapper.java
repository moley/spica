package org.spica.server.project.api;

import org.spica.server.project.domain.Project;
import org.spica.server.project.model.ProjectInfo;

public class ProjectsMapper {

  ProjectInfo toApi (Project project) {
    ProjectInfo projectInfo = new ProjectInfo();
    projectInfo.setId(project.getId());

    return projectInfo;
  }
}
