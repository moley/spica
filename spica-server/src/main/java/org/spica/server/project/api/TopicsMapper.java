package org.spica.server.project.api;

import org.spica.server.project.domain.Topic;
import org.spica.server.project.model.TopicContainerInfo;
import org.spica.server.project.model.TopicInfo;

import java.util.ArrayList;
import java.util.List;

public class TopicsMapper {

  TopicInfo toApi (Topic topic) {
    TopicInfo topicInfo = new TopicInfo();
    topicInfo.setId(topic.getId());
    topicInfo.setName(topic.getName());

    return topicInfo;
  }

  TopicContainerInfo toApi (List<Topic> topics) {
    TopicContainerInfo topicContainerInfo = new TopicContainerInfo();
    topicContainerInfo.setTopics(new ArrayList<TopicInfo>());
    for (Topic next: topics) {
      topicContainerInfo.addTopicsItem(toApi(next));
    }


    return topicContainerInfo;
  }
}
