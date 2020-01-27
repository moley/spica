package org.spica.javaclient.actions.links;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.Model;

public class RemoveLinkAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveLinkAction.class);

    @Override public String getDisplayname() {
        return "Remove link";
    }

    @Override
    public String getDescription() {
        return "Remove link with id";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        String query = commandLineArguments.getMandatoryMainArgument("You have to add a parameter containing an ID to your command");

        LinkInfo linkInfoById = model.findLinkInfoById(query);
        if (linkInfoById == null)
            throw new IllegalStateException("No link with id " + query + " found");

        model.getLinkInfos().remove(linkInfoById);

        outputOk("Removed link with id " + linkInfoById.getId());

        actionContext.saveModel(getClass().getName());

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.LINKS;
    }

    @Override
    public Command getCommand() {
        return new Command ("remove", "r");
    }
}