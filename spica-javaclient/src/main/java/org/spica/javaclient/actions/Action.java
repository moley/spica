package org.spica.javaclient.actions;


public interface Action {


    boolean fromButton ();

    String getDisplayname ();

    void execute (ActionContext actionContext, String parameterList);

    ActionGroup getGroup ();

    Command getCommand ();
}
