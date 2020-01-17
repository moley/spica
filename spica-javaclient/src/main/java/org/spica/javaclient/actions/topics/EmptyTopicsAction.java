package org.spica.javaclient.actions.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;

import java.util.ArrayList;

public class EmptyTopicsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmptyTopicsAction.class);

    @Override public String getDisplayname() {
        return "Empty topics";
    }

    @Override
    public String getDescription() {
        return "Empties complete list of topics";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        ModelCache modelCache = actionContext.getModelCache();
        modelCache.setTopicInfos(new ArrayList<TopicInfo>());

        outputOk("Removed all topics");

        actionContext.saveModelCache(getClass().getName());
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