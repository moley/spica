package org.spica.javaclient.actions;

import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public interface Action {

    String getDisplayname ();


    String getDescription ();

    /**
     * execute an action
     *
     * @param actionContext     context
     * @param inputParams       input params
     * @param commandLine       command line args
     * @return <code>null</code> or the follow up action if you want to chain actions
     */
    ActionResult execute (ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLine);

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
        return new InputParams();
    }



}
