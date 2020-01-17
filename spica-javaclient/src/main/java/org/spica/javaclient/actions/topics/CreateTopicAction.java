package org.spica.javaclient.actions.topics;

import java.util.Arrays;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;

public class CreateTopicAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateTopicAction.class);

    public final static String KEY_SUMMARY = "summary";
    public final static String KEY_DESCRIPTION = "description";

    public final static String ERROR_PARAM_NAME = "You did not define a subject as parameter";

    @Override public String getDisplayname() {
        return "Create topic";
    }


    @Override
    public String getDescription() {
        return "Creates a topic (parameter is topic subject)";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String name = inputParams.getInputValueAsString(KEY_SUMMARY);

        if (name != null) {
            TopicInfo topicInfo = new TopicInfo();
            topicInfo.setId(UUID.randomUUID().toString());
            topicInfo.setDescription(inputParams.getInputValueAsString(KEY_DESCRIPTION));
            topicInfo.setName(name);
            ModelCache modelCache = actionContext.getModelCache();
            modelCache.getTopicInfos().add(topicInfo);

            outputOk("Created topic " + topicInfo.getName() + "(" + topicInfo.getId() + ")");

            actionContext.saveModelCache(getClass().getName());
        }
        else
            outputError(ERROR_PARAM_NAME);
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TOPIC;
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
