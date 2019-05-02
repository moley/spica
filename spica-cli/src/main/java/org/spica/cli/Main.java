package org.spica.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.cli.actions.StandaloneActionContext;
import org.spica.cli.actions.StandaloneActionParamFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.FoundAction;

public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public final static void main (final String [] args) {

        ActionHandler actionHandler = new ActionHandler();
        if (args.length == 0) {
            System.out.println("");
            for (String next: actionHandler.getHelp()) {
                System.out.println (next);
            }
            System.out.println("\n");
        }
        else {

            FoundAction foundAction = actionHandler.findAction(String.join(" ", args));
            LOGGER.info("Found action     : " + foundAction.getAction().getClass().getName());
            LOGGER.info("with parameter   : " + foundAction.getParameter());

            Action action = foundAction.getAction();

            StandaloneActionContext actionContext = new StandaloneActionContext();

            if (! action.getInputParams(actionContext).isEmpty()) {
                StandaloneActionParamFactory actionParamFactory = new StandaloneActionParamFactory();

                actionParamFactory.build(actionContext, foundAction);
            }
            else
              action.execute(new StandaloneActionContext(), action.getInputParams(actionContext),foundAction.getParameter());
        }

    }

}
