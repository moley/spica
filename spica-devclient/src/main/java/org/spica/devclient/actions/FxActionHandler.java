package org.spica.devclient.actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.FoundAction;

public class FxActionHandler {

    private ActionHandler actionHandler = new ActionHandler();

    public void createButtons (final ActionContext actionContext, final ToolBar toolBar) {
        for (Action action: actionHandler.getRegisteredActions()) {
            if (action.fromButton()) {
                Button btn = new Button(action.getDisplayname());
                toolBar.getItems().add(btn);
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        if (! action.getInputParams().isEmpty()) {
                            FxActionParamFactory actionParamFactory = new FxActionParamFactory();
                            FoundAction foundAction = new FoundAction(action, "");
                            actionParamFactory.build(actionContext, foundAction);
                        }

                        action.execute(actionContext, action.getInputParams(), "");
                    }
                });
            }

        }

    }
}
