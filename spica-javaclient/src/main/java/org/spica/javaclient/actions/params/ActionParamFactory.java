package org.spica.javaclient.actions.params;

import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.FoundAction;

public interface ActionParamFactory {

    void build(ActionContext actionContext, FoundAction foundAction);
}
