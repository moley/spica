package org.spica.server.project.service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import io.atlassian.util.concurrent.Promise;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;
import org.spica.commons.services.jira.JiraConfiguration;
import org.spica.server.project.domain.Task;
import org.spica.server.project.domain.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class JiraTaskImporter implements TaskImporter {

    public final static String EXTERNAL_SYSTEM_KEY_JIRA = "JIRA";

    @Autowired
    private TaskRepository taskRepository;

    private AsynchronousJiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();

    @Override
    public List<Task> importTasksOfUser(String userID, final String user, final String pwd) throws InterruptedException {

        SpicaProperties spicaProperties = new SpicaProperties();
        String url = spicaProperties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_URL);

        log.info("Using jira url: " + url);
        log.info("Using jira user: " + user);
        log.info("Using jira pwd: " + pwd);

        //find already imported topics
        log.info("Find all topics of user " + userID + " with external system key " + EXTERNAL_SYSTEM_KEY_JIRA);
        final Collection<Task> existingTasks = taskRepository
            .findAllByCurrentUserIDAndExternalSystemID(userID, EXTERNAL_SYSTEM_KEY_JIRA);
        HashMap<String, Task> keyAndTopic = new HashMap<>();
        existingTasks.forEach(topic->keyAndTopic.put(topic.getExternalSystemKey(), topic));

        //TODO use JiraTopicAdapter at this point to avoid dependency to jira infrastructure at multiple locations
        JiraRestClient jiraRestClient = jiraRestClientFactory.create(URI.create(url), new BasicHttpAuthenticationHandler(user, pwd));

        log.info("Querying jira...");
        Promise<SearchResult> promise = jiraRestClient.getSearchClient().searchJql("assignee = currentUser() AND resolution = Unresolved").done(
            searchResult -> {
                log.info("Stepping issues");
                Collection<Task> importedTasks = importSearchResult(keyAndTopic, searchResult, userID);
                log.info("Finished importing topics from jira with " + importedTasks.size() + " items");
            });

        promise.claim();


        final List<Task> existingTopicsAfterImport = taskRepository
            .findAllByCurrentUserIDAndExternalSystemID(userID, EXTERNAL_SYSTEM_KEY_JIRA);
        log.info("Found existing topics after import: " + existingTopicsAfterImport + "(" + taskRepository.findAll().size() + ")");
        return existingTopicsAfterImport;
    }

    public Collection<Task> importSearchResult(final HashMap<String, Task> keyAndTopic, final SearchResult searchResult, final String userID) {
        for (Issue next: searchResult.getIssues()) {
            log.info("Found my issue " + next.getKey() + "-" + next.getSummary() + "-" + next.getStatus().getName());

            Task currentTask = keyAndTopic.get(next.getKey());
            if (currentTask == null) {
                currentTask = new Task();
                currentTask.setExternalSystemID(EXTERNAL_SYSTEM_KEY_JIRA);
                currentTask.setExternalSystemKey(next.getKey());
                currentTask.setCurrentUserID(userID);
                keyAndTopic.put(currentTask.getExternalSystemKey(), currentTask);
                //TODO currentTopic.setCreatorID();
            }

            currentTask.setName(next.getSummary());
            currentTask.setDescription(next.getDescription());

        }
        for (Map.Entry<String, Task> next: keyAndTopic.entrySet()) {
            log.info("After import: " + next.getKey() + "-" + next.getValue().getName());
        }
        taskRepository.saveAll(keyAndTopic.values());

        return keyAndTopic.values();

    }

    public static void main (String [] args) throws InterruptedException {

    }
}
