package org.spica.javaclient.actions.booking;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.api.TaskApi;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.timetracker.TimetrackerService;

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

      TimetrackerService timetrackerService = new TimetrackerService();
      timetrackerService.setModelCacheService(actionContext.getServices().getModelCacheService());
      timetrackerService.finishEvent(eventInfo);

      outputDefault("Finished topic " + eventInfo.getName());

      actionContext.saveModel(getClass().getName());

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
