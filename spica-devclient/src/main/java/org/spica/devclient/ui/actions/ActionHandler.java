package org.spica.devclient.ui.actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ActionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActionHandler.class);

    private Collection<Action> registeredActions = new ArrayList<Action>();


    private ToolBar toolBar;

    public Button registerAction(final Action action) {
        registeredActions.add(action);
        Button btn = action.getButton();
        if (btn != null) {
            toolBar.getItems().add(btn);
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    action.execute("");
                }
            });
        }

        return btn;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public void setToolBar(ToolBar toolBar) {
        this.toolBar = toolBar;
    }

    public FoundAction findAction (final String text) {
        String [] tokens = text.split(" ");
        String group = tokens[0];
        String command = tokens [1];
        String [] params = Arrays.copyOfRange(tokens, 2, tokens.length);
        String paramstring = String.join(" ", params);

        Collection<String> foundCommandTokens = new ArrayList<String>();

        if (tokens.length < 2)
            throw new IllegalStateException("No valid command line. Use '[ACTIONGROUP] [COMMAND] [PARAM1...n]");
        for (Action next: registeredActions) {

            foundCommandTokens.add(next.getGroup().name() + "(" + next.getGroup().getShortkey() + ") - " + next.getCommand().getCommand() + "(" + next.getCommand().getShortkey());
            if (next.getGroup().name().equalsIgnoreCase(group) || next.getGroup().getShortkey().equalsIgnoreCase(group)) {
                if (next.getCommand().getCommand().equalsIgnoreCase(command) || next.getCommand().getShortkey().equalsIgnoreCase(command)) {
                    return new FoundAction(next, paramstring);
                }
            }
        }

        throw new IllegalStateException("No command found for " + text + "(existing commands: " + foundCommandTokens + ")");



    }
}
