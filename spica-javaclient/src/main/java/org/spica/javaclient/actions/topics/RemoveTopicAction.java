package org.spica.javaclient.actions.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;

import java.util.List;

public class RemoveTopicAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveTopicAction.class);

    @Override public String getDisplayname() {
        return "Remove topic";
    }

    @Override
    public String getDescription() { return "Remove topics that match a certain string (in key, description, name or id)";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String query = commandLineArguments.getMandatoryFirstArgument("You have to add an parameter containing a name, id or external id to your command");

        ModelCache modelCache = actionContext.getModelCache();
        List<TopicInfo> infos = modelCache.findTopicInfosByQuery(query);
        modelCache.getTopicInfos().removeAll(infos);

        outputOk("Removed " + infos.size() +  " topics");

        actionContext.saveModelCache(getClass().getName());
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TOPIC;
    }

    @Override
    public Command getCommand() {
        return new Command ("remove", "r");
    }

}
