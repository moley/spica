package org.spica.javaclient.actions.projects;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class ShowProjectsAction extends AbstractProjectAction {

    @Override public String getDisplayname() {
        return "Show projects";
    }

    @Override
    public String getDescription() {
        return "Show projects that matches a certain string (in name or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();
        String query = commandLineArguments.getOptionalMainArgumentNotNull();
        List<ProjectInfo> infos = model.findProjectInfosByQuery(query);
        outputOk("Found " + infos.size() + " projects for query <" + query + ">");

        for (ProjectInfo next : infos) {
            outputDefault("ID               : " + next.getId());
            outputDefault("Name             : " + next.getName());
            outputDefault("Parent Project   : " + next.getParentId());
            outputDefault("\n\n");
        }

        return null;
    }

    @Override
    public Command getCommand() {
        return new Command ("show", "s");
    }
}
