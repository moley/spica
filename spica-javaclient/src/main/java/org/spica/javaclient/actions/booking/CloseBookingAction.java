package org.spica.javaclient.actions.booking;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParamGroup;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.actions.params.TextInputParam;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.utils.DateUtil;

public class CloseBookingAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(CloseBookingAction.class);

    private DateUtil dateUtil = new DateUtil();


    public final static String KEY_STOPTIME = "stoptime";


    @Override
    public String getDisplayname() {
        return "Close booking";
    }

    @Override
    public String getDescription() {
        return "Close booking with id";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {
        LocalTime stopTime = dateUtil.getTime(inputParams.getInputParamAsString(KEY_STOPTIME));

        ModelCache modelCache = actionContext.getModelCache();

        EventInfo eventInfoRealById = modelCache.findEventInfoRealById(parameterList);

        LocalDateTime stopDateTime = LocalDateTime.of(eventInfoRealById.getStart().toLocalDate(), stopTime);
        outputOk("Saved booking " + eventInfoRealById.getId() + " with stop time " + dateUtil.getDateAndTimeAsString(stopDateTime));
        eventInfoRealById.setStop(stopDateTime);

        actionContext.saveModelCache(getClass().getName());
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("close", "o");
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, String paramList) {
        if (paramList.strip().isBlank())
            throw new IllegalStateException("You have to add an id to your command");

        ModelCache modelCache = actionContext.getModelCache();

        EventInfo eventInfoRealById = modelCache.findEventInfoRealById(paramList);
        if (eventInfoRealById == null)
            throw new IllegalStateException("No event with id " + paramList + " found");

        TextInputParam description = new TextInputParam(5, KEY_STOPTIME, "Stopped at", "");

        InputParamGroup inputParamGroup = new InputParamGroup();
        inputParamGroup.getInputParams().add(description);

        return new InputParams(Arrays.asList(inputParamGroup));

    }
}
