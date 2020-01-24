package org.spica.javaclient.actions.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class SearchGoogleAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(SearchGoogleAction.class);

    @Override public String getDisplayname() {
        return "Goto google";
    }

    @Override
    public String getDescription() {
        return "Navigates to google. If clipboard has content, it looks for it";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {


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
            else
                outputError("You have to add content to clipboard before you call this command");

            Desktop.getDesktop().browse(new URI(url));


        } catch (UnsupportedFlavorException e) {
            LOGGER.error("Flavour not supported for accessing system clipboard", e);
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        } catch (URISyntaxException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return null;

    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.SEARCH;
    }

    @Override
    public Command getCommand() {
        return new Command("google", "g");
    }
}
