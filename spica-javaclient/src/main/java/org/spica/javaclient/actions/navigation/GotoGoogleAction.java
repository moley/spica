package org.spica.javaclient.actions.navigation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class GotoGoogleAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(GotoJiraAction.class);

    @Override
    public boolean fromButton() {
        return false;
    }

    @Override
    public String getDisplayname() {
        return "Goto google with clipboard content";
    }

    @Override
    public String getDescription() {
        return "Navigates to google. If clipboard has content, it looks for it";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {


        try {
            String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);

            String url = "https://www.google.de";

            if (data != null && ! data.strip().isBlank()) {
                LOGGER.info("Clipboard: " + data);
                String query = data.strip();
                String encodedQuery = URLEncoder.encode(query, Charset.defaultCharset());
                LOGGER.info("Search query " + query);
                LOGGER.info("Encoded query " + encodedQuery);
                url = url + "/search?q=" + encodedQuery;
                LOGGER.info("Search url " + url);
            }

            Desktop.getDesktop().browse(new URI(url));


        } catch (UnsupportedFlavorException e) {
            LOGGER.error("Flavour not supported for accessing system clipboard", e);
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        } catch (URISyntaxException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.GOTO;
    }

    @Override
    public Command getCommand() {
        return new Command("google", "g");
    }
}
