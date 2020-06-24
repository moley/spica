package org.spica.javaclient.actions.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;

import java.util.ArrayList;

public class EmptyTasksAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmptyTasksAction.class);

    @Override public String getDisplayname() {
        return "Empty topics";
    }

    @Override
    public String getDescription() {
        return "Empties complete list of topics";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();
        model.setTaskInfos(new ArrayList<TaskInfo>());

        outputOk("Removed all topics");

        actionContext.saveModel(getClass().getName());

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TASKS;
    }

    @Override
    public Command getCommand() {
        return new Command ("empty", "e");
    }


}