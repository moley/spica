package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.Model;
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
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        String id = commandLineArguments.getMandatoryFirstArgument("You have to add an parameter id to your command");

        EventInfo eventInfoRealById = model.findEventInfoRealById(id);
        if (eventInfoRealById == null)
            throw new IllegalStateException("No event with id " + id + " found");

        model.getEventInfosReal().remove(eventInfoRealById);

        outputOk("Removed booking with id " + eventInfoRealById.getId());

        actionContext.saveModel(getClass().getName());

        return null;

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
