package org.spica.devclient.ui.actions;

import javafx.scene.control.Button;
import org.spica.devclient.timetracker.TimetrackerService;

public class StartOrStopPauseAction implements Action {

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
    public void execute() {
        btnPause.setText(timetrackerService.togglePause());
    }

    public TimetrackerService getTimetrackerService() {
        return timetrackerService;
    }

    public void setTimetrackerService(TimetrackerService timetrackerService) {
        this.timetrackerService = timetrackerService;
    }
}
