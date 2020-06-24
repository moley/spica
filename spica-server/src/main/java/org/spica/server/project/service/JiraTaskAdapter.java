package org.spica.server.project.service;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import io.atlassian.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.commons.ExternalSystem;
import org.spica.server.project.model.TaskInfo;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Adapter which provides an abstraction to access and change topics in external system
 */
@Service
public class JiraTaskAdapter implements ExternalSystemTaskAdapter {

    public final static String EXTERNAL_SYSTEM_KEY_JIRA = "JIRA";


    private final static Logger LOGGER = LoggerFactory.getLogger(JiraTaskImporter.class);


    private AsynchronousJiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();

    @Override
    public Collection<TaskInfo> getTasksOfUser(ExternalSystem externalSystem) {
        JiraRestClient jiraRestClient = getRestClient(externalSystem);
        Collection<TaskInfo> topicsFromJira = new ArrayList<TaskInfo>();

        Promise<SearchResult> promise = jiraRestClient.getSearchClient().searchJql("assignee = currentUser() AND resolution = Unresolved").done(new Consumer<SearchResult>() {
            @Override
            public void accept(SearchResult searchResult) {
                for (Issue next: searchResult.getIssues()) {
                    LOGGER.info("Found my issue " + next.getKey() + "-" + next.getSummary() + "-" + next.getStatus().getName());

                    TaskInfo topicInfo = new TaskInfo();
                    topicInfo.setExternalSystemID(EXTERNAL_SYSTEM_KEY_JIRA);
                    topicInfo.setExternalSystemKey(next.getKey());
                    topicInfo.setName(next.getSummary());
                    topicInfo.setDescription(next.getDescription());
                    topicsFromJira.add(topicInfo);
                }
            }

        });

        promise.claim();


        return topicsFromJira;
    }

    private JiraRestClient getRestClient (ExternalSystem externalSystem) {
        return jiraRestClientFactory.create(URI.create(externalSystem.getUrl()), new BasicHttpAuthenticationHandler(externalSystem.getUser(), externalSystem.getPassword()));
    }


    public String getStatus(ExternalSystem externalSystem, String key) {
        JiraRestClient restClient = getRestClient(externalSystem);
        IssueRestClient issueRestClient = restClient.getIssueClient();
        Promise<Issue> issuePromise = issueRestClient.getIssue(key);
        Issue issue = issuePromise.claim();
        System.out.println ("Status: " + issue.getStatus().getName() + "-" + issue.getStatus().getId());
        Promise<Iterable<Transition>> ptransitions = issueRestClient.getTransitions(issue);
        Iterable<Transition> transitions = ptransitions.claim();

        for(Transition t: transitions){
            System.out.println("-" + t.getName() + ":" + t.getId() + "-" + t.toString());
        }
        System.out.println("Current state; " + issue.getStatus().getName());
        return issue.getStatus().getId().toString();

    }

    public void setStatus (ExternalSystem externalSystem, String key, int id) {
        JiraRestClient restClient = getRestClient(externalSystem);
        IssueRestClient issueRestClient = restClient.getIssueClient();
        Promise<Issue> issuePromise = issueRestClient.getIssue(key);
        Issue issue = issuePromise.claim();
        issueRestClient.transition(issue, new TransitionInput(id));
    }
}
