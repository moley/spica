package org.spica.javaclient.actions.workingsets;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class RemoveWorkingSetAction extends AbstractWorkingSetAction {

    @Override public String getDisplayname() {
        return "Remove workingset";
    }

    @Override
    public String getDescription() { return "Remove workingset that match a certain string (in name or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String query = commandLineArguments.getMandatoryMainArgument("You have to add an parameter containing a name, id or external id to your command");

        Model model = actionContext.getModel();
        List<WorkingSetInfo> infos = model.findWorkingSetInfosByQuery(query);
        model.getWorkingsetInfos().removeAll(infos);

        outputOk("Removed " + infos.size() +  " workingsets");

        actionContext.saveModel(getClass().getName());

        return null;
    }

    @Override
    public Command getCommand() {
        return new Command ("remove", "r");
    }

}
