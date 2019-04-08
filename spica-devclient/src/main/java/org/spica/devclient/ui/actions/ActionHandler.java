package org.spica.devclient.ui.actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.devclient.DevApplicationInternal;

public class ActionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActionHandler.class);


    private ToolBar toolBar;

    public void addAction (final Action action) {
        Button btn = action.getButton();
        toolBar.getItems().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                action.execute();
            }
        });
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public void setToolBar(ToolBar toolBar) {
        this.toolBar = toolBar;
    }
}
