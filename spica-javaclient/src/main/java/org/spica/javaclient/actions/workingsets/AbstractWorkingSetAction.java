package org.spica.javaclient.actions.workingsets;

import java.util.List;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.params.CommandLineArguments;

public abstract class AbstractWorkingSetAction extends AbstractAction {

  protected WorkingSetInfo getWorkingSet (Model model, final CommandLineArguments commandLineArguments) {
    String query = commandLineArguments
        .getMandatoryMainArgument("You have to add an parameter containing a name or an id to your command");

    List<WorkingSetInfo> infos = model.findWorkingSetInfosByQuery(query);
    if (infos.size() != 1)
      outputError("Your query <" + query + "> did not choose exactly one project, but " + infos.size()).finish();
    return infos.get(0);
  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.WORKINGSET;
  }

}
