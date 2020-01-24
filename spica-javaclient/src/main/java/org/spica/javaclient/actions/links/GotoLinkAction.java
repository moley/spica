package org.spica.javaclient.actions.links;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.LinkInfo;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class GotoLinkAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(GotoLinkAction.class);

    @Override public String getDisplayname() {
        return "Goto link";
    }

    @Override
    public String getDescription() {
        return "Navigate to all links that match the param (name, url or id)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        outputDefault("Links:\n\n");

        String query = commandLineArguments.getMandatoryFirstArgument("You have to add a query param (name, id, url) to the search");
        List<LinkInfo> linkInfos = actionContext.getModel().findLinkInfosByQuery(query);

        if (linkInfos.isEmpty())
            outputError("No links found for query <" + query + ">");
        else {
            for (LinkInfo next : linkInfos) {
                try {
                    Desktop.getDesktop().browse(new URI(next.getUrl()));
                } catch (IOException e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                } catch (URISyntaxException e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                }
            }
        }

        return null;

    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.LINKS;
    }

    @Override
    public Command getCommand() {
        return new Command("goto", "g");
    }
}
