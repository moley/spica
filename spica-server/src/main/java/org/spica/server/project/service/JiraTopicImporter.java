package org.spica.server.project.service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.server.project.domain.Topic;
import org.spica.server.project.domain.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Data
public class JiraTopicImporter implements TopicImporter {

    public final static String EXTERNAL_SYSTEM_KEY_JIRA = "JIRA";

    private final static Logger LOGGER = LoggerFactory.getLogger(JiraTopicImporter.class);

    @Autowired
    private TopicRepository topicRepository;

    private AsynchronousJiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();

    @Override
    public List<Topic> importTopicsOfUser(String userID, final String user, final String pwd) throws InterruptedException {

        SpicaProperties spicaProperties = new SpicaProperties();
        String url = spicaProperties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_URL);

        LOGGER.info("Using jira url: " + url);
        LOGGER.info("Using jira user: " + user);
        LOGGER.info("Using jira pwd: " + pwd);

        //find already imported topics
        LOGGER.info("Find all topics of user " + userID + " with external system key " + EXTERNAL_SYSTEM_KEY_JIRA);
        List<Topic> importedTopics = topicRepository.findAllByCurrentUserIDAndExternalSystemKey(userID, EXTERNAL_SYSTEM_KEY_JIRA);
        HashMap<String, Topic> keyAndTopic = new HashMap<String, Topic>();
        importedTopics.forEach(topic->keyAndTopic.put(topic.getExternalSystemKey(), topic));

        JiraRestClient jiraRestClient = jiraRestClientFactory.create(URI.create(url), new BasicHttpAuthenticationHandler(user, pwd));

        LOGGER.info("Querying jira...");
        jiraRestClient.getSearchClient().searchJql("assignee = currentUser() AND resolution = Unresolved").done(new Consumer<SearchResult>() {
            @Override
            public void accept(SearchResult searchResult) {
                LOGGER.info("Stepping issues");
                importSearchResult(keyAndTopic, searchResult);
                LOGGER.info("Finished importing topics from jira");
            }
        });

        Thread.sleep(1000);


        return importedTopics;
    }

    public Collection<Topic> importSearchResult(final HashMap<String, Topic> keyAndTopic, final SearchResult searchResult) {
        for (Issue next: searchResult.getIssues()) {
            LOGGER.info("Found my issue " + next.getKey() + "-" + next.getSummary() + "-" + next.getStatus().getName());

            Topic currentTopic = keyAndTopic.get(next.getKey());
            if (currentTopic == null) {
                currentTopic = new Topic();
                currentTopic.setExternalSystemID(EXTERNAL_SYSTEM_KEY_JIRA);
                currentTopic.setExternalSystemKey(next.getKey());
                keyAndTopic.put(currentTopic.getExternalSystemKey(), currentTopic);
                //TODO currentTopic.setCreatorID();
            }

            currentTopic.setName(next.getSummary());
            currentTopic.setDescription(next.getDescription());

        }
        for (Map.Entry<String, Topic> next: keyAndTopic.entrySet()) {
            LOGGER.info("After import: " + next.getKey() + "-" + next.getValue().getName());
        }
        topicRepository.saveAll(keyAndTopic.values());

        return keyAndTopic.values();

    }

    public final static void main (String [] args) throws InterruptedException {

    }
}
