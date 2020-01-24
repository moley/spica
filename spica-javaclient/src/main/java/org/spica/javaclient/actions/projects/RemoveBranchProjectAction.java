package org.spica.javaclient.actions.projects;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.ProjectSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;

public class RemoveBranchProjectAction extends AbstractProjectAction {

  private final static Logger LOGGER = LoggerFactory.getLogger(RemoveBranchProjectAction.class);

  private static String KEY_BRANCH = "branch";


  @Override public String getDisplayname() {
    return "Remove branch on project";
  }

  @Override public String getDescription() {
    return "Removes a branch on all submodules of a project";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    final String branch = inputParams.getInputValueAsString(KEY_BRANCH);

    ProjectInfo projectInfo = getProject(actionContext.getModel(), commandLineArguments);

    for (ProjectSourcePartInfo nextModule : projectInfo.getSourceparts()) {
      if (nextModule.isEnabled()) {
        File toDir = new File(nextModule.getId()).getAbsoluteFile();
        try {
          Git git = Git.open(toDir);
          outputDefault("Delete branch " + branch + " in " + toDir.getAbsolutePath());
          git.branchDelete().setBranchNames(branch).call();
        } catch (GitAPIException | IOException e) {
          throw new IllegalStateException("Error resetting " + toDir.getAbsolutePath(), e);
        }
      }
    }

    return null;

  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.PROJECT;
  }

  @Override public Command getCommand() {
    return new Command("removebranch", "r");
  }

  @Override public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {

    InputParamGroup inputParamGroup = new InputParamGroup();
    InputParams inputParams = new InputParams(Arrays.asList(inputParamGroup));

    TextInputParam branch = new TextInputParam(1, KEY_BRANCH, "Branch");
    inputParamGroup.getInputParams().add(branch);

    return inputParams;
  }

}
