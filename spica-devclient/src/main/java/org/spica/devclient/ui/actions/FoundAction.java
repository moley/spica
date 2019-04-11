package org.spica.devclient.ui.actions;

public class FoundAction {

    private final Action action;

    private final String parameter;

    public FoundAction (final Action action, String parameter) {
        this.action = action;
        this.parameter = parameter;

    }


    public Action getAction() {
        return action;
    }

    public String getParameter() {
        return parameter;
    }
}
