package org.spica.javaclient.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ActionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActionHandler.class);

    private Collection<Action> registeredActions = new ArrayList<Action>();


    //TODO per reflection
    public ActionHandler () {
        //Register actions
        registeredActions.add(new StartOrStopPauseAction());
        registeredActions.add(new FinishDayAction());
        registeredActions.add(new CreateTopicAction());
        registeredActions.add(new GotoStashAction());
        registeredActions.add(new GotoJiraAction());
    }


    public FoundAction findAction (final Class clazz) {
        Collection<String> foundClasses = new ArrayList<String>();

        for (Action next: registeredActions) {
            if (next.getClass().equals(clazz))
                return new FoundAction(next, "");
            else
                foundClasses.add(next.getClass().getSimpleName());
        }

        throw new IllegalStateException("No command found for class " + clazz + "(existing commands: " + foundClasses + ")");


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

    public Collection<Action> getRegisteredActions () {
        return registeredActions;
    }

    public Collection<String> getHelp () {
        Collection<String> commands = new ArrayList<>();
        for (Action next: registeredActions) {
            String helpToken = String.format("     %-2s%-5s%-40s%-70s", next.getGroup().getShortkey(), next.getCommand().getShortkey(), next.getDisplayname(), next.getDescription());
            commands.add(helpToken);
        }

        return commands;

    }
}
