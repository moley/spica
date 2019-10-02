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
import org.spica.javaclient.event.EventDetails;
import org.spica.javaclient.event.EventDetailsBuilder;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.ModelCacheService;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.utils.DateUtil;
import org.spica.javaclient.utils.LogUtil;
import org.spica.javaclient.utils.RenderUtil;

import java.time.LocalDateTime;

public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public final static void main (final String [] args) {

        DateUtil dateUtil = new DateUtil();

        System.err.close();
        System.setErr(System.out); //to avoid reflection warnings


        Configuration.getDefaultApiClient().setBasePath("http://localhost:8765/api"); //TODO make nice

        StandaloneActionContext actionContext = new StandaloneActionContext();

        EventInfo firstTaskOfDay = ! actionContext.getModelCache().getEventInfosRealToday().isEmpty() ? actionContext.getModelCache().getEventInfosRealToday().get(0):null;

        EventDetailsBuilder eventDetailsBuilder = new EventDetailsBuilder();
        eventDetailsBuilder.setModelCache(actionContext.getModelCache());
        EventDetails eventDetails = eventDetailsBuilder.getDurationDetails();


        EventInfo eventInfo = actionContext.getModelCache().findLastOpenEventFromToday();

        String task = LogUtil.cyan("No current task found for today");
        String since = "";
        if (eventInfo != null) {
            if (eventInfo.getEventType().equals(EventType.PAUSE)) {
                task = "Pause";
            }
            else if (eventInfo.getEventType().equals(EventType.TOPIC)) {
                TopicInfo topicInfoById = actionContext.getModelCache().findTopicInfoById(eventInfo.getReferenceId());
                RenderUtil renderUtil = new RenderUtil();
                task = "Topic " + renderUtil.getTopic(topicInfoById);
            } else {
                task = eventInfo.getEventType().getValue();
            }

            since = " ( since " + dateUtil.getTimeAsString(eventInfo.getStart()) + " )";
        }

        //System.out.println (LogUtil.clearScreen());
        System.out.println ("Current time:         " + LogUtil.cyan(dateUtil.getTimeAsString(LocalDateTime.now())));
        System.out.println ("Working since:        " + LogUtil.cyan(firstTaskOfDay != null ? dateUtil.getTimeAsString(firstTaskOfDay.getStart()) : ""));
        System.out.println ("Cumulated work time:  " + LogUtil.cyan(dateUtil.getDuration(eventDetails.getDurationWork())));
        System.out.println ("Cumulated pause time: " + LogUtil.cyan(dateUtil.getDuration(eventDetails.getDurationPause())));
        System.out.println ("Current task:         " + LogUtil.cyan(task)  + since + "\n\n");

        ActionHandler actionHandler = new ActionHandler();
        if (args.length == 0) {
            for (String next: actionHandler.getHelp()) {
                System.out.println (next);
            }
            System.out.println("\n");
        }
        else {


            String parameter = String.join(" ", args);
            FoundAction foundAction = actionHandler.findAction(parameter);

            if (foundAction == null) {
                System.out.println (LogUtil.red("No command found for <" + parameter + ">"));

                for (String next: actionHandler.getHelp()) {
                    System.out.println (next);
                }
                System.out.println("\n");

            } else {
                String parameterAddon = foundAction.getParameter().trim().isEmpty() ? "" : " ( " + foundAction.getParameter().trim() + " )";
                System.out.println(LogUtil.green(foundAction.getAction().getDisplayname().toUpperCase() + parameterAddon) + "\n\n");
                LOGGER.info("Found action     : " + foundAction.getAction().getClass().getName());
                LOGGER.info("with parameter   : " + foundAction.getParameter());

                Action action = foundAction.getAction();

                action.beforeParam(actionContext, foundAction.getParameter());

                InputParams inputParams = action.getInputParams(actionContext, foundAction.getParameter());

                if (!inputParams.isEmpty()) {
                    StandaloneActionParamFactory actionParamFactory = new StandaloneActionParamFactory();
                    inputParams = actionParamFactory.build(actionContext, inputParams, foundAction);
                }

                action.execute(new StandaloneActionContext(), inputParams, foundAction.getParameter());
            }
        }

    }

}
