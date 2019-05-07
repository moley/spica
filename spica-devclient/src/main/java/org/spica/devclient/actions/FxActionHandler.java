package org.spica.devclient.actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import org.spica.devclient.util.UiUtils;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.FoundAction;

public class FxActionHandler {

    private ActionHandler actionHandler = new ActionHandler();

    public void createButtons (final FxActionContext actionContext, final ToolBar toolBar, final TabPane tabPane) {
        for (Action action: actionHandler.getRegisteredActions()) {
            if (action.fromButton()) {
                Button btn = new Button();
                btn.setTooltip(new Tooltip(action.getDisplayname()));
                btn.setGraphic(UiUtils.getIcon(action.getIcon()));
                actionContext.registerButton(action.getClass(), btn);

                toolBar.getItems().add(btn);
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        action.beforeParam(actionContext, "");

                        if (! action.getInputParams(actionContext).isEmpty()) {
                            FxActionParamFactory actionParamFactory = new FxActionParamFactory(tabPane);
                            FoundAction foundAction = new FoundAction(action, "");
                            actionParamFactory.build(actionContext, foundAction);
                        }
                        else
                          action.execute(actionContext, action.getInputParams(actionContext), "");
                    }
                });
            }

        }

    }
}
