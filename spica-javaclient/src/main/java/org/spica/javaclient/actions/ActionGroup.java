package org.spica.javaclient.actions;

public enum ActionGroup {

    TOPIC("t"),
    PROJECT("p"),
    SEARCH("s"),
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

    public static ActionGroup findByShortKey (final String shortkey) {
        for (ActionGroup next: ActionGroup.values()) {
            if (next.getShortkey().equals(shortkey))
                return next;

        }

        throw new IllegalStateException("No action group found for shortkey '" + shortkey + "'");

    }
}
