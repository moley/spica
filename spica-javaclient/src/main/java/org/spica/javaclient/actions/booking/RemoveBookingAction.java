package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.utils.DateUtil;

public class RemoveBookingAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(RemoveBookingAction.class);

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

        ModelCache modelCache = actionContext.getModelCache();

        EventInfo eventInfoRealById = modelCache.findEventInfoRealById(parameterList);
        if (eventInfoRealById == null)
            throw new IllegalStateException("No event with id " + parameterList + " found");

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
