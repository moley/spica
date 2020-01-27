package org.spica.javaclient.actions.projects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.ProjectInfo;

import java.util.List;

public class RemoveProjectAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveProjectAction.class);

    @Override public String getDisplayname() {
        return "Remove project";
    }

    @Override
    public String getDescription() { return "Remove projects that match a certain string (in name or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();
        String query = commandLineArguments.getMandatoryMainArgument("You have to add an parameter containing a name or an id to your command");
        List<ProjectInfo> infos = model.findProjectInfosByQuery(query);
        model.getProjectInfos().removeAll(infos);

        outputOk("Removed " + infos.size() +  " projects");

        actionContext.saveModel(getClass().getName());

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.PROJECT;
    }

    @Override
    public Command getCommand() {
        return new Command ("remove", "r");
    }
}
