package org.spica.javaclient.actions.tasks;

import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.FlagInputParam;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class ListTasksAction extends AbstractAction {

    private final static String KEY_ALL = "all";

    @Override public String getDisplayname() {
        return "List tasks";
    }

    @Override
    public String getDescription() {
        return "List all tasks";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        outputDefault("Tasks:\n\n");

        boolean all = inputParams.getInputValueAsBoolean(KEY_ALL, false);
        Model model = actionContext.getModel();
        for (TaskInfo next: model.getTaskInfos()) {

            boolean topicIsOpen = next.getState() == null || !next.getState().equals(TaskInfo.StateEnum.FINISHED);
            if (topicIsOpen || all){

                String externalSystemID = next.getExternalSystemID() != null ? next.getExternalSystemID() : "";
                String externalSystemKey = next.getExternalSystemKey() != null ? next.getExternalSystemKey() : "";
                String topicToken = String
                    .format("     %-7s%-20s %-70s (%s)", externalSystemID, externalSystemKey, next.getName(), next.getId());
                outputDefault(topicToken);
            }
        }

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TASKS;
    }

    @Override
    public Command getCommand() {
        return new Command ("list", "l");
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {
        InputParams inputParams = new InputParams();
        InputParamGroup inputParamGroup = new InputParamGroup();

        inputParamGroup.getInputParams().add(new FlagInputParam(KEY_ALL));

        inputParams.getInputParamGroups().add(inputParamGroup);
        return inputParams;
    }


}