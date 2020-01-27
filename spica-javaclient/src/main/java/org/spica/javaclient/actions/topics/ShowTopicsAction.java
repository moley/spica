package org.spica.javaclient.actions.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.TopicInfo;

import java.util.List;

public class ShowTopicsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShowTopicsAction.class);

    @Override public String getDisplayname() {
        return "Show topics";
    }

    @Override
    public String getDescription() {
        return "Show topics that matches a certain string (in key, description, name or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
        Model model = actionContext.getModel();
        String query = commandLineArguments.getOptionalMainArgumentNotNull();
        List<TopicInfo> infos = model.findTopicInfosByQuery(query);
        outputOk("Found " + infos.size() + " topics for query <" + query + ">");

        for (TopicInfo next : infos) {

            outputDefault("ID               : " + next.getId());
            outputDefault("Name             : " + next.getName());
            outputDefault("Description      : " + next.getDescription());
            outputDefault("State            : " + next.getState());
            outputDefault("Project          : " + (next.getProject() != null ? next.getProject().getName(): "none"));
            outputDefault("External key     : " + (next.getExternalSystemKey() != null ? next.getExternalSystemKey(): "none"));
            outputDefault("External system  : " + (next.getExternalSystemID() != null ? next.getExternalSystemID(): "none"));
            outputDefault("\n\n");
        }

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TOPIC;
    }

    @Override
    public Command getCommand() {
        return new Command("show", "s");
    }
}