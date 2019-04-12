package org.spica.devclient.actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionHandler;

public class FxActionHandler {

    private ActionHandler actionHandler = new ActionHandler();

    public void createButtons (final ActionContext actionContext, final ToolBar toolBar) {
        for (Action next: actionHandler.getRegisteredActions()) {
            if (next.fromButton()) {
                Button btn = new Button(next.getDisplayname());
                toolBar.getItems().add(btn);
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        next.execute(actionContext, "");
                    }
                });
            }

        }

    }
}
