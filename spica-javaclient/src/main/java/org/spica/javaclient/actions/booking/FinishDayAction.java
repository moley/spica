package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.timetracker.TimetrackerService;
import org.spica.javaclient.utils.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FinishDayAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(FinishDayAction.class);

    private DateUtil dateUtil = new DateUtil();

    public final static String DISPLAY_NAME = "Finish day";

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

        LocalDateTime stopTime = LocalDateTime.now();
        if (parameterlist != null && ! parameterlist.trim().isEmpty()) {
            LocalTime localTime = dateUtil.getTime(parameterlist);
            stopTime = LocalDateTime.of(LocalDate.now(), localTime);
        }
        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getModelCacheService());
        timetrackerService.finishDay(stopTime);
        outputOk("Finished day at " + dateUtil.getTimeAsString(stopTime));
    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("day", "d");
    }




}
