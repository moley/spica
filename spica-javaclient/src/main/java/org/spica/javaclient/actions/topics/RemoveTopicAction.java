package org.spica.javaclient.actions.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
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
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String query = commandLineArguments.getMandatoryMainArgument("You have to add an parameter containing a name, id or external id to your command");

        Model model = actionContext.getModel();
        List<TopicInfo> infos = model.findTopicInfosByQuery(query);
        model.getTopicInfos().removeAll(infos);

        outputOk("Removed " + infos.size() +  " topics");

        actionContext.saveModel(getClass().getName());

        return null;
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
