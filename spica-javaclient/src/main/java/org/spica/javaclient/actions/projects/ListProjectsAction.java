package org.spica.javaclient.actions.projects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.ProjectInfo;

public class ListProjectsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ListProjectsAction.class);

    @Override public String getDisplayname() {
        return "List projects";
    }

    @Override
    public String getDescription() {
        return "List all projects";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
        outputDefault("Projects:\n\n");
        Model model = actionContext.getModel();
        for (ProjectInfo next: model.getProjectInfos()) {
            String topicToken = String.format("     %-40s (%s)", next.getName(), next.getId());

            if (commandLineArguments.noArgumentOr(next.getId(), next.getName()));
              outputDefault(topicToken);
        }

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.PROJECT;
    }

    @Override
    public Command getCommand() {
        return new Command ("list", "l");
    }
}
