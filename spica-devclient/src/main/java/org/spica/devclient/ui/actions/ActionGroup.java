package org.spica.devclient.ui.actions;

public enum ActionGroup {

    TOPIC("t"),
    PROJECT("p"),
    BOOKING("b");

    private String shortkey;

    private ActionGroup (final String shortkey) {
        this.shortkey = shortkey;
    }

    public String getShortkey () {
        return shortkey;
    }
}
