package org.spica.javaclient.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.admin.ImportUsersAction;
import org.spica.javaclient.actions.admin.ShowStatusAction;
import org.spica.javaclient.actions.booking.*;
import org.spica.javaclient.actions.links.*;
import org.spica.javaclient.actions.navigation.GotoGoogleAction;
import org.spica.javaclient.actions.navigation.GotoJiraAction;
import org.spica.javaclient.actions.navigation.GotoStashAction;
import org.spica.javaclient.actions.projects.*;
import org.spica.javaclient.actions.topics.*;
import org.spica.javaclient.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ActionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActionHandler.class);

    private Collection<Action> registeredActions = new ArrayList<Action>();


    //TODO per reflection
    public ActionHandler () {
        //Register actions

        //booking
        registeredActions.add(new CreateBookingAction());
        registeredActions.add(new StartTopicAction());
        registeredActions.add(new StartOrStopPauseAction());
        registeredActions.add(new FinishTopicAction());
        registeredActions.add(new StartPhonecallAction());
        registeredActions.add(new ListBookingsAction());
        registeredActions.add(new CloseBookingAction());
        registeredActions.add(new FinishDayAction());
        registeredActions.add(new RemoveBookingAction());
        registeredActions.add(new EmptyBookingsAction());

        //topics
        registeredActions.add(new CreateTopicAction());
        registeredActions.add(new ListTopicsAction());
        registeredActions.add(new ShowTopicsAction());
        registeredActions.add(new ImportTopicAction());
        registeredActions.add(new RemoveTopicAction());
        registeredActions.add(new EmptyTopicsAction());

        //projects
        registeredActions.add(new CreateProjectAction());
        registeredActions.add(new ListProjectsAction());
        registeredActions.add(new ShowProjectsAction());
        registeredActions.add(new RemoveProjectAction());
        registeredActions.add(new EmptyProjectsAction());

        //links
        registeredActions.add(new CreateLinkAction());
        registeredActions.add(new ListLinksAction());
        registeredActions.add(new ExecuteLinkAction());
        registeredActions.add(new GotoLinkAction());
        registeredActions.add(new RemoveLinkAction());


        //navigation
        registeredActions.add(new GotoStashAction());
        registeredActions.add(new GotoJiraAction());
        registeredActions.add(new GotoGoogleAction());

        registeredActions.add(new ImportUsersAction());
        registeredActions.add(new ShowStatusAction());

    }

    public FoundAction findAction (final String text) {
        String [] tokens = text.split(" ");

        if (tokens.length < 2)
            throw new IllegalStateException("No valid command line. Use '[ACTIONGROUP] [COMMAND] [PARAM1...n]");

        String group = tokens[0];
        String command = tokens [1];
        String [] params = Arrays.copyOfRange(tokens, 2, tokens.length);
        String paramstring = String.join(" ", params);

        Collection<String> foundCommandTokens = new ArrayList<String>();


        for (Action next: registeredActions) {

            foundCommandTokens.add(next.getGroup().name() + "(" + next.getGroup().getShortkey() + ") - " + next.getCommand().getCommand() + "(" + next.getCommand().getShortkey());
            if (next.getGroup().name().equalsIgnoreCase(group) || next.getGroup().getShortkey().equalsIgnoreCase(group)) {
                if (next.getCommand().getCommand().equalsIgnoreCase(command) || next.getCommand().getShortkey().equalsIgnoreCase(command)) {
                    return new FoundAction(next, paramstring);
                }
            }
        }


        return null;

    }

    public Collection<String> getHelp () {
        Collection<String> commands = new ArrayList<>();
        String lastGroup = "";

        for (Action next: registeredActions) {
            if (lastGroup.strip().isBlank() || ! lastGroup.equals(next.getGroup().name())) {
                commands.add("\n  " + LogUtil.green(next.getGroup().name()));
            }
            lastGroup = next.getGroup().name();

            String helpToken = String.format("     %-2s%-5s%-40s%-70s", next.getGroup().getShortkey(), next.getCommand().getShortkey(), next.getDisplayname(), next.getDescription());
            commands.add(helpToken);
        }

        return commands;

    }
}
