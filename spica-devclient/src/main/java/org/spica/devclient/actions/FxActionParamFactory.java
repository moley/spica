package org.spica.devclient.actions;

import org.spica.devclient.ui.controller.ActionParamController;
import org.spica.devclient.util.Mask;
import org.spica.devclient.util.MaskLoader;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.params.ActionParamFactory;

public class FxActionParamFactory implements ActionParamFactory {


    @Override
    public void build(ActionContext actionContext, FoundAction foundAction) {

        MaskLoader<ActionParamController> actionParamControllerMaskLoader = new MaskLoader<ActionParamController>();
        Mask<ActionParamController> actionsparamMask = actionParamControllerMaskLoader.load("actionparams");
        ActionParamController actionParamController = actionsparamMask.getController();
        actionParamController.setActionContext(actionContext);
        actionParamController.setFoundAction(foundAction);
        actionParamController.setStage(actionsparamMask.getStage());
        actionsparamMask.show();



    }
}
