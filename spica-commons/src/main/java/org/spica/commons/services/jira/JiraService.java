package org.spica.commons.services.jira;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;

@Slf4j
public class JiraService {

  private AsynchronousJiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();

  public Jira connectToServer () {

    SpicaProperties spicaProperties = new SpicaProperties();

    String jiraUrlString = spicaProperties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_URL);
    String jiraUser = spicaProperties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_USER);
    String jiraPassword = spicaProperties.getValueNotNull(JiraConfiguration.PROPERTY_SPICA_JIRA_PWD);

    JiraRestClient jiraRestClient = jiraRestClientFactory.create(URI.create(jiraUrlString), new BasicHttpAuthenticationHandler(jiraUser, jiraPassword));
    Jira jira = new Jira(jiraRestClient);
    return jira;
  }

  public List<JiraProject> getProjects (final Jira jira) {
    JiraRestClient jiraRestClient = jira.getJiraRestClient();
    List<JiraProject> jiraProjects = new ArrayList<JiraProject>();
    for (BasicProject basicProject: jiraRestClient.getProjectClient().getAllProjects().claim()) {
      jiraProjects.add(new JiraProject(basicProject));
    }

    return jiraProjects;
  }

  public List<JiraComponent> getComponents (final Jira jira, final String projectKey) {
    JiraRestClient jiraRestClient = jira.getJiraRestClient();
    List<JiraProject> jiraProjects = new ArrayList<JiraProject>();
    List<JiraComponent> components = new ArrayList<>();
    Project project = jiraRestClient.getProjectClient().getProject(projectKey).claim();
    for (BasicComponent next: project.getComponents()) {
      components.add(new JiraComponent(next));
    }
    return components;
  }

  public JiraIssue createIssue (final Jira jira, final String summary, final String description, final String projectKey, final String componentName) {
    log.info("Create jira issue with summary " + summary + ", description " + description + ", projectKey " + projectKey + ", componentName " + componentName);
    JiraRestClient jiraRestClient = jira.getJiraRestClient();

    for (IssueType next: jiraRestClient.getMetadataClient().getIssueTypes().claim()) {
      log.info ("Next: " + next.getName());

    }

    //Project project = jiraRestClient.getProjectClient().getProject(projectKey).claim();
    //project.getIssueTypes()

    IssueInputBuilder issueInputBuilder = new IssueInputBuilder();
    issueInputBuilder.setDescription(description);
    issueInputBuilder.setSummary(summary);
    issueInputBuilder.setProjectKey(projectKey);
    issueInputBuilder.setComponentsNames(Arrays.asList(componentName));
    IssueInput issueInput = issueInputBuilder.build();

    return new JiraIssue(jiraRestClient.getIssueClient().createIssue(issueInput).claim());

  }

}
