package org.spica.javaclient.actions.workingsets;

import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.workingsets.template.InitializeFrom;
import org.spica.javaclient.actions.workingsets.template.InitializeFromFactory;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j public class ImportSourceToWorkingSetAction extends AbstractWorkingSetAction {

  @Override public String getDisplayname() {
    return "Import source to workingset";
  }

  @Override public String getDescription() {
    return "Import source to workingset (params ID URL BRANCH)";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {
    Model model = actionContext.getModel();
    WorkingSetInfo workingSetInfo = getWorkingSet(model, commandLineArguments);
    String url = commandLineArguments.getMainArguments().get(1);
    String branch = (commandLineArguments.getMainArguments().size() > 2 ? commandLineArguments.getMainArguments().get(2): null);

    File currentPath = new File ("").getAbsoluteFile();
    workingSetInfo.setLocalFolder(currentPath.getAbsolutePath());
    outputDefault("Workingset " + workingSetInfo.getId() + " associated with local folder " + workingSetInfo.getLocalFolder());
    outputDefault("Looking for branches to import source parts to workingSet");
    outputDefault("for url " + url + " and branch " + branch);

    InitializeFromFactory initializeFromFactory = new InitializeFromFactory();
    InitializeFrom initializeFrom = initializeFromFactory.create(url);
    List<WorkingSetSourcePartInfo> workingSetSourcePartInfos = initializeFrom.initialize(url, branch);
    outputOk("Found " + workingSetSourcePartInfos.size() + " source parts:");
    for (WorkingSetSourcePartInfo next: workingSetSourcePartInfos) {
      next.setEnabled(true);

      WorkingSetSourcePartInfo existingSourcePart = model.findWorkingSetSourcePart(workingSetInfo, next.getUrl(), next.getBranch());
      if (existingSourcePart == null) {
        workingSetInfo.addSourcepartsItem(next);
        outputOk("- Added sourcepart " + next.getBranch() + "(" + next.getUrl() + ")");
      }
      else {
        outputWarning("- Sourcepart " + next.getBranch() + "(" + next.getUrl() + ") already exists");
      }
    }

    actionContext.saveModel("Imported " + workingSetSourcePartInfos.size() + "sourceparts for workingset " + workingSetInfo.getId());

    return null;
  }

  @Override public Command getCommand() {
    return new Command("import", "i");
  }

}
