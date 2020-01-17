package org.spica.javaclient.actions.links;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.ModelCache;

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
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        ModelCache modelCache = actionContext.getModelCache();

        String query = commandLineArguments.getMandatoryFirstArgument("You have to add a parameter containing an ID to your command");

        LinkInfo linkInfoById = modelCache.findLinkInfoById(query);
        if (linkInfoById == null)
            throw new IllegalStateException("No link with id " + query + " found");

        modelCache.getLinkInfos().remove(linkInfoById);

        outputOk("Removed link with id " + linkInfoById.getId());

        actionContext.saveModelCache(getClass().getName());
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