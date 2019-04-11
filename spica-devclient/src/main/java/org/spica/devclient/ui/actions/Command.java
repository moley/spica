package org.spica.devclient.ui.actions;

public class Command {

    private final String command;

    private final String shortkey;

    public Command (String command, String shortkey) {
        this.command = command;
        this.shortkey = shortkey;
    }

    public String getCommand() {
        return command;
    }

    public String getShortkey() {
        return shortkey;
    }
}
