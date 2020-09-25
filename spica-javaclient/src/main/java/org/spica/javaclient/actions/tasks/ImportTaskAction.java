package org.spica.javaclient.actions.tasks;

import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.api.TaskApi;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskContainerInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class ImportTaskAction extends AbstractAction {

    @Override public String getDisplayname() {
        return "Import tasks";
    }

    @Override
    public String getDescription() {
        return "Synchronizes tasks from an external system";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        SpicaProperties spicaProperties = actionContext.getProperties();
        String jiraUser = spicaProperties.getValueNotNull("spica.jira.user");
        String jiraPassword = spicaProperties.getValueNotNull("spica.jira.password");

        TaskApi taskApi = actionContext.getApi().getTaskApi();
        try {
            TaskContainerInfo topicContainerInfo = taskApi.importTasks(jiraUser, jiraUser, jiraPassword);
            model.setTaskInfos(topicContainerInfo.getTasks());
            outputOk("Imported " + topicContainerInfo.getTasks().size() + " taks");
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
