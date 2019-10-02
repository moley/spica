package org.spica.javaclient.actions.projects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ProjectInfo;

import java.util.List;

public class RemoveProjectAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveProjectAction.class);


    @Override
    public String getDisplayname() {
        return "Remove projects";
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

        actionContext.saveModelCache(getClass().getName());
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
