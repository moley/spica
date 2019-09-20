package org.spica.javaclient.actions.projects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.actions.topics.EmptyTopicsAction;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ProjectInfo;

import java.util.ArrayList;

public class EmptyProjectsAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmptyTopicsAction.class);

    @Override
    public String getDisplayname() {
        return "Empty projects";
    }

    @Override
    public String getDescription() {
        return "Empties complete list of projects";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        ModelCache modelCache = actionContext.getModelCache();
        modelCache.setProjectInfos(new ArrayList<ProjectInfo>());

        outputOk("Removed all projects");

        actionContext.saveModelCache();
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.PROJECT;
    }

    @Override
    public Command getCommand() {
        return new Command ("empty", "e");
    }
}
