package org.spica.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.cli.actions.StandaloneActionContext;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.FoundAction;

public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public final static void main (final String [] args) {
        LOGGER.info("Hello from cli");

        ActionHandler actionHandler = new ActionHandler();
        FoundAction foundAction = actionHandler.findAction(String.join(" ", args));
        LOGGER.info("Found action     :" + foundAction.getAction().getClass().getName());
        LOGGER.info("with parameter   :" + foundAction.getParameter());

        Action action = foundAction.getAction();
        action.execute(new StandaloneActionContext(), foundAction.getParameter());

    }

}
