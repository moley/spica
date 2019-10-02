package org.spica.javaclient.actions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.params.InputParams;


public interface Action {

    final static Logger LOGGER = LoggerFactory.getLogger(Action.class);

    String getDisplayname ();

    String getDescription ();

    void execute (ActionContext actionContext, InputParams inputParams, String parameterList);

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

    default InputParams getInputParams(ActionContext actionContext, String parameterList) {
        LOGGER.info("default getInputParams implementation");
        return new InputParams();
    }

    default void beforeParam (ActionContext actionContext, String parameterList) {
        LOGGER.info("default beforeParam implementation");
    }


}
