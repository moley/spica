package org.spica.commons.services.jenkins;

import com.offbytwo.jenkins.JenkinsServer;

public class Jenkins {
  private final JenkinsServer jenkinsServer;

  private final String username;

  public Jenkins (final JenkinsServer jenkinsServer,
      final String username) {
    this.jenkinsServer = jenkinsServer;
    this.username = username;
  }

  JenkinsServer getJenkinsServer() {
    return jenkinsServer;
  }


  public String getUsername() {
    return username;
  }

}
