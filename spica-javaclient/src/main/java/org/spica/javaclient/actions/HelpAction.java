package org.spica.javaclient.actions;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class HelpAction extends AbstractAction {

  private final static Logger LOGGER = LoggerFactory.getLogger(HelpAction.class);

  private ActionGroup group;



  @Override public String getDisplayname() {
    return "Show help";
  }

  @Override
  public String getDescription() {
    return "Show help for actiongroup " + group.name();
  }

  @Override
  public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

    for (String next: actionContext.getActionHandler().getHelp(group)) {
      outputDefault(next);
    }

    return null;
  }


  @Override
  public ActionGroup getGroup() {
    return group;
  }

  @Override
  public Command getCommand() {
    return new Command("help", "h");
  }

  public void setGroup(ActionGroup group) {
    this.group = group;
  }
}
