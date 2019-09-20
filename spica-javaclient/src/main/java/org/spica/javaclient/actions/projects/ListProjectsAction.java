package org.spica.javaclient.actions.projects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.actions.topics.ListTopicsAction;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ProjectInfo;

public class ListProjectsAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(ListTopicsAction.class);


    @Override
    public String getDisplayname() {
        return "List projects";
    }

    @Override
    public String getDescription() {
        return "List all projects";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        outputDefault("Projects:\n\n");
        ModelCache modelCache = actionContext.getModelCache();
        for (ProjectInfo next: modelCache.getProjectInfos()) {
            String topicToken = String.format("     %-40s (%s)", next.getName(), next.getId());
            outputDefault(topicToken);
        }
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.PROJECT;
    }

    @Override
    public Command getCommand() {
        return new Command ("list", "l");
    }
}
