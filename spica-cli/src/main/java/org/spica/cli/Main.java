package org.spica.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.cli.actions.StandaloneActionContext;
import org.spica.cli.actions.StandaloneActionParamFactory;
import org.spica.javaclient.Configuration;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.utils.DateUtil;

public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public final static void main (final String [] args) {

        DateUtil dateUtil = new DateUtil();

        System.err.close();
        System.setErr(System.out); //to avoid reflection warnings

        Configuration.getDefaultApiClient().setBasePath("http://localhost:8765/api"); //TODO make nice

        System.out.println ("SPICA-CLI \n\n");

        StandaloneActionContext actionContext = new StandaloneActionContext();

        EventInfo eventInfo = actionContext.getModelCache().findLastOpenEventFromToday();
        String workingOn = "";
        if (eventInfo != null) {
            if (eventInfo.getEventType().equals(EventType.PAUSE)) {
                workingOn = "Pause";
            }
            else if (eventInfo.getEventType().equals(EventType.TOPIC)) {
                TopicInfo topicInfoById = actionContext.getModelCache().findTopicInfoById(eventInfo.getReferenceId());
                workingOn = "Working on topic " + topicInfoById.getExternalSystemKey() + "-" + topicInfoById.getName() + "(" + topicInfoById.getId() + ")";
            } else {
                workingOn = eventInfo.getEventType().getValue();
            }

            System.out.println (workingOn + " ( since " + dateUtil.getTimeAsString(eventInfo.getStart()) + " )");
        }
        else
            System.out.println ("No current task found for today");

        System.out.println ("\n\n\n");

        ActionHandler actionHandler = new ActionHandler();
        if (args.length == 0) {
            System.out.println("\n\nActions:");
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

            action.beforeParam(actionContext, foundAction.getParameter());

            InputParams inputParams = action.getInputParams(actionContext, foundAction.getParameter());

            if (! inputParams.isEmpty()) {
                StandaloneActionParamFactory actionParamFactory = new StandaloneActionParamFactory();
                inputParams = actionParamFactory.build(actionContext, inputParams, foundAction);
            }

            action.execute(new StandaloneActionContext(), inputParams,foundAction.getParameter());
        }

    }

}
