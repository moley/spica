package org.spica.javaclient.params;

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

  public String getOptionalFirstArgument () {
    if (isEmpty())
      return null;
    else
      return arguments.get(0);
  }

  public String getOptionalFirstArgumentNotNull () {
    if (isEmpty())
      return "";
    else
      return arguments.get(0);
  }

  public boolean hasArgument (final String argument) {
    return commandLine.hasOption(argument);
  }

  public String getMandatoryFirstArgument (String error) {
    if (isEmpty())
      throw new IllegalStateException(error);
    else
      return arguments.get(0);
  }

  public String getSingleArgument () {
    if (arguments.size() != 1)
      throw new IllegalStateException("Not exactly one argument used, but " + commandLine.getArgList());
    else
      return arguments.get(0);
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

  public String getArgumentsString () {
    return String.join(" ", arguments);
  }

  public String toString () {
    return String.join(" ", arguments);
  }
}
