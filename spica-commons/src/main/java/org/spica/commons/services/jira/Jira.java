package org.spica.commons.services.jira;

import com.atlassian.jira.rest.client.api.JiraRestClient;

public class Jira {

  private JiraRestClient jiraRestClient;

  public Jira (final JiraRestClient restClient) {
    this.jiraRestClient = restClient;
  }

  JiraRestClient getJiraRestClient() {
    return jiraRestClient;
  }

}
