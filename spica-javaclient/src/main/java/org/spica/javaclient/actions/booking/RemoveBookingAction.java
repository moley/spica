package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class RemoveBookingAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveBookingAction.class);

    @Override public String getDisplayname() {
        return "Remove bookings";
    }

    @Override
    public String getDescription() {
        return "Remove booking with id";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        ModelCache modelCache = actionContext.getModelCache();

        String id = commandLineArguments.getMandatoryFirstArgument("You have to add an parameter id to your command");

        EventInfo eventInfoRealById = modelCache.findEventInfoRealById(id);
        if (eventInfoRealById == null)
            throw new IllegalStateException("No event with id " + id + " found");

        modelCache.getEventInfosReal().remove(eventInfoRealById);

        outputOk("Removed booking with id " + eventInfoRealById);

        actionContext.saveModelCache(getClass().getName());

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
