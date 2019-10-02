package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.ConfirmInputParam;
import org.spica.javaclient.actions.params.InputParamGroup;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.timetracker.TimetrackerService;
import org.spica.javaclient.utils.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FinishDayAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(FinishDayAction.class);

    private DateUtil dateUtil = new DateUtil();

    public final static String DISPLAY_NAME = "Finish day";
    public final static String KEY_CONFIRM_LATER = "CONFIRM_LATER";

    @Override
    public String getDisplayname() {
        return DISPLAY_NAME;
    }

    @Override
    public String getDescription() {
        return "Finished the timetracker and closes the day (parameter can be stop time)";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterlist) {
        LOGGER.info("Finish day called with parameter " + parameterlist);



        LocalDate lastOpenEventDate = getDateLastOpenEvent(actionContext);
        if (lastOpenEventDate.equals(LocalDate.now()) || inputParams.getInputParamAsBoolean(KEY_CONFIRM_LATER, Boolean.FALSE)) {

            if (inputParams.getInputParamAsBoolean(KEY_CONFIRM_LATER, Boolean.FALSE) && parameterlist.isEmpty()) {
                outputError("If you want to finish a previous day you have to add the stop time as parameter");
            }
            else {
                LocalDateTime stopTime = LocalDateTime.now();
                if (parameterlist != null && !parameterlist.trim().isEmpty()) {
                    LocalTime localTime = dateUtil.getTime(parameterlist);
                    stopTime = LocalDateTime.of(LocalDate.now(), localTime);
                }
                TimetrackerService timetrackerService = new TimetrackerService();
                timetrackerService.setModelCacheService(actionContext.getModelCacheService());
                timetrackerService.finishDay(stopTime);
                outputOk("Finished day at " + dateUtil.getTimeAsString(stopTime));
            }
        }
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
        ModelCache modelCache = actionContext.getModelCache();
        EventInfo lastOpenEvent = modelCache.findLastOpenEvent();
        return lastOpenEvent.getStart().toLocalDate();
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, String parameterList) {


        InputParams inputParams = new InputParams();

        LocalDate lastOpenEventDate = getDateLastOpenEvent(actionContext);

        if (lastOpenEventDate.isBefore(LocalDate.now())) {
            InputParamGroup lastOpenBookingNotFromToday = new InputParamGroup();
            inputParams.getInputParamGroups().add(lastOpenBookingNotFromToday);
            String day = dateUtil.getDateAsString(lastOpenEventDate);
            ConfirmInputParam confirmInputParam = new ConfirmInputParam(KEY_CONFIRM_LATER, "An open event was found from " + day + ". Do you want to finish this day?", "Y");
            InputParamGroup inputParamGroupTopic = new InputParamGroup();
            inputParamGroupTopic.getInputParams().add(confirmInputParam);
            inputParams.getInputParamGroups().add(inputParamGroupTopic);

        }

        return inputParams;
    }
}
