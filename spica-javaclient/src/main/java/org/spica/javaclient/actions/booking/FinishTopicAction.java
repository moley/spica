package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.api.TopicApi;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.timetracker.TimetrackerService;

public class FinishTopicAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(FinishTopicAction.class);

    @Override
    public String getDescription() {
        return "Finish current topic";
    }

    @Override public String getDisplayname() {
        return "Finish current topic";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
        Model model = actionContext.getModel();
        SpicaProperties spicaProperties = actionContext.getProperties();

        EventInfo eventInfo = model.findLastOpenEventFromToday();
        LOGGER.info("Found event " + eventInfo);
        if (eventInfo != null && eventInfo.getEventType().equals(EventType.TOPIC)) {
            LOGGER.info("Reference ID: " + eventInfo.getReferenceId());
            if (eventInfo.getReferenceId() != null) {
                TopicInfo topicInfo = model.findTopicInfoById(eventInfo.getReferenceId());

                TopicApi topicApi = new TopicApi();
                try {
                    String jiraUser = spicaProperties.getValue("spica.jira.user");
                    topicApi.finishTopic(jiraUser, topicInfo.getExternalSystemKey());
                    TopicInfo removeTopic = model.findTopicInfoById(topicInfo.getExternalSystemKey());
                    model.getTopicInfos().remove(removeTopic);
                } catch (ApiException e) {
                    LOGGER.error("Error importing jira topics: " + e.getResponseBody(), e);
                }
            }

            TimetrackerService timetrackerService = new TimetrackerService();
            timetrackerService.setModelCacheService(actionContext.getServices().getModelCacheService());
            timetrackerService.finishEvent(eventInfo);

            outputDefault("Finished topic " + eventInfo.getName());

            actionContext.saveModel(getClass().getName());


        }

        return null;

        //finish event

    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command("finish", "f");
    }


}
