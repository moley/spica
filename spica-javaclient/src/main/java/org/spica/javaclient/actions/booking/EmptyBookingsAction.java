package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.EventInfo;

import java.util.List;

public class EmptyBookingsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmptyBookingsAction.class);

    @Override public String getDisplayname() {
        return "Empty booking";
    }

    @Override
    public String getDescription() {
        return "Empty all bookings of the day";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        List<EventInfo> eventInfosRealToday = model.getEventInfosRealToday();
        model.getEventInfosReal().removeAll(eventInfosRealToday);

        outputOk("Removed all events from today (" + eventInfosRealToday.size() + ")");

        actionContext.saveModel(getClass().getName());
        return null;

    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("empty", "e");
    }

}
