package org.spica.javaclient.actions.projects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ProjectInfo;

import java.util.List;

public class ShowProjectsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShowProjectsAction.class);

    @Override
    public String getDisplayname() {
        return "Show projects";
    }

    @Override
    public String getDescription() {
        return "Show projects that matches a certain string (in name or id)";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        ModelCache modelCache = actionContext.getModelCache();
        List<ProjectInfo> infos = modelCache.findProjectInfosByQuery(parameterList);
        outputOk("Found " + infos.size() + " projects for query <" + parameterList + ">");

        for (ProjectInfo next : infos) {
            outputDefault("ID               : " + next.getId());
            outputDefault("Name             : " + next.getName());
            outputDefault("Parent Project   : " + next.getParent() != null ? next.getParent().getName() : "");
            outputDefault("\n\n");
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
