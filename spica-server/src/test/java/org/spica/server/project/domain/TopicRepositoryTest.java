package org.spica.server.project.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.project.service.JiraTopicImporter;
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
        Topic topic1 = Topic.builder().currentUserID("1").externalSystemKey(JiraTopicImporter.EXTERNAL_SYSTEM_KEY_JIRA).build();
        Topic topic2 = Topic.builder().currentUserID("2").externalSystemKey(JiraTopicImporter.EXTERNAL_SYSTEM_KEY_JIRA).build();
        Topic topic3 = Topic.builder().currentUserID("3").externalSystemKey("NERD").build();

        topicRepository.saveAll(Arrays.asList(topic1, topic2, topic3));

        for (Topic next: topicRepository.findAll()) {
            LOGGER.info("Found " + next.getCurrentUserID() + "-" + next.getExternalSystemKey());
        }
        Assert.assertEquals (1, topicRepository.findAllByCurrentUserIDAndExternalSystemKey("1", JiraTopicImporter.EXTERNAL_SYSTEM_KEY_JIRA).size());
        Assert.assertEquals (1, topicRepository.findAllByCurrentUserIDAndExternalSystemKey("2", JiraTopicImporter.EXTERNAL_SYSTEM_KEY_JIRA).size());
        Assert.assertEquals (0, topicRepository.findAllByCurrentUserIDAndExternalSystemKey("3", JiraTopicImporter.EXTERNAL_SYSTEM_KEY_JIRA).size());
    }
}
