package org.spica.commons.services.jira;

import com.atlassian.jira.rest.client.api.domain.BasicComponent;

public class JiraComponent {

  private BasicComponent basicComponent;

  public JiraComponent(final BasicComponent basicComponent) {
    this.basicComponent = basicComponent;
  }

  public String getName () {
    return basicComponent.getName();
  }


  public String toString () {
    return getName();
  }
}
