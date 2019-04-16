package org.spica.cli.actions;

import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.params.ActionParamFactory;
import org.spica.javaclient.actions.params.InputParam;
import org.spica.javaclient.actions.params.InputParamGroup;

public class StandaloneActionParamFactory implements ActionParamFactory {
    @Override
    public void build(ActionContext actionContext, FoundAction foundAction) {

        for (InputParamGroup nextGroup: foundAction.getAction().getInputParams()) {
            for (InputParam nextInputParam: nextGroup.getInputParams()) {
                System.out.println ("Build " + nextInputParam.getDisplayname() + "-" + nextInputParam.getKey() + "-" + nextInputParam.getValue());
            }
        }

    }
}
