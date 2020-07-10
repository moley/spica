package org.spica.javaclient.actions.workingsets;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.workingsets.template.InitializeFrom;
import org.spica.javaclient.actions.workingsets.template.InitializeFromFactory;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j public class ImportSourceToWorkingsSetAction extends AbstractWorkingSetAction {

  @Override public String getDisplayname() {
    return "Import source to workingset";
  }

  @Override public String getDescription() {
    return "Import source to workingset (params ID URL BRANCH)";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {
    WorkingSetInfo workingSetInfo = getWorkingSet(actionContext.getModel(), commandLineArguments);
    String url = commandLineArguments.getMainArguments().get(1);
    String branch = commandLineArguments.getMainArguments().get(2);

    outputDefault("Looking for branches to import source parts to workingSet " + workingSetInfo.getId());
    outputDefault("for url " + url + " and branch " + branch);

    InitializeFromFactory initializeFromFactory = new InitializeFromFactory();
    InitializeFrom initializeFrom = initializeFromFactory.create(url);
    List<WorkingSetSourcePartInfo> workingSetSourcePartInfos = initializeFrom.initialize(url, branch);
    outputOk("Found " + workingSetSourcePartInfos.size() + " source parts:");
    for (WorkingSetSourcePartInfo next: workingSetSourcePartInfos) {
      next.setEnabled(true);
      workingSetInfo.addSourcepartsItem(next);
      outputOk("-" + next.getBranch() + "(" + next.getUrl() + ")");
    }

    actionContext.saveModel("Imported " + workingSetSourcePartInfos.size() + "sourceparts for workingset " + workingSetInfo.getId());

    return null;
  }

  @Override public Command getCommand() {
    return new Command("import", "i");
  }

}
