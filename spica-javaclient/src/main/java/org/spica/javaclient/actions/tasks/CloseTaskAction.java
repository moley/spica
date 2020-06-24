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

public class CloseTaskAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(CloseTaskAction.class);


    @Override public String getDisplayname() {
        return "Finish topic";
    }


    @Override
    public String getDescription() {
        return "Finishes a topic (parameter is topic subject)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String query = commandLineArguments.getMandatoryMainArgument("You have to add an parameter containing a name, id or external id to your command");
        Model model = actionContext.getModel();
        List<TaskInfo> infos = model.findTaskInfosByQuery(query);
        for (TaskInfo next: infos) {
            next.setState(TaskStatus.CLOSED.name());
            outputDefault("Finishing " + next.getId());
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
        return new Command ("finish", "f");
    }


}
