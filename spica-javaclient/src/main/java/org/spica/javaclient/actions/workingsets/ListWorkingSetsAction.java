package org.spica.javaclient.actions.workingsets;

import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class ListWorkingSetsAction extends AbstractWorkingSetAction {

  @Override public String getDisplayname() {
    return "List workingsets";
  }

  @Override
  public String getDescription() {
    return "List all workingsets";
  }

  @Override
  public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
    outputDefault("Workingsets:\n\n");
    Model model = actionContext.getModel();
    for (WorkingSetInfo next: model.getWorkingsetInfos()) {
      String topicToken = String.format("     %-40s (%s)", next.getName(), next.getId());

      if (commandLineArguments.noArgumentOr(next.getId(), next.getName()));
      outputDefault(topicToken);
    }

    return null;
  }


  @Override
  public Command getCommand() {
    return new Command ("list", "l");
  }
}
