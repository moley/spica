package org.spica.javaclient.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.auto.ExecuteScriptAction;
import org.spica.javaclient.actions.booking.CloseBookingAction;
import org.spica.javaclient.actions.booking.CreateBookingAction;
import org.spica.javaclient.actions.booking.FinishDayAction;
import org.spica.javaclient.actions.booking.FinishTaskAction;
import org.spica.javaclient.actions.booking.ListBookingsAction;
import org.spica.javaclient.actions.booking.RemoveBookingAction;
import org.spica.javaclient.actions.booking.StartOrStopPauseAction;
import org.spica.javaclient.actions.booking.StartPhonecallAction;
import org.spica.javaclient.actions.booking.StartTaskAction;
import org.spica.javaclient.actions.configuration.ImportUsersAction;
import org.spica.javaclient.actions.configuration.ShowStatusAction;
import org.spica.javaclient.actions.gradle.ExecuteParallelAction;
import org.spica.javaclient.actions.gradle.ExecuteSequentialAction;
import org.spica.javaclient.actions.gradle.InitGradleProjectAction;
import org.spica.javaclient.actions.imports.ImportLogfilesAction;
import org.spica.javaclient.actions.links.CreateLinkAction;
import org.spica.javaclient.actions.links.GotoLinkAction;
import org.spica.javaclient.actions.links.ListLinksAction;
import org.spica.javaclient.actions.links.RemoveLinkAction;
import org.spica.javaclient.actions.projects.CreateProjectAction;
import org.spica.javaclient.actions.projects.ListProjectsAction;
import org.spica.javaclient.actions.projects.RemoveProjectAction;
import org.spica.javaclient.actions.projects.ShowProjectsAction;
import org.spica.javaclient.actions.search.SearchGoogleAction;
import org.spica.javaclient.actions.search.SearchJenkinsAction;
import org.spica.javaclient.actions.search.SearchJiraAction;
import org.spica.javaclient.actions.search.SearchProjectPathInBrowserAction;
import org.spica.javaclient.actions.search.SearchVersioncontrolAction;
import org.spica.javaclient.actions.tasks.CloseTaskAction;
import org.spica.javaclient.actions.tasks.CreateTaskAction;
import org.spica.javaclient.actions.tasks.ImportTaskAction;
import org.spica.javaclient.actions.tasks.ListTasksAction;
import org.spica.javaclient.actions.tasks.RemoveTaskAction;
import org.spica.javaclient.actions.tasks.ShowTasksAction;
import org.spica.javaclient.actions.workingsets.BranchWorkingSetAction;
import org.spica.javaclient.actions.workingsets.CheckoutBranchProjectAction;
import org.spica.javaclient.actions.workingsets.CloneWorkingSetAction;
import org.spica.javaclient.actions.workingsets.CreateWorkingSetAction;
import org.spica.javaclient.actions.workingsets.ImportSourceToWorkingSetAction;
import org.spica.javaclient.actions.workingsets.ListWorkingSetsAction;
import org.spica.javaclient.actions.workingsets.ModulesInWorkingSetAction;
import org.spica.javaclient.actions.workingsets.RemoveBranchProjectAction;
import org.spica.javaclient.actions.workingsets.RemoveWorkingSetAction;
import org.spica.javaclient.actions.workingsets.ResetWorkingSetAction;
import org.spica.javaclient.actions.workingsets.ShowWorkingSetsAction;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.FlagInputParam;
import org.spica.javaclient.params.InputParam;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.utils.LogUtil;

public class ActionHandler {

  private final static Logger LOGGER = LoggerFactory.getLogger(ActionHandler.class);

  private List<Action> registeredActions = new ArrayList<Action>();

  public final static String HELP_TASKNAME = "help";

  public ActionHandler() {
    //import
    registeredActions.add(new ImportLogfilesAction());

    //booking
    registeredActions.add(new CreateBookingAction());
    registeredActions.add(new StartTaskAction());
    registeredActions.add(new StartOrStopPauseAction());
    registeredActions.add(new FinishTaskAction());
    registeredActions.add(new StartPhonecallAction());
    registeredActions.add(new ListBookingsAction());
    registeredActions.add(new CloseBookingAction());
    registeredActions.add(new FinishDayAction());
    registeredActions.add(new RemoveBookingAction());

    //tasks
    registeredActions.add(new CreateTaskAction());
    registeredActions.add(new ListTasksAction());
    registeredActions.add(new ShowTasksAction());
    registeredActions.add(new ImportTaskAction());
    registeredActions.add(new RemoveTaskAction());
    registeredActions.add(new CloseTaskAction());

    //projects
    registeredActions.add(new CreateProjectAction());
    registeredActions.add(new ListProjectsAction());
    registeredActions.add(new ShowProjectsAction());
    registeredActions.add(new RemoveProjectAction());

    //workingsets
    registeredActions.add(new CreateWorkingSetAction());
    registeredActions.add(new ListWorkingSetsAction());
    registeredActions.add(new ShowWorkingSetsAction());
    registeredActions.add(new RemoveWorkingSetAction());
    registeredActions.add(new ImportSourceToWorkingSetAction());
    registeredActions.add(new CloneWorkingSetAction());
    registeredActions.add(new ModulesInWorkingSetAction());
    registeredActions.add(new BranchWorkingSetAction());
    registeredActions.add(new ResetWorkingSetAction());
    registeredActions.add(new RemoveBranchProjectAction());
    registeredActions.add(new CheckoutBranchProjectAction());

    //links
    registeredActions.add(new CreateLinkAction());
    registeredActions.add(new ListLinksAction());
    registeredActions.add(new GotoLinkAction());
    registeredActions.add(new RemoveLinkAction());

    //navigation
    registeredActions.add(new SearchVersioncontrolAction());
    registeredActions.add(new SearchJiraAction());
    registeredActions.add(new SearchGoogleAction());
    registeredActions.add(new SearchJenkinsAction());
    registeredActions.add(new SearchProjectPathInBrowserAction());

    //configuration
    registeredActions.add(new ImportUsersAction());
    registeredActions.add(new ShowStatusAction());

    //gradle
    registeredActions.add(new InitGradleProjectAction());
    registeredActions.add(new ExecuteParallelAction());
    registeredActions.add(new ExecuteSequentialAction());

    //automatication
    registeredActions.add(new ExecuteScriptAction());

    for (ActionGroup next: ActionGroup.values()) {
      HelpAction helpAction = new HelpAction();
      helpAction.setGroup(next);
      registeredActions.add(helpAction);
    }
  }

  public FoundAction findAction(Class<? extends Action> actionClass) {
    for (Action next : registeredActions) {
      if (next.getClass().equals(actionClass))
        return new FoundAction(next, new String[0]);
    }

    throw new IllegalStateException("Action with class " + actionClass.getName() + " was not registered");

  }

  public FoundAction findAction (final ActionGroup actionGroup, final String command) {
    for (Action next : registeredActions) {

      if (next.getGroup().equals(actionGroup) && command.equals(next.getCommand().getCommand()))
        return new FoundAction(next, new String [0]);
    }

    throw new IllegalStateException("Action for group " + actionGroup.name() + " and command " + command + " not found");

  }

  public FoundAction findAction(final String text) {
    String[] tokens = text.split(" ");

    if (tokens.length < 2) //when using spica [GROUP] without further parameter show help
      tokens = new String [] {tokens[0], HELP_TASKNAME};

    String group = tokens[0];
    String command = tokens[1];
    String[] params = Arrays.copyOfRange(tokens, 2, tokens.length);

    Collection<String> foundCommandTokens = new ArrayList<String>();

    for (Action next : registeredActions) {

      foundCommandTokens.add(next.getGroup().name() + "(" + next.getGroup().getShortkey() + ") - " + next.getCommand()
          .getCommand() + "(" + next.getCommand().getShortkey());
      if (next.getGroup().name().equalsIgnoreCase(group) || next.getGroup().getShortkey().equalsIgnoreCase(group)) {
        if (next.getCommand().getCommand().equalsIgnoreCase(command) || next.getCommand().getShortkey()
            .equalsIgnoreCase(command)) {
          return new FoundAction(next, params);
        }
      }
    }

    return null;

  }

  public ActionResult handleAction(ActionContext actionContext, FoundAction foundAction) {
    String parameterAddon = String.join(" ", foundAction.getParameter());
    System.out.println(LogUtil.green(foundAction.getAction().getDisplayname().toUpperCase() + " " + parameterAddon) + "\n\n");
    LOGGER.info("Found action     : " + foundAction.getAction().getClass().getName());
    LOGGER.info("with parameter   : " + foundAction.getParameter());

    Action action = foundAction.getAction();

    CommandLineArguments commandLineArguments = new CommandLineArguments(foundAction.getParameter());

    //create input params
    InputParams inputParams = action.getInputParams(actionContext, commandLineArguments);

    //Parse commandline
    Options options = new Options();
    for (InputParamGroup next : inputParams.getInputParamGroups()) {
      for (InputParam nextParam : next.getInputParams()) {
        boolean hasArg = !(nextParam instanceof FlagInputParam);
        options.addOption(new Option(nextParam.getKey(), hasArg, nextParam.getDisplayname()));
      }
    }
    CommandLine commandLine = commandLineArguments.buildCommandline(options);

    if (!inputParams.isEmpty()) {

      //inject values of commandline
      for (InputParamGroup next : inputParams.getInputParamGroups()) {
        for (InputParam nextParam : next.getInputParams()) {
          if ( nextParam instanceof FlagInputParam) {
            boolean optionContained = commandLine.hasOption(nextParam.getKey());
            nextParam.setValue(String.valueOf(optionContained));
          }
          else if (options.hasOption(nextParam.getKey())) {
            String optionValue = commandLine.getOptionValue(nextParam.getKey());
            if (optionValue != null)
              nextParam.setValue(commandLine.getOptionValue(nextParam.getKey()));
          }
        }
      }

      inputParams = actionContext.getActionParamFactory().build(actionContext, inputParams, foundAction);
    }

    return action.execute(actionContext, inputParams, commandLineArguments);
  }

  public Collection<String> getHelp(ActionGroup group) {
    Collection<String> commands = new ArrayList<>();

    for (Action next : registeredActions) {
      if (next.getGroup().equals(group)) {
        String helpToken = String.format("     %-15s%-50s%-70s", next.getCommand().getCommand(), next.getDisplayname(), next.getDescription());
        commands.add(helpToken);
      }
    }

    return commands;

  }
}
