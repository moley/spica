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
import org.spica.javaclient.utils.LogUtil;

import java.util.List;

public class RemoveTopicAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveTopicAction.class);

    @Override
    public String getDisplayname() {
        return "Remove topic";
    }

    @Override
    public String getDescription() { return "Remove topics that match a certain string (in key, description, name or id)";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        ModelCache modelCache = actionContext.getModelCache();
        List<TopicInfo> infos = modelCache.findTopicInfosByQuery(parameterList);
        modelCache.getTopicInfos().removeAll(infos);

        outputOk("Removed " + infos.size() +  " projects");

        actionContext.saveModelCache();
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
