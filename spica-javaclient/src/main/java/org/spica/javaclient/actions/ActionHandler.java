package org.spica.javaclient.actions;

import com.atlassian.stash.rest.client.api.entity.Branch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.auto.ExecuteScriptAction;
import org.spica.javaclient.actions.booking.CloseBookingAction;
import org.spica.javaclient.actions.booking.CreateBookingAction;
import org.spica.javaclient.actions.booking.EmptyBookingsAction;
import org.spica.javaclient.actions.booking.FinishDayAction;
import org.spica.javaclient.actions.booking.FinishTopicAction;
import org.spica.javaclient.actions.booking.ListBookingsAction;
import org.spica.javaclient.actions.booking.RemoveBookingAction;
import org.spica.javaclient.actions.booking.StartOrStopPauseAction;
import org.spica.javaclient.actions.booking.StartPhonecallAction;
import org.spica.javaclient.actions.booking.StartTopicAction;
import org.spica.javaclient.actions.configuration.ImportUsersAction;
import org.spica.javaclient.actions.configuration.ShowStatusAction;
import org.spica.javaclient.actions.links.CreateLinkAction;
import org.spica.javaclient.actions.links.ExecuteLinkAction;
import org.spica.javaclient.actions.links.GotoLinkAction;
import org.spica.javaclient.actions.links.ListLinksAction;
import org.spica.javaclient.actions.links.RemoveLinkAction;
import org.spica.javaclient.actions.navigation.GotoGoogleAction;
import org.spica.javaclient.actions.navigation.GotoJiraAction;
import org.spica.javaclient.actions.navigation.GotoStashAction;
import org.spica.javaclient.actions.projects.BranchProjectAction;
import org.spica.javaclient.actions.projects.CloneProjectAction;
import org.spica.javaclient.actions.projects.CreateProjectAction;
import org.spica.javaclient.actions.projects.EmptyProjectsAction;
import org.spica.javaclient.actions.projects.ListProjectsAction;
import org.spica.javaclient.actions.projects.ModulesInProjectAction;
import org.spica.javaclient.actions.projects.RemoveBranchProjectAction;
import org.spica.javaclient.actions.projects.RemoveProjectAction;
import org.spica.javaclient.actions.projects.ResetProjectAction;
import org.spica.javaclient.actions.projects.ShowProjectsAction;
import org.spica.javaclient.actions.topics.CreateTopicAction;
import org.spica.javaclient.actions.topics.EmptyTopicsAction;
import org.spica.javaclient.actions.topics.ImportTopicAction;
import org.spica.javaclient.actions.topics.ListTopicsAction;
import org.spica.javaclient.actions.topics.RemoveTopicAction;
import org.spica.javaclient.actions.topics.ShowTopicsAction;
import org.spica.javaclient.utils.LogUtil;

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
        registeredActions.add(new ModulesInProjectAction());
        registeredActions.add(new CloneProjectAction());
        registeredActions.add(new BranchProjectAction());
        registeredActions.add(new ResetProjectAction());
        registeredActions.add(new RemoveBranchProjectAction());
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

        //configuration
        registeredActions.add(new ImportUsersAction());
        registeredActions.add(new ShowStatusAction());

        //automatication
        registeredActions.add(new ExecuteScriptAction());
    }

    public FoundAction findAction (final String text) {
        String [] tokens = text.split(" ");

        if (tokens.length < 2)
            throw new IllegalStateException("No valid command line. Use '[ACTIONGROUP] [COMMAND] [PARAM1...n]");

        String group = tokens[0];
        String command = tokens [1];
        String [] params = Arrays.copyOfRange(tokens, 2, tokens.length);

        Collection<String> foundCommandTokens = new ArrayList<String>();

        for (Action next: registeredActions) {

            foundCommandTokens.add(next.getGroup().name() + "(" + next.getGroup().getShortkey() + ") - " + next.getCommand().getCommand() + "(" + next.getCommand().getShortkey());
            if (next.getGroup().name().equalsIgnoreCase(group) || next.getGroup().getShortkey().equalsIgnoreCase(group)) {
                if (next.getCommand().getCommand().equalsIgnoreCase(command) || next.getCommand().getShortkey().equalsIgnoreCase(command)) {
                    return new FoundAction(next, params);
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
                commands.add("\n  " + LogUtil.green(next.getGroup().name() + " (" + next.getGroup().getShortkey() + ")"));
            }
            lastGroup = next.getGroup().name();

            String helpToken = String.format("     %-15s%-50s%-70s", next.getCommand().getCommand(), next.getDisplayname(), next.getDescription());
            commands.add(helpToken);
        }

        return commands;

    }
}
