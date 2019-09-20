package org.spica.javaclient.actions.projects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.actions.topics.RemoveTopicAction;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ProjectInfo;

import java.util.List;

public class RemoveProjectAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveTopicAction.class);


    @Override
    public String getDisplayname() {
        return "Remove project";
    }

    @Override
    public String getDescription() { return "Remove projects that match a certain string (in name or id)";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        ModelCache modelCache = actionContext.getModelCache();
        List<ProjectInfo> infos = modelCache.findProjectInfosByQuery(parameterList);
        modelCache.getProjectInfos().removeAll(infos);

        outputOk("Removed " + infos.size() +  " projects");

        actionContext.saveModelCache();
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.PROJECT;
    }

    @Override
    public Command getCommand() {
        return new Command ("remove", "r");
    }
}
