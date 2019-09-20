package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.timetracker.TimetrackerService;

public class StartOrStopPauseAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartOrStopPauseAction.class);



    @Override
    public String getDisplayname() {
        return "Pause";
    }

    @Override
    public String getDescription() {
        return "Starts a pause / Stops a pause and starts working on the previous topic again";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterlist) {
        LOGGER.info("Start or stop pause called with parameter " + parameterlist);
        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getModelCacheService());

        outputOk(timetrackerService.togglePause());

        actionContext.saveModelCache();
    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("pause", "p");
    }

}
