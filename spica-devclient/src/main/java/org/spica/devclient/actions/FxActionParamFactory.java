package org.spica.devclient.actions;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.spica.devclient.ui.controller.ActionParamController;
import org.spica.devclient.util.Mask;
import org.spica.devclient.util.MaskLoader;
import org.spica.devclient.util.UiUtils;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.params.ActionParamFactory;

public class FxActionParamFactory implements ActionParamFactory {

    private TabPane tabPane;

    public FxActionParamFactory (final TabPane tabPane) {
        this.tabPane = tabPane;

    }


    @Override
    public void build(ActionContext actionContext, FoundAction foundAction) {

        MaskLoader<ActionParamController> actionParamControllerMaskLoader = new MaskLoader<ActionParamController>();
        Mask<ActionParamController> actionsparamMask = actionParamControllerMaskLoader.load("actionparams");
        ActionParamController actionParamController = actionsparamMask.getController();
        actionParamController.setActionContext(actionContext);
        actionParamController.setFoundAction(foundAction);

        Tab tab = new Tab(foundAction.getAction().getDisplayname(), actionsparamMask.getNode());
        actionParamController.setTab(tab);

        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        UiUtils.getFirstEditableNode(actionsparamMask.getNode()).requestFocus();
    }
}
