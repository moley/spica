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

public class ListTopicsAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(ListTopicsAction.class);

    @Override
    public boolean fromButton() {
        return false;
    }

    @Override
    public String getDisplayname() {
        return "List topics";
    }

    @Override
    public String getDescription() {
        return "List all topics";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        System.out.println ("Topics:\n\n");
        ModelCache modelCache = actionContext.getModelCache();
        for (TopicInfo next: modelCache.getTopicInfos()) {
            String topicToken = String.format("     %-7s%-20s %-70s (%s)", next.getExternalSystemID(), next.getExternalSystemKey(), next.getName(), next.getId());
            System.out.println (topicToken);
        }
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TOPIC;
    }

    @Override
    public Command getCommand() {
        return new Command ("list", "l");
    }


}