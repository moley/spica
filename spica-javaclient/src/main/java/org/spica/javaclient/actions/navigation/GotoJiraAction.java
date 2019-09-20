package org.spica.javaclient.actions.navigation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GotoJiraAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(GotoJiraAction.class);

    @Override
    public String getDisplayname() {
        return "Goto current topic in Jira";
    }

    @Override
    public String getDescription() {
        return "Navigates to jira into details of issue, which you are working on";
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
                TopicInfo newValue = modelCache.findTopicInfoById(eventInfo.getReferenceId());
                String key = newValue.getExternalSystemKey();
                //TODO make multi system able
                String jiraBaseUrl = spicaProperties.getValue("spica.jira.url");
                String jiraUrl = jiraBaseUrl + "/browse/" + key;
                LOGGER.info("Step to " + jiraUrl);

                try {
                    Desktop.getDesktop().browse(new URI(jiraUrl));
                } catch (IOException e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                } catch (URISyntaxException e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                }
            }
        }
        else
            LOGGER.info("No task available, were you are currently working on");

    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.GOTO;
    }

    @Override
    public Command getCommand() {
        return new Command("jira", "j");
    }
}
