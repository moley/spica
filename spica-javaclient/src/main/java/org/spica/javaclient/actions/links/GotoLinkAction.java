package org.spica.javaclient.actions.links;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.links.LinkFinder;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.utils.RenderUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class GotoLinkAction implements Action {

        private final static Logger LOGGER = LoggerFactory.getLogger(GotoLinkAction.class);

        @Override
        public String getDisplayname() {
            return "Goto links";
        }

        @Override
        public String getDescription() {
            return "Navigate to all links that match the param (name, url or id)";
        }

        @Override
        public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

            outputDefault("Links:\n\n");

            List<LinkInfo> linkInfos = actionContext.getModelCache().findLinkInfosByQuery(parameterList);
            for (LinkInfo next: linkInfos) {
                try {
                    Desktop.getDesktop().browse(new URI(next.getUrl()));
                } catch (IOException e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                } catch (URISyntaxException e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                }
            }

        }


        @Override
        public ActionGroup getGroup() {
            return ActionGroup.LINKS;
        }

        @Override
        public Command getCommand() {
            return new Command ("goto", "g");
        }
}
