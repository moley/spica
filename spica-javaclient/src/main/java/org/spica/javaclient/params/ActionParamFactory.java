package org.spica.javaclient.params;

import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.FoundAction;

public interface ActionParamFactory {

    InputParams build(ActionContext actionContext, InputParams inputParams);
}
