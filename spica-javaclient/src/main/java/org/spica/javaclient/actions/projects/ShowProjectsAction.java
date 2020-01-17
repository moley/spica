package org.spica.javaclient.actions.projects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class ShowProjectsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShowProjectsAction.class);

    @Override public String getDisplayname() {
        return "Show projects";
    }

    @Override
    public String getDescription() {
        return "Show projects that matches a certain string (in name or id)";
    }

    private String getSourceParts (final ProjectInfo projectInfo, final boolean enabled) {
        if (projectInfo.getSourceparts() == null || projectInfo.getSourceparts().isEmpty())
            return "empty";

        Collection<String> ids = new ArrayList<String>();
        projectInfo.getSourceparts().forEach(projectSourcePartInfo -> {
            boolean sourceEnabled = projectSourcePartInfo.isEnabled() != null && projectSourcePartInfo.isEnabled().booleanValue();
            if (sourceEnabled == enabled)
              ids.add(projectSourcePartInfo.getId());
        });
        return ids.size() + "(" + String.join(",", ids) + ")";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        ModelCache modelCache = actionContext.getModelCache();
        String query = commandLineArguments.getOptionalFirstArgumentNotNull();
        List<ProjectInfo> infos = modelCache.findProjectInfosByQuery(query);
        outputOk("Found " + infos.size() + " projects for query <" + query + ">");

        for (ProjectInfo next : infos) {
            outputDefault("ID               : " + next.getId());
            outputDefault("Name             : " + next.getName());
            outputDefault("Parent Project   : " + (next.getParent() != null ? next.getParent().getName() : ""));
            outputDefault("Enabled modules  : " + getSourceParts(next, true));
            outputDefault("Disabled modules : " + getSourceParts(next, false));
            outputDefault("\n\n");
        }
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.PROJECT;
    }

    @Override
    public Command getCommand() {
        return new Command ("show", "s");
    }
}
