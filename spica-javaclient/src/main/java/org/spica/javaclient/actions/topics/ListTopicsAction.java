package org.spica.javaclient.actions.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.TopicInfo;

public class ListTopicsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ListTopicsAction.class);

    @Override public String getDisplayname() {
        return "List topic";
    }

    @Override
    public String getDescription() {
        return "List all topics";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        outputDefault("Topics:\n\n");
        Model model = actionContext.getModel();
        for (TopicInfo next: model.getTopicInfos()) {
            String externalSystemID = next.getExternalSystemID() != null ? next.getExternalSystemID(): "";
            String externalSystemKey = next.getExternalSystemKey() != null ? next.getExternalSystemKey(): "";
            String topicToken = String.format("     %-7s%-20s %-70s (%s)", externalSystemID, externalSystemKey, next.getName(), next.getId());
            outputDefault(topicToken);
        }

        return null;
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