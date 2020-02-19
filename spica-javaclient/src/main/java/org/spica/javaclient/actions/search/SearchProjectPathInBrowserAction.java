package org.spica.javaclient.actions.search;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class SearchProjectPathInBrowserAction extends AbstractAction {

  private final static Logger LOGGER = LoggerFactory.getLogger(SearchProjectPathInBrowserAction.class);

  @Override public String getDisplayname() {
    return "Goto project path in browser";
  }

  @Override public String getDescription() {
    return "Navigate to current project path in browser";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

    try {
      String projectUrl = "file://" + actionContext.getCurrentWorkingDir().getAbsolutePath();
      outputDefault("Go to " + projectUrl);


      Desktop.getDesktop().browse(URI.create(projectUrl));

    } catch (IOException e) {
      LOGGER.error(e.getLocalizedMessage(), e);
    }

    return null;

  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.SEARCH;
  }

  @Override public Command getCommand() {
    return new Command("browser", "b");
  }
}