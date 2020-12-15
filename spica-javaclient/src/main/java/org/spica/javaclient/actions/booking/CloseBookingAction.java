package org.spica.javaclient.actions.booking;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;
import org.spica.javaclient.utils.DateUtil;

@Slf4j
public class CloseBookingAction extends AbstractAction {

  private DateUtil dateUtil = new DateUtil();

  public final static String KEY_STOPTIME = "stoptime";
  public final static String KEY_STOPOLD = "stopold";

  @Override public String getDisplayname() {
    return "Close booking";
  }

  @Override public String getDescription() {
    return "Close booking with id";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {
    Model model = actionContext.getModel();

    //Handle old closed
    int index = 0;
    for (EventInfo next : actionContext.getModel().findOldOpenEvents()) {
      LocalTime stopTime = dateUtil.getTime(inputParams.getInputValueAsString(KEY_STOPOLD + "_" + index));
      outputOk("Stopping old booking (" + next.getId() + " - " + next.getName() + ") started at " + next
          .getStart() + " at " + stopTime);
      LocalDateTime stopDateTime = LocalDateTime.of(next.getStart().toLocalDate(), stopTime);
      next.setStop(stopDateTime);
      actionContext.saveModel(getClass().getName());
      index++;
    }
    actionContext.saveModel(getClass().getName());


    //Handle new closing
    String query = commandLineArguments.getOptionalMainArgument();
    EventInfo eventInfoRealById = query != null ? model.findEventInfoRealById(query) : model.findLastOpenEventFromToday();
    if (eventInfoRealById == null) {
      outputDefault((query != null ? "No event with id " + query : "No last open event from today") + " found");
    } else {

      String newClosingValue = inputParams.getInputValueAsString(KEY_STOPTIME);
      if (newClosingValue != null) {
        LocalTime stopTime = dateUtil.getTime(newClosingValue);

        LocalDateTime stopDateTime = LocalDateTime.of(eventInfoRealById.getStart().toLocalDate(), stopTime);
        outputOk("Saved booking " + eventInfoRealById.getId() + " with stop time " + dateUtil.getDateAndTimeAsString(stopDateTime));
        eventInfoRealById.setStop(stopDateTime);

        actionContext.saveModel(getClass().getName());
      }
    }
    return null;
  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.BOOKING;
  }

  @Override public Command getCommand() {
    return new Command("close", "o");
  }

  private String getInfoFromEvent(final EventInfo eventInfo) {
    return eventInfo.getName() + " started at " + eventInfo.getStart();
  }

  @Override public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {

    InputParamGroup inputParamGroup = new InputParamGroup();

    int index = 0;
    for (EventInfo next : actionContext.getModel().findOldOpenEvents()) {
      TextInputParam stopped = new TextInputParam(1, KEY_STOPOLD + "_" + index,
          "Stop old event " + getInfoFromEvent(next));
      inputParamGroup.getInputParams().add(stopped);
      index++;
    }

    EventInfo lastEventInfoToday = actionContext.getModel().findLastOpenEventFromToday();
    if (lastEventInfoToday != null) {
      TextInputParam description = new TextInputParam(5, KEY_STOPTIME,
          "Stop last event " + getInfoFromEvent(lastEventInfoToday) + " (CR to skip)");
      inputParamGroup.getInputParams().add(description);
    }

    return new InputParams(Arrays.asList(inputParamGroup));
  }
}
