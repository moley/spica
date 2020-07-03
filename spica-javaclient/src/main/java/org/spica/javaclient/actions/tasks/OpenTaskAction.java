package org.spica.javaclient.actions.tasks;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class OpenTaskAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(OpenTaskAction.class);


    @Override public String getDisplayname() {
        return "Open task";
    }


    @Override
    public String getDescription() {
        return "Open tasks that match a certain string (in key, description, name or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String query = commandLineArguments.getMandatoryMainArgument("You have to add an parameter containing a name, id or external id to your command");
        Model model = actionContext.getModel();
        List<TaskInfo> infos = model.findTaskInfosByQuery(query);
        for (TaskInfo next: infos) {
            next.setState(TaskInfo.StateEnum.NEW);
            outputDefault("Opening " + next.getId());
        }

        actionContext.saveModel(getClass().getName());
        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TASKS;
    }

    @Override
    public Command getCommand() {
        return new Command ("open", "o");
    }


}
