package org.spica.commons.services.jira;

import com.atlassian.jira.rest.client.api.domain.BasicProject;

public class JiraProject {

  private BasicProject project;

  public JiraProject(final BasicProject project) {
    this.project = project;
  }

  public String getKey () {
    return project.getKey();
  }

  public String getName () {
    return project.getName();
  }

  public String toString () {
    return getKey() + " - " + getName();
  }
}
