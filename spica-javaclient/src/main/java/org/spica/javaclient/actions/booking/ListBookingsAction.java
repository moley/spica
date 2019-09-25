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

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ListBookingsAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(ListBookingsAction.class);

    private DateUtil dateUtil = new DateUtil();

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

        LocalDate from = LocalDate.now();
        LocalDate until = LocalDate.now();

        LocalDate today = LocalDate.now();

        if (getOptionalFirstValue(parameterList) != null) {

            if (parameterList.trim().startsWith("week")) {
                String offset = parameterList.trim().substring(4);
                System.out.println ("offset: " + offset);
                from = today.minusDays(today.getDayOfWeek().getValue() - 1);
                until = from.plusDays(6);

                if (! offset.trim().isEmpty()) {
                    int offsetAsInt = Integer.parseInt(offset);
                    from = from.plusWeeks(offsetAsInt);
                    until = until.plusWeeks(offsetAsInt);
                }
            }
            else if (parameterList.trim().startsWith("month")) {
                String offset = parameterList.trim().substring(5);
                System.out.println ("offset: " + offset);
                from = today.minusDays(today.getDayOfMonth() - 1);
                until = from.plusMonths(1).minusDays(1);

                if (! offset.trim().isEmpty()) {
                    int offsetAsInt = Integer.parseInt(offset);
                    from = from.plusMonths(offsetAsInt);
                    until = until.plusMonths(offsetAsInt);
                }

            }
            else if (parameterList.length() == 4 || parameterList.length() == 5) {
                LocalDate concreteDate = dateUtil.getDate(parameterList);
                from = concreteDate;
                until = concreteDate;
            }
            else
                throw new IllegalArgumentException("Illegal format of parameter: " + parameterList);


        }
        outputDefault("Bookings from " + dateUtil.getDateAsString(from) + " until " + dateUtil.getDateAsString(until) + ":\n\n");
        ModelCache modelCache = actionContext.getModelCache();
        for (EventInfo next: modelCache.getEventInfosReal()) {

            if (next.getStart().toLocalDate().isBefore(from))
                continue;

            if (next.getStart().toLocalDate().isAfter(until))
                continue;

            String startAsString = dateUtil.getTimeAsString(next.getStart());
            String stopAsString = dateUtil.getTimeAsString(next.getStop());
            String dayAsString = dateUtil.getDateAsString(next.getStart());
            String nameNotNull = next.getName() != null ? next.getName() : "";
            String eventToken = String.format("     %-15s%-10s%-10s %-10s %-90s (%s)", dayAsString, startAsString, stopAsString, next.getEventType().name(), nameNotNull, next.getId());
            outputDefault(eventToken);
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