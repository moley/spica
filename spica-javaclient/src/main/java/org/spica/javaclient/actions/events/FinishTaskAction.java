package org.spica.javaclient.actions.events;

import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.events.EventService;

@Slf4j
public class FinishTaskAction extends AbstractAction {

  @Override public String getDescription() {
    return "Finish current task";
  }

  @Override public String getDisplayname() {
    return "Finish current task";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {
    Model model = actionContext.getModel();

    EventInfo eventInfo = model.findLastOpenEventFromToday();
    log.info("Found event " + eventInfo);
    if (eventInfo != null && eventInfo.getEventType().equals(EventType.TASK)) {
      log.info("Reference ID: " + eventInfo.getReferenceId());
      if (eventInfo.getReferenceId() != null) {
        //TODO

      }

      EventService eventService = actionContext.getServices().getEventService();
      eventService.finishEvent(eventInfo);

      outputDefault("Finished topic " + eventInfo.getName());

    }

    return null;

    //finish event

  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.BOOKING;
  }

  @Override public Command getCommand() {
    return new Command("finish", "f");
  }

}
