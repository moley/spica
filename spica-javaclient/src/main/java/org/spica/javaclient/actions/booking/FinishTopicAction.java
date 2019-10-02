package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.api.TopicApi;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.timetracker.TimetrackerService;

public class FinishTopicAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(FinishTopicAction.class);


    @Override
    public String getDisplayname() {
        return "Finish topic";
    }

    @Override
    public String getDescription() {
        return "Finish current topic";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {
        ModelCache modelCache = actionContext.getModelCache();
        SpicaProperties spicaProperties = actionContext.getSpicaProperties();

        EventInfo eventInfo = modelCache.findLastOpenEventFromToday();
        LOGGER.info("Found event " + eventInfo);
        if (eventInfo != null && eventInfo.getEventType().equals(EventType.TOPIC)) {
            LOGGER.info("Reference ID: " + eventInfo.getReferenceId());
            if (eventInfo.getReferenceId() != null) {
                TopicInfo topicInfo = modelCache.findTopicInfoById(eventInfo.getReferenceId());

                TopicApi topicApi = new TopicApi();
                try {
                    String jiraUser = spicaProperties.getValue("spica.jira.user");
                    topicApi.finishTopic(jiraUser, topicInfo.getExternalSystemKey());
                    TopicInfo removeTopic = modelCache.findTopicInfoById(topicInfo.getExternalSystemKey());
                    modelCache.getTopicInfos().remove(removeTopic);
                } catch (ApiException e) {
                    LOGGER.error("Error importing jira topics: " + e.getResponseBody(), e);
                }
            }

            TimetrackerService timetrackerService = new TimetrackerService();
            timetrackerService.setModelCacheService(actionContext.getModelCacheService());
            timetrackerService.finishEvent(eventInfo);

            outputDefault("Finished topic " + eventInfo.getName());

            actionContext.saveModelCache(getClass().getName());


        }

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
