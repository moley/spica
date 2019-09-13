package org.spica.javaclient.actions.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;

import java.util.ArrayList;

public class EmptyTopicsAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmptyTopicsAction.class);

    @Override
    public boolean fromButton() {
        return false;
    }

    @Override
    public String getDisplayname() {
        return "Empty topics";
    }

    @Override
    public String getDescription() {
        return "Empties complete list of topics";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        ModelCache modelCache = actionContext.getModelCache();
        modelCache.setTopicInfos(new ArrayList<TopicInfo>());
        actionContext.saveModelCache();
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TOPIC;
    }

    @Override
    public Command getCommand() {
        return new Command ("empty", "e");
    }


}