package org.spica.javaclient.actions.projects;

import java.util.List;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.params.CommandLineArguments;

public abstract class AbstractProjectAction extends AbstractAction {

  protected ProjectInfo getProject (ModelCache modelCache, final CommandLineArguments commandLineArguments) {
    String query = commandLineArguments
        .getMandatoryFirstArgument("You have to add an parameter containing a name or an id to your command");

    List<ProjectInfo> infos = modelCache.findProjectInfosByQuery(query);
    if (infos.size() != 1)
      outputError("Your query <" + query + "> did not choose exactly one project, but " + infos.size()).finish();
    return infos.get(0);
  }
}
