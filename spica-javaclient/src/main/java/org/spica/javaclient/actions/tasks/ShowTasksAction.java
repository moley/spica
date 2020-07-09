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

public class ShowTasksAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShowTasksAction.class);



    @Override public String getDisplayname() {
        return "Show tasks";
    }

    @Override
    public String getDescription() {
        return "Show tasks that matches a certain string (in key, description, name or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
        Model model = actionContext.getModel();
        String query = commandLineArguments.getOptionalMainArgumentNotNull();
        List<TaskInfo> infos = model.findTaskInfosByQuery(query);
        outputOk("Found " + infos.size() + " topics for query <" + query + ">");

        for (TaskInfo next : infos) {

            outputDefault("ID               : " + next.getId());
            outputDefault("Name             : " + next.getName());
            outputDefault("Description      : " + next.getDescription());
            outputDefault("State            : " + next.getState());
            outputDefault("Project          : " + next.getProjectId());
            outputDefault("External key     : " + (next.getExternalSystemKey() != null ? next.getExternalSystemKey(): "none"));
            outputDefault("External system  : " + (next.getExternalSystemID() != null ? next.getExternalSystemID(): "none"));
            outputDefault("\n\n");
        }

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TASKS;
    }

    @Override
    public Command getCommand() {
        return new Command("show", "s");
    }
}