package org.spica.devclient.ui.actions;

import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.devclient.timetracker.TimetrackerService;

public class FinishDayAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(FinishDayAction.class);


    public final static String DISPLAY_NAME = "Finish day";

    private Button btnFinishDay = new Button();

    private TimetrackerService timetrackerService;


    public FinishDayAction () {
        btnFinishDay.setText(DISPLAY_NAME);
    }
    @Override
    public Button getButton() {
        return btnFinishDay;
    }

    @Override
    public String getDisplayname() {
        return DISPLAY_NAME;
    }

    @Override
    public void execute() {
        timetrackerService.stopWork();
    }

    public TimetrackerService getTimetrackerService() {
        return timetrackerService;
    }

    public void setTimetrackerService(TimetrackerService timetrackerService) {
        this.timetrackerService = timetrackerService;
    }
}
