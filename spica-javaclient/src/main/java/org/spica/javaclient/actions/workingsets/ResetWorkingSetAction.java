package org.spica.javaclient.actions.workingsets;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.FlagInputParam;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class ResetWorkingSetAction extends AbstractWorkingSetAction {

  private static String KEY_CLEAN = "clean";


  @Override public String getDisplayname() {
    return "Resets workingset";
  }

  @Override public String getDescription() {
    return "Reset all source parts of a workingset. All local changes are removed";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    boolean clean = inputParams.isInputParamAvailable(KEY_CLEAN);

    WorkingSetInfo workingSetInfo = getWorkingSet(actionContext.getModel(), commandLineArguments);

    for (WorkingSetSourcePartInfo nextModule : workingSetInfo.getSourceparts()) {
      if (nextModule.isEnabled()) {
        File toDir = new File(nextModule.getId()).getAbsoluteFile();
        try {
          Git git = Git.open(toDir);
          outputDefault("Reset " + toDir.getAbsolutePath() + " (clean " + clean + ")");
          git.reset().setMode(ResetCommand.ResetType.HARD).call();
          if (clean)
            git.clean().call();

        } catch (GitAPIException | IOException e) {
          throw new IllegalStateException("Error resetting " + toDir.getAbsolutePath(), e);
        }
      }
    }

    return null;

  }

  @Override public Command getCommand() {
    return new Command("reset", "r");
  }

  @Override public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {

    InputParamGroup inputParamGroup = new InputParamGroup();
    InputParams inputParams = new InputParams(Arrays.asList(inputParamGroup));

    inputParamGroup.getInputParams().add(new FlagInputParam(KEY_CLEAN));

    return inputParams;
  }

}
