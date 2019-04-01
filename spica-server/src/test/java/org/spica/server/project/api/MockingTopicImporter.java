package org.spica.server.project.api;

import org.spica.server.project.domain.Topic;
import org.spica.server.project.service.TopicImporter;

import java.util.ArrayList;
import java.util.List;

public class MockingTopicImporter implements TopicImporter {

    private static List<Topic> topicCollection = new ArrayList<Topic>();
    @Override
    public List<Topic> importTopicsOfUser(String userID, String user, String pwd) throws InterruptedException {
        if (topicCollection.isEmpty()) {
            Topic topic = Topic.builder().name("topic").externalSystemKey("external").description("description").id("id").build();
            topicCollection.add(topic);

        }
        return topicCollection;
    }
}
