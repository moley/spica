package org.spica.javaclient.actions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.params.InputParams;



public interface Action {

    final static Logger LOGGER = LoggerFactory.getLogger(Action.class);



    default String getIcon () {
        return null;
    }

    boolean fromButton ();

    String getDisplayname ();

    String getDescription ();

    void execute (ActionContext actionContext, InputParams inputParams, String parameterList);

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
