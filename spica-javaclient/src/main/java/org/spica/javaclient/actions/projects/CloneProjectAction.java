package org.spica.javaclient.actions.projects;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.projects.template.InitializeFromFactory;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.ProjectSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class CloneProjectAction extends AbstractProjectAction {

  private final static Logger LOGGER = LoggerFactory.getLogger(CloneProjectAction.class);

  private InitializeFromFactory initializeFromFactory = new InitializeFromFactory();

  @Override public String getDisplayname() {
    return "Clone project";
  }

  @Override public String getDescription() {
    return "Clones all source modules of a project";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    final String username = actionContext.getProperties().getValue("spica.stash.user");
    final String password = actionContext.getProperties().getValue("spica.stash.password");
    UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(username, password);

    ProjectInfo projectInfo = getProject(actionContext.getModel(), commandLineArguments);

    for (ProjectSourcePartInfo nextModule: projectInfo.getSourceparts()) {
      if (nextModule.isEnabled()) {
        File toDir = new File (nextModule.getId()).getAbsoluteFile();
        outputDefault("Clone " + nextModule.getId() + " to " + toDir.getAbsolutePath());
        try {
          Git.cloneRepository().setDirectory(toDir).setCredentialsProvider(usernamePasswordCredentialsProvider).setBranch(nextModule.getBranch()).setURI(nextModule.getUrl()).call();
        } catch (GitAPIException e) {
          throw new IllegalStateException(e);
        }
      }
    }

    return null;




  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.PROJECT;
  }

  @Override public Command getCommand() {
    return new Command("clone", "c");
  }

}
