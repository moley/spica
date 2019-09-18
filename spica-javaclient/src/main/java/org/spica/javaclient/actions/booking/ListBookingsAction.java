package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.utils.DateUtil;

import java.time.LocalDateTime;

public class ListBookingsAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(ListBookingsAction.class);

    private DateUtil dateUtil = new DateUtil();

    @Override
    public boolean fromButton() {
        return false;
    }

    @Override
    public String getDisplayname() {
        return "List bookings";
    }

    @Override
    public String getDescription() {
        return "List all bookings of the day";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        System.out.println ("Bookings " + dateUtil.getDateAsString(LocalDateTime.now()) + ":\n\n");
        ModelCache modelCache = actionContext.getModelCache();
        for (EventInfo next: modelCache.getEventInfosRealToday()) {
            String startAsString = dateUtil.getTimeAsString(next.getStart());
            String stopAsString = dateUtil.getTimeAsString(next.getStop());
            String nameNotNull = next.getName() != null ? next.getName() : "";
            String eventToken = String.format("     %-10s%-10s %-10s %-90s (%s)", startAsString, stopAsString, next.getEventType().name(), nameNotNull, next.getId());
            System.out.println (eventToken);
        }
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("list", "l");
    }


}