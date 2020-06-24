package org.spica.commons.services.jira;

import com.atlassian.jira.rest.client.api.domain.BasicIssue;

public class JiraIssue {

  private BasicIssue basicIssue;

  public JiraIssue (final BasicIssue basicIssue) {
    this.basicIssue = basicIssue;
  }

  public String getKey () {
    return basicIssue.getKey();
  }
}
