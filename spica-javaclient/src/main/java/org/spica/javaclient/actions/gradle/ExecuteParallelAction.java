package org.spica.javaclient.actions.gradle;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class ExecuteParallelAction extends AbstractAction {
  @Override public String getDisplayname() {
    return "Execute gradle parallelly";
  }

  @Override public String getDescription() {
    return "Execute gradle tasks in all sourceparts of a workingset parallelly";
  }

  private List<GradleLauncher> gradleLaunchers = new ArrayList<GradleLauncher>();


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

      Collection<Callable<Void>> jobs = new ArrayList<Callable<Void>>();
      for (WorkingSetSourcePartInfo nextSourcepart: workingSetInfo.getSourceparts()) {
        index++;
        File nextSourcePartFolder = new File (workingsetFolder, nextSourcepart.getId());

        outputOk("Starting gradle with tasks " + commands + " in module " + index + " of " + sum + " (folder " + nextSourcePartFolder.getAbsolutePath() + ")");

        jobs.add(new Callable<Void>() {

          @Override public Void call() throws Exception {
              GradleLauncher gradleLauncher = new GradleLauncher();
              gradleLauncher.setId(nextSourcepart.getId());
              gradleLauncher.setTasks(commands);
              gradleLauncher.setWorkingDir(nextSourcePartFolder);
              gradleLauncher.execute();
              gradleLaunchers.add(gradleLauncher);
              return null;
          }
        });
      }

      int availableProcessors = Runtime.getRuntime().availableProcessors();
      ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors);
      try {
        executorService.invokeAll(jobs);
      } catch (InterruptedException e) {
        log.error(e.getLocalizedMessage(), e);
      } finally {
        executorService.shutdown();

        long end = System.currentTimeMillis();

        Statistics showStatistics = new Statistics();
        showStatistics.show(gradleLaunchers, (end-start));
      }



    }


    return null;
  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.GRADLE;
  }

  @Override public Command getCommand() {
    return new Command("parallel", "p");
  }
}
