package org.spica.server.project.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.project.service.JiraTaskAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

@ExtendWith(value = {SpringExtension.class})
@DataJpaTest
@AutoConfigurationPackage
@ComponentScan("org.spica.server")
public class TaskRepositoryTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskRepositoryTest.class);

    @Autowired TaskRepository taskRepository;

    @Test
    public void findAllByCurrentUserIDAndExternalSystemKey () {
        Task task1 = Task.builder().currentUserID("1").externalSystemID(JiraTaskAdapter.EXTERNAL_SYSTEM_KEY_JIRA).build();
        Task task2 = Task.builder().currentUserID("2").externalSystemID(JiraTaskAdapter.EXTERNAL_SYSTEM_KEY_JIRA).build();
        Task task3 = Task.builder().currentUserID("3").externalSystemID("NERD").build();

        taskRepository.saveAll(Arrays.asList(task1, task2, task3));

        for (Task next: taskRepository.findAll()) {
            LOGGER.info("Found " + next.getCurrentUserID() + "-" + next.getExternalSystemKey());
        }
        Assert.assertEquals (1, taskRepository.findAllByCurrentUserIDAndExternalSystemID("1", JiraTaskAdapter.EXTERNAL_SYSTEM_KEY_JIRA).size());
        Assert.assertEquals (1, taskRepository.findAllByCurrentUserIDAndExternalSystemID("2", JiraTaskAdapter.EXTERNAL_SYSTEM_KEY_JIRA).size());
        Assert.assertEquals (0, taskRepository.findAllByCurrentUserIDAndExternalSystemID("3", JiraTaskAdapter.EXTERNAL_SYSTEM_KEY_JIRA).size());
    }
}
