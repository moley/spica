package org.spica.server.project.api;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.project.domain.Topic;
import org.spica.server.project.model.TopicContainerInfo;
import org.spica.server.project.service.JiraTopicImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopicsController implements TopicsApi {

  @Autowired
  JiraTopicImporter jiraTopicImporter;

  TopicsMapper topicsMapper = new TopicsMapper();

  private final static Logger LOGGER = LoggerFactory.getLogger(TopicsController.class);

  public TopicsController () {
    System.out.println ("");
  }


  @Override
  public ResponseEntity<Void> deleteTopic(@ApiParam(value = "",required=true) @PathVariable("topicId") String topicId) {
    return null;
  }

  @Override
  public ResponseEntity<TopicContainerInfo> importTopics(@ApiParam(value = "",required=true) @PathVariable("userId") String userId,@ApiParam(value = "",required=true) @PathVariable("usernameExternalSystem") String usernameExternalSystem,@ApiParam(value = "",required=true) @PathVariable("passwordExternalSystem") String passwordExternalSystem) {


    List<Topic> importedTopics = null;
    try {
      importedTopics = jiraTopicImporter.importTopicsOfUser(userId, usernameExternalSystem, passwordExternalSystem);
    } catch (InterruptedException e) {
      LOGGER.error("Error importing topics: " + e.getLocalizedMessage(), e);
    }

    TopicContainerInfo topicContainerInfo = topicsMapper.toApi(importedTopics);
    return ResponseEntity.ok(topicContainerInfo);
  }


}
