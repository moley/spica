package org.spica.javaclient.actions.tasks;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class RemoveTaskAction extends AbstractAction {

    @Override public String getDisplayname() {
        return "Remove task";
    }

    @Override
    public String getDescription() { return "Remove tasks that match a certain string (in key, description, name or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String query = commandLineArguments.getMandatoryMainArgument("You have to add an parameter containing a name, id or external id to your command");

        Model model = actionContext.getModel();
        List<TaskInfo> infos = model.findTaskInfosByQuery(query);
        model.getTaskInfos().removeAll(infos);

        outputOk("Removed " + infos.size() +  " tasks");

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
