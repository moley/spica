package org.spica.javaclient.actions.configuration;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.Model;

import java.net.URL;
import java.nio.charset.Charset;

public class ShowStatusAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShowStatusAction.class);

    @Override public String getDisplayname() {
        return "Show status";
    }

    @Override
    public String getDescription() {
        return "Show current state";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        outputDefault("Event infos real       : " + model.getEventInfosReal().size());
        outputDefault("Event infos planned    : " + model.getEventInfosPlanned().size());
        outputDefault("Projects               : " + model.getProjectInfos().size());
        outputDefault("Tasks                 : " + model.getTaskInfos().size());
        outputDefault("MessageContainers      : " + model.getMessagecontainerInfos().size());
        outputDefault("Links                  : " + model.getLinkInfos().size());
        outputDefault("Users                  : " + model.getUserInfos().size());
        outputDefault("Model file             : " + model.getCurrentFile().getAbsolutePath());

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

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.CONFIGURATION;
    }

    @Override
    public Command getCommand() {
        return new Command("state", "s");
    }
}
