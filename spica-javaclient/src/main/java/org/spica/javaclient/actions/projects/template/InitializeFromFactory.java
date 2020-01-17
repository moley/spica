package org.spica.javaclient.actions.projects.template;

public class InitializeFromFactory {

  public InitializeFrom create (String url) {
    if (url.startsWith("http"))
      return new InitializeFromStash();
    else
      throw new IllegalStateException("Initialize a project is only supported by applying an stash url yet");
  }
}
