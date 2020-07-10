package org.spica.javaclient.actions.workingsets;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class ShowWorkingSetsAction extends AbstractAction {

    @Override public String getDisplayname() {
        return "Show workingsets";
    }

    @Override
    public String getDescription() {
        return "Show workingsets that matches a certain string (in key, description, name or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
        Model model = actionContext.getModel();
        String query = commandLineArguments.getOptionalMainArgumentNotNull();
        List<WorkingSetInfo> infos = model.findWorkingSetInfosByQuery(query);
        outputOk("Found " + infos.size() + " workingsets for query <" + query + ">");

        for (WorkingSetInfo next : infos) {
            outputDefault("ID               : " + next.getId());
            outputDefault("Name             : " + next.getName());
            if (next.getSourceparts() != null) {
                outputDefault("SourceParts      : " + next.getSourceparts().size());
                for (WorkingSetSourcePartInfo sourcePartInfo : next.getSourceparts()) {
                    String state = (sourcePartInfo.isEnabled() != null && sourcePartInfo.isEnabled()) ? "enabled" : "disabled";
                    outputDefault("                   " + sourcePartInfo.getUrl() + "-" + sourcePartInfo.getBranch() + " (" + state + ")");
                }
            }
            outputDefault("\n\n");
        }

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.WORKINGSET;
    }

    @Override
    public Command getCommand() {
        return new Command("show", "s");
    }
}