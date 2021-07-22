package org.spica.javaclient.actions.events;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.ConfirmInputParam;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.events.EventService;
import org.spica.commons.DateUtils;

@Slf4j
public class FinishDayAction extends AbstractAction {


    private DateUtils dateUtils = new DateUtils();

    public final static String DISPLAY_NAME = "Finish day";
    public final static String KEY_CONFIRM_LATER = "CONFIRM_LATER";

    @Override public String getDisplayname() {
        return "Finish day";
    }

    @Override
    public String getDescription() {
        return "Finished the timetracker and closes the day (parameter can be stop time)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
        log.info("Finish day called with parameter " + commandLineArguments);

        LocalDate lastOpenEventDate = getDateLastOpenEvent(actionContext);
        if (lastOpenEventDate.equals(LocalDate.now()) || inputParams.getInputValueAsBoolean(KEY_CONFIRM_LATER, Boolean.FALSE)) {

            if (inputParams.getInputValueAsBoolean(KEY_CONFIRM_LATER, Boolean.FALSE) && commandLineArguments.isEmpty()) {
                outputError("If you want to finish a previous day you have to add the stop time as parameter");
            }
            else {
                LocalDateTime stopTime = LocalDateTime.now();
                if (! commandLineArguments.isEmpty()) {
                    LocalTime localTime = dateUtils.getTime(commandLineArguments.getSingleMainArgument());
                    stopTime = LocalDateTime.of(LocalDate.now(), localTime);
                }
                EventService eventService = actionContext.getServices().getEventService();
                eventService.finishDay(stopTime);
                outputOk("Finished day at " + dateUtils.getTimeAsString(stopTime));
            }
        }

        return null;
    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("day", "d");
    }

    private LocalDate getDateLastOpenEvent (ActionContext actionContext) {
        Model model = actionContext.getModel();
        EventInfo lastOpenEvent = model.findLastOpenEventFromToday();
        return lastOpenEvent.getStart().toLocalDate();
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {


        InputParams inputParams = new InputParams();

        LocalDate lastOpenEventDate = getDateLastOpenEvent(actionContext);

        if (lastOpenEventDate.isBefore(LocalDate.now())) {
            InputParamGroup lastOpenBookingNotFromToday = new InputParamGroup();
            inputParams.getInputParamGroups().add(lastOpenBookingNotFromToday);
            String day = dateUtils.getDateAsString(lastOpenEventDate);
            ConfirmInputParam confirmInputParam = new ConfirmInputParam(KEY_CONFIRM_LATER, "An open event was found from " + day + ". Do you want to finish this day?", "Y");
            InputParamGroup inputParamGroupTopic = new InputParamGroup();
            inputParamGroupTopic.getInputParams().add(confirmInputParam);
            inputParams.getInputParamGroups().add(inputParamGroupTopic);

        }

        return inputParams;
    }
}
