package org.spica.server.project.api;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.server.commons.ExternalSystem;
import org.spica.server.project.domain.Topic;
import org.spica.server.project.domain.TopicRepository;
import org.spica.server.project.model.TopicContainerInfo;
import org.spica.server.project.service.JiraConfiguration;
import org.spica.server.project.service.JiraTopicAdapter;
import org.spica.server.project.service.TopicImporter;
import org.spica.server.project.service.TopicImporterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class TopicsController implements TopicsApi {

    TopicsMapper topicsMapper = new TopicsMapper();

    private final static Logger LOGGER = LoggerFactory.getLogger(TopicsController.class);

    @Autowired
    private TopicImporterStrategy topicImporterStrategy;

    @Autowired
    private TopicRepository topicRepository;

    public TopicsController() {
        System.out.println("");
    }


    @Override
    public ResponseEntity<Void> deleteTopic(@NotNull @Valid String userId, String topicId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> finishTopic(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "userId", required = true) String userId, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "topicId", required = true) String topicId) {
        SpicaProperties properties = new SpicaProperties();

        JiraTopicAdapter adapter = new JiraTopicAdapter();
        String jiraUrl = properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_URL);
        String jiraUser = properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_USER);
        String jiraPwd = properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_PWD);
        String closedStatus = properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_STATUS_CLOSED);
        LOGGER.info("Using jira " + jiraUrl + " with user " + jiraUser);

        LOGGER.info("Set status " + closedStatus + " on jira " + topicId);

        ExternalSystem externalSystem = new ExternalSystem(jiraUrl, jiraUser, jiraPwd);
        adapter.setStatus(externalSystem, topicId, Integer.valueOf(closedStatus).intValue());

        Topic finishedTopic = topicRepository.findByCurrentUserIDAndExternalSystemKey(userId, topicId);
        topicRepository.delete(finishedTopic);


        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TopicContainerInfo> importTopics(@ApiParam(value = "", required = true) @PathVariable("userId") String userId, @ApiParam(value = "", required = true) @PathVariable("usernameExternalSystem") String usernameExternalSystem, @ApiParam(value = "", required = true) @PathVariable("passwordExternalSystem") String passwordExternalSystem) {
        SpicaProperties properties = new SpicaProperties();
        TopicImporter topicImporter = topicImporterStrategy.create(properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_IMPORT_IMPLEMENTATION));

        List<Topic> importedTopics = null;
        try {
            importedTopics = topicImporter.importTopicsOfUser(userId, usernameExternalSystem, passwordExternalSystem);
        } catch (InterruptedException e) {
            LOGGER.error("Error importing topics: " + e.getLocalizedMessage(), e);
        }

        TopicContainerInfo topicContainerInfo = topicsMapper.toApi(importedTopics);
        return ResponseEntity.ok(topicContainerInfo);
    }


}
