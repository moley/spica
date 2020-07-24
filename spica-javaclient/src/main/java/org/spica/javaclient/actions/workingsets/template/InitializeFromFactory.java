package org.spica.javaclient.actions.workingsets.template;

import java.io.File;

public class InitializeFromFactory {

  public InitializeFrom create (String url) {
    if (url.startsWith("http"))
      return new InitializeFromStash();
    else {
      File localFile = new File (url);
      if (localFile.exists()) {
        return new InitializeFromLocalFileSystem();
      }
      else
        throw new IllegalStateException("Initialize a project is only supported by applying an stash url or an existing absolute path from local filesystem");

    }

  }
}
