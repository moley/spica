package org.spica.javaclient.actions;


import org.spica.javaclient.actions.params.InputParamGroup;

import java.util.ArrayList;
import java.util.List;

public interface Action {


    boolean fromButton ();

    String getDisplayname ();

    String getDescription ();

    void execute (ActionContext actionContext, List<InputParamGroup> inputParamGroups, String parameterList);

    ActionGroup getGroup ();

    Command getCommand ();

    default List<InputParamGroup> getInputParams() {
        return new ArrayList<>();
    }
}
