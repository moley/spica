package org.spica.server.project.api;

import io.swagger.annotations.ApiParam;
import org.spica.server.project.domain.Project;
import org.spica.server.project.domain.ProjectRepository;
import org.spica.server.project.domain.Topic;
import org.spica.server.project.domain.TopicRepository;
import org.spica.server.project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
public class ProjectsController implements ProjectsApi {

  @Autowired
  private ProjectRepository projectRepository;

  private ProjectsMapper projectsMapper = new ProjectsMapper();

  @Autowired
  private TopicRepository topicRepository;

  private TopicsMapper topicsMapper = new TopicsMapper();

  @Override
  public ResponseEntity<ProjectInfo> createProject(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "name", required = true) String name) {
    Project project = new Project();
    projectRepository.save(project);
    return new ResponseEntity<ProjectInfo>(projectsMapper.toApi(project), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<TopicInfo> createTopic(@ApiParam(value = "",required=true) @PathVariable("projectId") String projectId, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "name", required = true) String name) {


    Optional<Project> project = projectRepository.findById(projectId);
    if (! project.isPresent())
      throw new IllegalStateException("Project with id " + projectId + " not found");

    Topic topic = new Topic();
    topic.setName(name);
    topic.setProjectID(projectId);
    topicRepository.save(topic);
    return new ResponseEntity<TopicInfo>(topicsMapper.toApi(topic), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ProjectContainerInfo> getProjects() {

    List<Project> projects = projectRepository.findAll();

    ProjectContainerInfo containerInfo = new ProjectContainerInfo();
    for (Project next: projects) {
      containerInfo.addProjectsItem(projectsMapper.toApi(next));
    }

    return new ResponseEntity<ProjectContainerInfo>(containerInfo, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<TopicContainerInfo> getTopics(@ApiParam(value = "",required=true) @PathVariable("projectId") String projectId) {

    List<Topic> topicsByProject = topicRepository.findAllByProjectID(projectId);
    TopicContainerInfo topicContainerInfo = new TopicContainerInfo();
    for (Topic next: topicsByProject) {
      topicContainerInfo.addTopicsItem(topicsMapper.toApi(next));
    }
    return new ResponseEntity<TopicContainerInfo>(topicContainerInfo, HttpStatus.OK);
  }
}
