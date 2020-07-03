package org.spica.javaclient.actions.tasks;

import java.util.Arrays;
import java.util.UUID;
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
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;

public class CreateTaskAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateTaskAction.class);

    public final static String KEY_SUMMARY = "summary";
    public final static String KEY_DESCRIPTION = "description";

    public final static String ERROR_PARAM_NAME = "You did not define a subject as parameter";

    @Override public String getDisplayname() {
        return "Create task";
    }


    @Override
    public String getDescription() {
        return "Creates a task (with parameter as name)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String name = inputParams.getInputValueAsString(KEY_SUMMARY);
        ActionResult actionResult = new ActionResult();

        if (name != null) {
            TaskInfo topicInfo = new TaskInfo();
            topicInfo.setId(UUID.randomUUID().toString());
            topicInfo.setDescription(inputParams.getInputValueAsString(KEY_DESCRIPTION));
            topicInfo.setName(name);
            Model model = actionContext.getModel();
            model.getTaskInfos().add(topicInfo);
            actionResult.setUserObject(topicInfo);

            outputOk("Created topic " + topicInfo.getName() + "(" + topicInfo.getId() + ")");

            actionContext.saveModel(getClass().getName());
        }
        else
            outputError(ERROR_PARAM_NAME);

        return actionResult;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TASKS;
    }

    @Override
    public Command getCommand() {
        return new Command ("create", "c");
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {

        TextInputParam summary = new TextInputParam(1, KEY_SUMMARY, "Summary");
        TextInputParam description = new TextInputParam(5, KEY_DESCRIPTION, "Description");

        InputParamGroup inputParamGroup = new InputParamGroup();
        inputParamGroup.getInputParams().add(summary);
        inputParamGroup.getInputParams().add(description);

        return new InputParams(Arrays.asList(inputParamGroup));
    }
}
