package org.spica.devclient.ui.actions;

import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.devclient.timetracker.TimetrackerService;

public class StartOrStopPauseAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartOrStopPauseAction.class);


    private Button btnPause = new Button();

    private TimetrackerService timetrackerService;

    public StartOrStopPauseAction () {
        btnPause.setText(TimetrackerService.DISPLAY_START_PAUSE);
    }

    @Override
    public Button getButton() {
        return btnPause;
    }

    @Override
    public String getDisplayname() {
        return "Pause";
    }

    @Override
    public void execute(String parameterlist) {
        LOGGER.info("Start or stop pause called with parameter " + parameterlist);
        btnPause.setText(timetrackerService.togglePause());
    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("pause", "p");
    }

    public TimetrackerService getTimetrackerService() {
        return timetrackerService;
    }

    public void setTimetrackerService(TimetrackerService timetrackerService) {
        this.timetrackerService = timetrackerService;
    }
}
