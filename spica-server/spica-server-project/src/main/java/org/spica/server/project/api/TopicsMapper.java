package org.spica.server.project.api;

import org.spica.server.project.domain.Topic;
import org.spica.server.project.model.TopicInfo;

public class TopicsMapper {

  TopicInfo toApi (Topic project) {
    TopicInfo topicInfo = new TopicInfo();
    topicInfo.setId(Long.toString(project.getId()));

    return topicInfo;
  }
}
