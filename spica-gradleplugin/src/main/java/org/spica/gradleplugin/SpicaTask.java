package org.spica.gradleplugin;

import org.gradle.api.DefaultTask;
import org.spica.javaclient.StandaloneActionContext;

public class SpicaTask extends DefaultTask {

  private StandaloneActionContext context = new StandaloneActionContext();

  public StandaloneActionContext getContext() {
    return context;
  }
}
