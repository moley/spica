package org.spica.javaclient.actions.projects;

import java.util.List;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.params.CommandLineArguments;

public abstract class AbstractProjectAction extends AbstractAction {

  protected ProjectInfo getProject (Model model, final CommandLineArguments commandLineArguments) {
    String query = commandLineArguments
        .getMandatoryMainArgument("You have to add an parameter containing a name or an id to your command");

    List<ProjectInfo> infos = model.findProjectInfosByQuery(query);
    if (infos.size() != 1)
      outputError("Your query <" + query + "> did not choose exactly one project, but " + infos.size()).finish();
    return infos.get(0);
  }
}
