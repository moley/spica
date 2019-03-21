package org.spica.server.project.service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import io.atlassian.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.project.domain.Topic;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;

public class JiraTopicImporter implements TopicImporter {

    public final static String EXTERNAL_SYSTEM_KEY_JIRA = "JIRA";

    private final static Logger LOGGER = LoggerFactory.getLogger(JiraTopicImporter.class);

    @Override
    public List<Topic> importTopicsOfUser(Long userID) throws InterruptedException {

        AsynchronousJiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();
        JiraRestClient jiraRestClient = jiraRestClientFactory.create(URI.create("https://jira.intra.vsa.de"), new BasicHttpAuthenticationHandler("OleyMa", "Momopomo351977"));

        jiraRestClient.getSearchClient().searchJql("assignee = currentUser() AND resolution = Unresolved").done(new Consumer<SearchResult>() {
            @Override
            public void accept(SearchResult searchResult) {
                for (Issue next: searchResult.getIssues()) {
                    LOGGER.error("Found my issue " + next.getKey() + "-" + next.getSummary() + "-" + next.getStatus().getName());
                }
            }
        });

        Thread.sleep(10000);

        return null;
    }

    public final static void main (String [] args) throws InterruptedException {
        JiraTopicImporter jiraTopicImporter = new JiraTopicImporter();
        jiraTopicImporter.importTopicsOfUser(1L);
    }
}
