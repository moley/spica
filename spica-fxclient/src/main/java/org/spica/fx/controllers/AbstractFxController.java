package org.spica.fx.controllers;

import org.spica.javaclient.actions.ActionContext;

public class AbstractFxController {

  private ActionContext actionContext;

  public ActionContext getActionContext() {
    return actionContext;
  }

  public void setActionContext(ActionContext actionContext) {
    this.actionContext = actionContext;
  }

  public void setTiming () {

  }

}
