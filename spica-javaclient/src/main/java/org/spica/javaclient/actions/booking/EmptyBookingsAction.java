package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class EmptyBookingsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmptyBookingsAction.class);

    @Override
    public String getDisplayname() {
        return "Empty bookings";
    }

    @Override
    public String getDescription() {
        return "Empty all bookings of the day";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        ModelCache modelCache = actionContext.getModelCache();

        List<EventInfo> eventInfosRealToday = modelCache.getEventInfosRealToday();
        modelCache.getEventInfosReal().removeAll(eventInfosRealToday);

        outputOk("Removed all events from today (" + eventInfosRealToday.size() + ")");

        actionContext.saveModelCache(getClass().getName());

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
