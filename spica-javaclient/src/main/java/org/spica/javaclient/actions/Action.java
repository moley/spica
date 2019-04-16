package org.spica.javaclient.actions;


import org.spica.javaclient.actions.params.InputParams;

public interface Action {


    boolean fromButton ();

    String getDisplayname ();

    String getDescription ();

    void execute (ActionContext actionContext, InputParams inputParams, String parameterList);

    ActionGroup getGroup ();

    Command getCommand ();

    default InputParams getInputParams() {
        return new InputParams();
    }
}
