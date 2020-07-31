package org.spica.javaclient.actions.workingsets;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.workingsets.template.InitializeFromFactory;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class CloneWorkingSetAction extends AbstractWorkingSetAction {

  private InitializeFromFactory initializeFromFactory = new InitializeFromFactory();

  @Override public String getDisplayname() {
    return "Clone source parts";
  }

  @Override public String getDescription() {
    return "Clones all source parts of a workingset";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    final String username = actionContext.getProperties().getValue("spica.stash.user");
    final String password = actionContext.getProperties().getValue("spica.stash.password");

    UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = null;

    if (username != null && password != null)
      usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(username, password);

    WorkingSetInfo workingSetInfo = getWorkingSet(actionContext.getModel(), commandLineArguments);

    for (WorkingSetSourcePartInfo nextModule: workingSetInfo.getSourceparts()) {
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

  @Override public Command getCommand() {
    return new Command("clone", "c");
  }

}
