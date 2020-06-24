package org.spica.javaclient.actions.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.api.TaskApi;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskContainerInfo;

public class ImportTaskAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImportTaskAction.class);

    @Override public String getDisplayname() {
        return "Import topic";
    }

    @Override
    public String getDescription() {
        return "Imports or refreshes imported topics from an external system";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        SpicaProperties spicaProperties = actionContext.getProperties();
        String jiraUser = spicaProperties.getValue("spica.jira.user");
        String jiraPassword = spicaProperties.getValue("spica.jira.password");

        TaskApi topicApi = new TaskApi();
        try {
            TaskContainerInfo topicContainerInfo = topicApi.importTasks(jiraUser, jiraUser, jiraPassword);
            model.setTaskInfos(topicContainerInfo.getTasks());
            outputOk("Imported " + topicContainerInfo.getTasks().size() + " topics");
        } catch (ApiException e) {
            LOGGER.error("Error importing jira topics: " + e.getResponseBody(), e);
        }
        actionContext.saveModel(getClass().getName());

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TASKS;
    }

    @Override
    public Command getCommand() {
        return new Command ("import", "i");
    }
}
