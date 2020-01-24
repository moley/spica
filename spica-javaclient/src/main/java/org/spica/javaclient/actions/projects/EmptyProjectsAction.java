package org.spica.javaclient.actions.projects;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class EmptyProjectsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmptyProjectsAction.class);

    @Override public String getDisplayname() {
        return "Empty projects";
    }

    @Override
    public String getDescription() {
        return "Empties complete list of projects";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();
        model.setProjectInfos(new ArrayList<ProjectInfo>());

        outputOk("Removed all projects");

        actionContext.saveModel(getClass().getName());
        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.PROJECT;
    }

    @Override
    public Command getCommand() {
        return new Command ("empty", "e");
    }
}
