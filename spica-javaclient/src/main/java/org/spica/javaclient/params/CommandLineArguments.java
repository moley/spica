package org.spica.javaclient.params;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineArguments {

  private CommandLine commandLine;



  private List<String> arguments;

  private String [] argumentsAsArray;

  public CommandLineArguments (final String [] args) {
    this.arguments = Arrays.asList(args);
    this.argumentsAsArray = args;
  }

  public CommandLine getCommandLine () {
    if (commandLine == null)
      throw new IllegalStateException("Commandline can accessed after it is built. At this point in the lifecycle you cannot use it");
    return commandLine;
  }

  public CommandLine buildCommandline(final Options options) {
    try {
      commandLine = new DefaultParser().parse(options, argumentsAsArray);
    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp( "spica", options );
      System.exit(1);
    }
    return commandLine;
  }

  public String getOption (String option) {
    return commandLine.getOptionValue(option);
  }

  public List<String> getMainArguments () {
    List<String> mainArguments = new ArrayList<>();

    for (String next: arguments) {
      if (! next.startsWith("-"))
        mainArguments.add(next);
    }

    return mainArguments;
  }

  public String getOptionalMainArgument() {
    List<String> mainArguments = getMainArguments();
    if (mainArguments.isEmpty())
      return null;
    else
      return mainArguments.get(0);
  }

  public String getOptionalMainArgumentNotNull() {
    List<String> mainArguments = getMainArguments();

    if (mainArguments.isEmpty())
      return "";
    else
      return mainArguments.get(0);
  }

  public boolean hasArgument (final String argument) {
    return commandLine.hasOption(argument);
  }

  public String getMandatoryMainArgument(String error) {
    List<String> mainArguments = getMainArguments();
    if (mainArguments.isEmpty())
      throw new IllegalStateException(error);
    else
      return mainArguments.get(0);
  }

  public String getSingleMainArgument() {
    List<String> mainArguments = getMainArguments();

    if (mainArguments.size() != 1)
      throw new IllegalStateException("Not exactly one argument used, but " + commandLine.getArgList());
    else
      return mainArguments.get(0);
  }

  public boolean isEmpty () {
    return arguments.isEmpty();
  }

  /**
   * returns true if argument list is empty or the first argument matches the given match parameter
   *
   * @param match  match parameter
   *
   * @return true: no arg or matching
   */
  public boolean noArgumentOr (final String ... match) {
    if (isEmpty())
      return true;
    for (String next: match) {
      if (arguments.get(0).equals(next))
        return true;

    }
    return false;
  }

  public List<String> getArguments() {
    return arguments;
  }

  public String toString () {
    return String.join(" ", arguments);
  }
}
