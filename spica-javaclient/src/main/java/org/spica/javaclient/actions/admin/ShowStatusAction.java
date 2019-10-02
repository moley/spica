package org.spica.javaclient.actions.admin;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.ModelCache;

import java.net.URL;
import java.nio.charset.Charset;

public class ShowStatusAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShowStatusAction.class);

    @Override
    public String getDisplayname() {
        return "Show state";
    }

    @Override
    public String getDescription() {
        return "Show current state";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        ModelCache modelCache = actionContext.getModelCache();

        outputDefault("Event infos real       : " + modelCache.getEventInfosReal().size());
        outputDefault("Event infos planned    : " + modelCache.getEventInfosPlanned().size());
        outputDefault("Topics                 : " + modelCache.getTopicInfos().size());
        outputDefault("MessageContainers      : " + modelCache.getMessagecontainerInfos().size());
        outputDefault("Links                  : " + modelCache.getLinkInfos().size());
        outputDefault("Users                  : " + modelCache.getUserInfos().size());
        outputDefault("Model file             : " + modelCache.getCurrentFile().getAbsolutePath());

        try {
            URL leguanVersion = getClass().getClassLoader().getResource("spica-cli.version");
            String version = "not found";
            if (leguanVersion != null) {
                version = IOUtils.toString(leguanVersion, Charset.defaultCharset());
            }

            outputDefault("Spica version          : " + version);

        } catch (Throwable e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.ADMIN;
    }

    @Override
    public Command getCommand() {
        return new Command("state", "s");
    }
}
