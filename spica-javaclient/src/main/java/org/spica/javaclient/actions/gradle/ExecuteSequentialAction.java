package org.spica.javaclient.actions.gradle;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class ExecuteSequentialAction extends AbstractAction {

  private List<GradleLauncher> gradleLaunchers = new ArrayList<GradleLauncher>();

  @Override public String getDisplayname() {
    return "Execute gradle tasks sequentially";
  }

  @Override public String getDescription() {
    return "Execute gradle tasks in all sourceparts of a workingset sequentially";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLine) {

    long start = System.currentTimeMillis();

    List<String> commands = commandLine.getMainArguments();

    Model model = actionContext.getModel();
    WorkingSetInfo workingSetInfo = model.findWorkingSetInfoByFolder(new File(""));
    if (workingSetInfo == null)
      outputError("You are not in a folder, which is associated to any workingset");
    else {
      File workingsetFolder = new File (workingSetInfo.getLocalFolder());



      int sum = workingSetInfo.getSourceparts().size();
      int index = 0;
      for (WorkingSetSourcePartInfo nextSourcepart: workingSetInfo.getSourceparts()) {
        index++;
        File nextSourcePartFolder = new File (workingsetFolder, nextSourcepart.getId());

        GradleLauncher gradleLauncher = new GradleLauncher();
        gradleLauncher.setId(nextSourcepart.getId());
        gradleLauncher.setTasks(commands);
        gradleLauncher.setWorkingDir(nextSourcePartFolder);
        outputOk("Starting gradle with tasks " + commands + " in module " + index + " of " + sum + " (folder " + nextSourcePartFolder.getAbsolutePath() + ")");
        gradleLauncher.execute();

        gradleLaunchers.add(gradleLauncher);
      }

      long end = System.currentTimeMillis();

      Statistics showStatistics = new Statistics();
      showStatistics.show(gradleLaunchers, (end - start));


    }

    return null;
  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.GRADLE;
  }

  @Override public Command getCommand() {
    return new Command("sequential", "s");
  }
}
