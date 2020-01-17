package org.spica.javaclient.actions.auto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class ExecuteScriptAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExecuteScriptAction.class);

    @Override public String getDisplayname() {
        return "Execute script";
    }

    @Override
    public String getDescription() {
        return "Execute script";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
        throw new IllegalStateException("NYI");
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.AUTOMATION;
    }

    @Override
    public Command getCommand() {
        return new Command ("execute", "e");
    }


}
