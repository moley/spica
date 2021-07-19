package org.spica.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.StandaloneActionContext;
import org.spica.cli.actions.StandaloneActionParamFactory;
import org.spica.javaclient.Configuration;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.auth.HttpBasicAuth;
import org.spica.javaclient.event.EventDetails;
import org.spica.javaclient.event.EventDetailsBuilder;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.TaskInfo;
import org.spica.commons.DateUtils;
import org.spica.javaclient.utils.LogUtil;
import org.spica.javaclient.utils.RenderUtil;

@Slf4j
public class Main {

  public final static void main(final String[] args) throws IOException {

    log.info("Started cli application with parameters " + Arrays.asList(args));

    DateUtils dateUtils = new DateUtils();

    System.err.close();
    System.setErr(System.out); //to avoid reflection warnings

    //read configuration from gradle properties (proxy...)
    File gradlePropertiesFile = new File (System.getProperty("user.home"), ".gradle/gradle.properties");
    if (gradlePropertiesFile.exists()) {
      Properties gradleProperties = new Properties();
      gradleProperties.load(new FileInputStream(gradlePropertiesFile));
      for (String propName: gradleProperties.stringPropertyNames()) {
        if (propName.startsWith("systemProp")) {
          String value = gradleProperties.getProperty(propName);
          log.info("Using " + propName + "=" + value);
          System.setProperty(propName.replace("systemProp.", ""), value);
        }
      }
    }

    StandaloneActionContext actionContext = new StandaloneActionContext();
    String serverUrl = actionContext.getProperties().getValueOrDefault("spica.cli.serverurl", "http://localhost:8765/api");
    Configuration.getDefaultApiClient().setBasePath(serverUrl);

    String username = actionContext.getProperties().getValueNotNull("spica.cli.username");
    String password = actionContext.getProperties().getValueNotNull("spica.cli.password");
    HttpBasicAuth httpBasicAuth = (HttpBasicAuth) Configuration.getDefaultApiClient().getAuthentication("basicAuth");
    if (httpBasicAuth != null) {
      httpBasicAuth.setUsername(username);
      httpBasicAuth.setPassword(password);
    }

    Configuration.getDefaultApiClient().setUsername(username);
    Configuration.getDefaultApiClient().setPassword(password);

    actionContext.setActionHandler(new ActionHandler());
    actionContext.setActionParamFactory(new StandaloneActionParamFactory());

    EventInfo firstTaskOfDay = !actionContext.getModel().getEventInfosRealToday().isEmpty() ?
        actionContext.getModel().getEventInfosRealToday().get(0) :
        null;

    EventDetailsBuilder eventDetailsBuilder = new EventDetailsBuilder();
    eventDetailsBuilder.setModel(actionContext.getModel());
    EventDetails eventDetails = eventDetailsBuilder.getDurationDetails();

    EventInfo eventInfo = actionContext.getModel().findLastOpenEventFromToday();

    String task = LogUtil.cyan("No current task found for today");
    String since = "";
    if (eventInfo != null) {

      if (eventInfo.getEventType() != null) {
        if (eventInfo.getEventType().equals(EventType.PAUSE)) {
          task = "Pause";
        } else if (eventInfo.getEventType().equals(EventType.TASK)) {
          TaskInfo topicInfoById = actionContext.getModel().findTaskInfoById(eventInfo.getReferenceId());
          RenderUtil renderUtil = new RenderUtil();
          task = renderUtil.getTask(topicInfoById);
        } else if (eventInfo.getEventType().equals(EventType.MESSAGE)) {
          task = "Message " + eventInfo.getName();
        } else {
          task = eventInfo.getEventType().getValue();
        }
      }
      else
        task = "Invalid tasktype (" + eventInfo.getId() +  ")";

      since = " ( since " + dateUtils.getTimeAsString(eventInfo.getStart()) + " )";
    }

    //System.out.println (LogUtil.clearScreen());

    System.out.println("Current time:         " + LogUtil.cyan(dateUtils.getTimeAsString(LocalDateTime.now())));
    System.out.println("Working since:        " + LogUtil
        .cyan(firstTaskOfDay != null ? dateUtils.getTimeAsString(firstTaskOfDay.getStart()) : ""));
    System.out.println("Cumulated work time:  " + LogUtil.cyan(dateUtils.getDuration(eventDetails.getDurationWork())));
    System.out.println("Cumulated pause time: " + LogUtil.cyan(dateUtils.getDuration(eventDetails.getDurationPause())));
    System.out.println("Current task:         " + LogUtil.cyan(task) + since);

    List<EventInfo> oldUnfinishedEvents = actionContext.getModel().findOldOpenEvents();
    if (!oldUnfinishedEvents.isEmpty()) {
      System.out.println(LogUtil
          .yellow(oldUnfinishedEvents.size() + " open events found before today, finish them with command b close"));
    }

    System.out.println("\n\n");

    ActionHandler actionHandler = actionContext.getActionHandler();

    if (args.length == 0) { //if no params are added
      System.out.println("Type 's' to show all actiongroups, 's [ACTIONGROUP]' to show all commands of a specific actiongroup or 's c state' to show details about your configuration");
      System.out.println("Usage: s (ACTIONGROUP) (COMMAND) [PARAMETER1..n]\n");
      System.out.println("Available action groups:\n");
      for (ActionGroup nextGroup : ActionGroup.values()) {
        System.out.println("   " + LogUtil.green(nextGroup.getShortkey() + " - " + nextGroup.name()));
      }
      System.out.println("\n\nFurther help on an action group you get with 's [ACTIONGROUP] help\n");
    } else {
      String parameter = String.join(" ", args);

      FoundAction foundAction = actionHandler.findAction(parameter);

      //if no action found we choose the help action for the given action group
      if (foundAction == null || args.length == 1) {
        ActionGroup actionGroup = ActionGroup.findByShortKey(args[0].trim());
        foundAction = actionHandler.findAction(actionGroup, ActionHandler.HELP_TASKNAME);
      }

      while (foundAction != null) {
        ActionResult actionResult = actionHandler.handleAction(actionContext, foundAction);

        if (actionResult != null)
          foundAction = actionResult.getFollowUpAction();
        else
          foundAction = null;
      }

    }

  }

}
