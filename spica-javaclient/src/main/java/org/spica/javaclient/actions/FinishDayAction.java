package org.spica.javaclient.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.params.InputParamGroup;
import org.spica.javaclient.timetracker.TimetrackerService;

import java.util.ArrayList;
import java.util.List;

public class FinishDayAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(FinishDayAction.class);


    public final static String DISPLAY_NAME = "Finish day";



    private TimetrackerService timetrackerService = new TimetrackerService();


    @Override
    public boolean fromButton() {
        return true;
    }

    @Override
    public String getDisplayname() {
        return DISPLAY_NAME;
    }

    @Override
    public String getDescription() {
        return "Finished the timetracker and closes the day";
    }

    @Override
    public void execute(ActionContext actionContext, List<InputParamGroup> inputParamGroups, String parameterlist) {
        LOGGER.info("Finish day called with parameter " + parameterlist);
        timetrackerService.stopWork();
    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("end", "e");
    }




}
