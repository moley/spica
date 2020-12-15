package org.spica.javaclient.actions.booking;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.utils.DateUtil;

import java.time.LocalDate;

@Slf4j
public class ListBookingsAction extends AbstractAction {

    private DateUtil dateUtil = new DateUtil();

    @Override public String getDisplayname() {
        return "List booking";
    }


    @Override
    public String getDescription() {
        return "List all bookings of the day ( parameter may be a date or <week>, <week-1>... or <month>, <month-1>.... )";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        LocalDate from = LocalDate.now();
        LocalDate until = LocalDate.now();

        LocalDate today = LocalDate.now();

        String optionalFirstArgument = commandLineArguments.getOptionalMainArgument();

        if (optionalFirstArgument != null) {
            //TODO extract Date parser in own class
            if (optionalFirstArgument.trim().startsWith("all")) {
                from = LocalDate.of(2016, 1, 1);
                until = today;
            }
            else if (optionalFirstArgument.trim().startsWith("year")) {
                from = LocalDate.of(today.getYear(), 1, 1);
                until = today;
            } else if (optionalFirstArgument.trim().startsWith("week")) {
                String offset = optionalFirstArgument.trim().substring(4);
                from = today.minusDays(today.getDayOfWeek().getValue() - 1);
                until = from.plusDays(6);

                if (! offset.trim().isEmpty()) {
                    int offsetAsInt = Integer.parseInt(offset);
                    from = from.plusWeeks(offsetAsInt);
                    until = until.plusWeeks(offsetAsInt);
                }
            }
            else if (optionalFirstArgument.trim().startsWith("month")) {
                String offset = optionalFirstArgument.trim().substring(5);
                from = today.minusDays(today.getDayOfMonth() - 1);
                until = from.plusMonths(1).minusDays(1);

                if (! offset.trim().isEmpty()) {
                    int offsetAsInt = Integer.parseInt(offset);
                    from = from.plusMonths(offsetAsInt);
                    until = until.plusMonths(offsetAsInt);
                }

            }
            else if (optionalFirstArgument.length() == 4 || optionalFirstArgument.length() == 5) {
                LocalDate concreteDate = dateUtil.getDate(optionalFirstArgument);
                from = concreteDate;
                until = concreteDate;
            }
            else
                throw new IllegalArgumentException("Illegal format of parameter: " + optionalFirstArgument);


        }
        outputDefault("Bookings from " + dateUtil.getDateAsString(from) + " until " + dateUtil.getDateAsString(until) + ":\n\n");
        Model model = actionContext.getModel();
        for (EventInfo next: model.getEventInfosReal()) {

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

        return null;
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