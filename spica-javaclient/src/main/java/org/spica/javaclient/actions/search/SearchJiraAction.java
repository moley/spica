package org.spica.javaclient.actions.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.TopicInfo;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SearchJiraAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(SearchJiraAction.class);

    @Override public String getDisplayname() {
        return "Goto jira";
    }

    @Override
    public String getDescription() {
        return "Navigates to jira into details of issue, which you are working on";
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
                TopicInfo newValue = model.findTopicInfoById(eventInfo.getReferenceId());
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

        return null;

    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.SEARCH;
    }

    @Override
    public Command getCommand() {
        return new Command("jira", "j");
    }
}
