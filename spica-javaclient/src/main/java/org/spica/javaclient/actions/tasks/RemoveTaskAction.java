package org.spica.javaclient.actions.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.TaskInfo;

import java.util.List;

public class RemoveTaskAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveTaskAction.class);

    @Override public String getDisplayname() {
        return "Remove topic";
    }

    @Override
    public String getDescription() { return "Remove topics that match a certain string (in key, description, name or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String query = commandLineArguments.getMandatoryMainArgument("You have to add an parameter containing a name, id or external id to your command");

        Model model = actionContext.getModel();
        List<TaskInfo> infos = model.findTaskInfosByQuery(query);
        model.getTaskInfos().removeAll(infos);

        outputOk("Removed " + infos.size() +  " topics");

        actionContext.saveModel(getClass().getName());

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TASKS;
    }

    @Override
    public Command getCommand() {
        return new Command ("remove", "r");
    }

}
