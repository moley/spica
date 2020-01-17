package org.spica.javaclient.actions.projects;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.ProjectSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.FlagInputParam;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;

public class ResetProjectAction extends AbstractProjectAction {

  private final static Logger LOGGER = LoggerFactory.getLogger(ResetProjectAction.class);

  private static String KEY_CLEAN = "clean";


  @Override public String getDisplayname() {
    return "Reset project";
  }

  @Override public String getDescription() {
    return "Reset all sourcemodules of a project. All local changes are removed";
  }

  @Override public void execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    boolean clean = inputParams.isInputParamAvailable(KEY_CLEAN);

    ProjectInfo projectInfo = getProject(actionContext.getModelCache(), commandLineArguments);

    for (ProjectSourcePartInfo nextModule : projectInfo.getSourceparts()) {
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

  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.PROJECT;
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
