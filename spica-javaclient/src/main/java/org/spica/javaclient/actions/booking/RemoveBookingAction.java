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

public class RemoveBookingAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveBookingAction.class);

    private DateUtil dateUtil = new DateUtil();

    @Override
    public boolean fromButton() {
        return false;
    }

    @Override
    public String getDisplayname() {
        return "Remove booking";
    }

    @Override
    public String getDescription() {
        return "Remove booking with id";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        if (parameterList.strip().isBlank())
            throw new IllegalStateException("You have to add an id to your command");

        System.out.println ("Remove bookings with id " + parameterList);
        ModelCache modelCache = actionContext.getModelCache();

        EventInfo eventInfoRealById = modelCache.findEventInfoRealById(parameterList);
        if (eventInfoRealById == null)
            throw new IllegalStateException("No event with id " + parameterList + " found");

        modelCache.getEventInfosReal().remove(eventInfoRealById);
        actionContext.saveModelCache();

    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("remove", "r");
    }


}