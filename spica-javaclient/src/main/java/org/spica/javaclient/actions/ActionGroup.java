package org.spica.javaclient.actions;

public enum ActionGroup {

    TOPIC("t"),
    PROJECT("p"),
    GOTO ("g"),
    BOOKING("b"),
    LINKS("l"),
    CONFIGURATION("c"),
    AUTOMATION ("a");

    private String shortkey;

    private ActionGroup (final String shortkey) {
        this.shortkey = shortkey;
    }

    public String getShortkey () {
        return shortkey;
    }
}
