package org.spica.server.project.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.project.service.JiraTopicAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

@ExtendWith(value = {SpringExtension.class})
@DataJpaTest
@AutoConfigurationPackage
public class TopicRepositoryTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(TopicRepositoryTest.class);

    @Autowired
    TopicRepository topicRepository;

    @Test
    public void findAllByCurrentUserIDAndExternalSystemKey () {
        Topic topic1 = Topic.builder().currentUserID("1").externalSystemID(JiraTopicAdapter.EXTERNAL_SYSTEM_KEY_JIRA).build();
        Topic topic2 = Topic.builder().currentUserID("2").externalSystemID(JiraTopicAdapter.EXTERNAL_SYSTEM_KEY_JIRA).build();
        Topic topic3 = Topic.builder().currentUserID("3").externalSystemID("NERD").build();

        topicRepository.saveAll(Arrays.asList(topic1, topic2, topic3));

        for (Topic next: topicRepository.findAll()) {
            LOGGER.info("Found " + next.getCurrentUserID() + "-" + next.getExternalSystemKey());
        }
        Assert.assertEquals (1, topicRepository.findAllByCurrentUserIDAndExternalSystemID("1", JiraTopicAdapter.EXTERNAL_SYSTEM_KEY_JIRA).size());
        Assert.assertEquals (1, topicRepository.findAllByCurrentUserIDAndExternalSystemID("2", JiraTopicAdapter.EXTERNAL_SYSTEM_KEY_JIRA).size());
        Assert.assertEquals (0, topicRepository.findAllByCurrentUserIDAndExternalSystemID("3", JiraTopicAdapter.EXTERNAL_SYSTEM_KEY_JIRA).size());
    }
}
