package org.spica.javaclient.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public interface Action {

    final static Logger LOGGER = LoggerFactory.getLogger(Action.class);

    String getDisplayname ();


    String getDescription ();

    void execute (ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLine);

    default String getFirstValue (final String ... values) {

        String firstValue = getOptionalFirstValue(values);
        if (firstValue == null)
          throw new IllegalArgumentException("No value of " + values + " was valid");
        return firstValue;
    }

    default String getOptionalFirstValue (final String ... values) {
        for (String next: values) {
            if (next != null && ! next.trim().isBlank())
                return next;
        }
        return null;
    }

    ActionGroup getGroup ();

    Command getCommand ();

    default InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {
        LOGGER.info("default getInputParams implementation");
        return new InputParams();
    }



}
