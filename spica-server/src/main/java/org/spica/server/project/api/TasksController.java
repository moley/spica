package org.spica.server.project.api;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.commons.services.jira.JiraConfiguration;
import org.spica.server.commons.ExternalSystem;
import org.spica.server.project.domain.Task;
import org.spica.server.project.domain.TaskRepository;
import org.spica.server.project.model.TaskContainerInfo;
import org.spica.server.project.service.JiraTaskAdapter;
import org.spica.server.project.service.TaskImporter;
import org.spica.server.project.service.TaskImporterStrategy;
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
public class TasksController implements TasksApi {

    TasksMapper topicsMapper = new TasksMapper();

    private final static Logger LOGGER = LoggerFactory.getLogger(TasksController.class);

    @Autowired
    private TaskImporterStrategy taskImporterStrategy;

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public ResponseEntity<Void> deleteTask(@NotNull @Valid String userId, String topicId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> finishTask(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "userId", required = true) String userId, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "topicId", required = true) String topicId) {
        SpicaProperties properties = new SpicaProperties();

        JiraTaskAdapter adapter = new JiraTaskAdapter();
        String jiraUrl = properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_URL);
        String jiraUser = properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_USER);
        String jiraPwd = properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_PWD);
        String closedStatus = properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_STATUS_CLOSED);
        LOGGER.info("Using jira " + jiraUrl + " with user " + jiraUser);

        LOGGER.info("Set status " + closedStatus + " on jira " + topicId);

        ExternalSystem externalSystem = new ExternalSystem(jiraUrl, jiraUser, jiraPwd);
        adapter.setStatus(externalSystem, topicId, Integer.valueOf(closedStatus).intValue());

        //TODO do not delete finished tasks, but filter them
        Task finishedTask = taskRepository.findByCurrentUserIDAndExternalSystemKey(userId, topicId);
        taskRepository.delete(finishedTask);


        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TaskContainerInfo> importTasks(@ApiParam(value = "", required = true) @PathVariable("userId") String userId, @ApiParam(value = "", required = true) @PathVariable("usernameExternalSystem") String usernameExternalSystem, @ApiParam(value = "", required = true) @PathVariable("passwordExternalSystem") String passwordExternalSystem) {
        log.info("import Tasks called with params " + userId + " , " + usernameExternalSystem + ", password with length " + passwordExternalSystem.length());
        SpicaProperties properties = new SpicaProperties();
        TaskImporter taskImporter = taskImporterStrategy.create(properties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_IMPORT_IMPLEMENTATION));

        List<Task> importedTasks = null;
        try {
            importedTasks = taskImporter.importTasksOfUser(userId, usernameExternalSystem, passwordExternalSystem);
        } catch (InterruptedException e) {
            LOGGER.error("Error importing topics: " + e.getLocalizedMessage(), e);
        }

        TaskContainerInfo topicContainerInfo = topicsMapper.toApi(importedTasks);
        return ResponseEntity.ok(topicContainerInfo);
    }


}
