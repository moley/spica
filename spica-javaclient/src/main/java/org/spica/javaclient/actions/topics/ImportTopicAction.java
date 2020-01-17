package org.spica.javaclient.actions.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.api.TopicApi;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicContainerInfo;

public class ImportTopicAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImportTopicAction.class);

    @Override public String getDisplayname() {
        return "Import topic";
    }

    @Override
    public String getDescription() {
        return "Imports or refreshes imported topics from an external system";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        ModelCache modelCache = actionContext.getModelCache();

        SpicaProperties spicaProperties = actionContext.getSpicaProperties();
        String jiraUser = spicaProperties.getValue("spica.jira.user");
        String jiraPassword = spicaProperties.getValue("spica.jira.password");

        TopicApi topicApi = new TopicApi();
        try {
            TopicContainerInfo topicContainerInfo = topicApi.importTopics(jiraUser, jiraUser, jiraPassword);
            modelCache.setTopicInfos(topicContainerInfo.getTopics());
            outputOk("Imported " + topicContainerInfo.getTopics().size() + " topics");
        } catch (ApiException e) {
            LOGGER.error("Error importing jira topics: " + e.getResponseBody(), e);
        }
        actionContext.saveModelCache(getClass().getName());
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TOPIC;
    }

    @Override
    public Command getCommand() {
        return new Command ("import", "i");
    }
}
