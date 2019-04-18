package org.spica.javaclient.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.params.InputParam;
import org.spica.javaclient.actions.params.InputParamGroup;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.actions.params.TextInputParam;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;

import java.util.Arrays;
import java.util.List;

public class CreateTopicAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateTopicAction.class);

    public final static String KEY_SUMMARY = "summary";
    public final static String KEY_DESCRIPTION = "description";


    @Override
    public boolean fromButton() {
        return true;
    }

    @Override
    public String getDisplayname() {
        return "Create topic";
    }

    @Override
    public String getDescription() {
        return "Creates a topic (parameter is topic subject)";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {


        TopicInfo topicInfo = new TopicInfo();
        topicInfo.setDescription(inputParams.getInputParamAsString(KEY_DESCRIPTION));
        topicInfo.setName(inputParams.getInputParamAsString(KEY_SUMMARY));
        ModelCache modelCache = actionContext.getModelCache();
        modelCache.getTopicInfos().add(topicInfo);

        actionContext.saveModelCache();
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
    public InputParams getInputParams() {

        TextInputParam summary = TextInputParam.builder().key(KEY_SUMMARY).displayname("Summary").value("").build();
        TextInputParam description = TextInputParam.builder().key(KEY_DESCRIPTION).displayname("Description").numberOfLines(5).value("").build();

        InputParamGroup inputParamGroup = new InputParamGroup();
        inputParamGroup.getInputParams().add(summary);
        inputParamGroup.getInputParams().add(description);

        return new InputParams(Arrays.asList(inputParamGroup));
    }
}
