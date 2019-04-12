package org.spica.javaclient.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.timetracker.TimetrackerService;

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
    public void execute(ActionContext actionContext, String parameterlist) {
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
