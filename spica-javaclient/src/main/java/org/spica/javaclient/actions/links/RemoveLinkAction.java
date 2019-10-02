package org.spica.javaclient.actions.links;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.utils.RenderUtil;

public class RemoveLinkAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveLinkAction.class);

    private RenderUtil renderUtil = new RenderUtil();

    @Override
    public String getDisplayname() {
        return "Remove link";
    }

    @Override
    public String getDescription() {
        return "Remove link with id";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        if (parameterList.strip().isBlank())
            throw new IllegalStateException("You have to add an id to your command");

        ModelCache modelCache = actionContext.getModelCache();

        LinkInfo linkInfoById = modelCache.findLinkInfoById(parameterList);
        if (linkInfoById == null)
            throw new IllegalStateException("No link with id " + parameterList + " found");

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