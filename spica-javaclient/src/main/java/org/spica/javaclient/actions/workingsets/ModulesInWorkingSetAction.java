package org.spica.javaclient.actions.workingsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;

@Slf4j
public class ModulesInWorkingSetAction extends AbstractWorkingSetAction {

  @Override public String getDisplayname() {
    return "Enable and disable source parts in a workingset";
  }

  private static String KEY_ENABLE = "enable";
  private static String KEY_DISABLE = "disable";
  private static String KEY_REMOVE = "remove";



  @Override public String getDescription() {
    return "Enables or disables source parts in a workingset";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {
    String disableModules = inputParams.getInputValueAsString(KEY_DISABLE);
    String enableModules = inputParams.getInputValueAsString(KEY_ENABLE);
    String removeModules = inputParams.getInputValueAsString(KEY_REMOVE);
    Collection<String> disabledModulesAsList = (disableModules != null ? Arrays.asList(disableModules.split(",")): new ArrayList<String>());
    Collection<String> enabledModulesAsList = (enableModules != null ? Arrays.asList(enableModules.split(",")): new ArrayList<String>());
    Collection<String> removeModulesAsList = (removeModules != null ? Arrays.asList(removeModules.split(",")): new ArrayList<String>());

    WorkingSetInfo workingSetInfo = getWorkingSet(actionContext.getModel(), commandLineArguments);


    if (!enabledModulesAsList.isEmpty())
      outputDefault("Enabling modules " + enabledModulesAsList);

    if (!disabledModulesAsList.isEmpty())
      outputDefault("Disabling modules " + disabledModulesAsList);

    if (!removeModulesAsList.isEmpty())
      outputDefault("Removing modules " + removeModulesAsList);

    Collection<WorkingSetSourcePartInfo> removed = new ArrayList<>();

    for (String next: disabledModulesAsList) {
      WorkingSetSourcePartInfo nextDisabled = findSourcePart(workingSetInfo, next);
      outputDefault("- Disable module " + nextDisabled.getId());
      nextDisabled.setEnabled(false);
    }

    for (String next: enabledModulesAsList) {
      WorkingSetSourcePartInfo nextDisabled = findSourcePart(workingSetInfo, next);
      outputDefault("- Enable module " + nextDisabled.getId());
      nextDisabled.setEnabled(true);
    }

    for (String next: removeModulesAsList) {
      WorkingSetSourcePartInfo nextRemoved = findSourcePart(workingSetInfo, next);
      outputDefault("- Remove module " + nextRemoved.getId());
      removed.add(nextRemoved);
    }
    workingSetInfo.getSourceparts().removeAll(removed);
    actionContext.saveModel(getClass().getName());

    return null;
  }

  private WorkingSetSourcePartInfo findSourcePart (final WorkingSetInfo workingSetInfo, String module) {
    for (WorkingSetSourcePartInfo next: workingSetInfo.getSourceparts()) {
      if (next.getId().equals(module))
        return next;
    }

    throw new IllegalStateException("Module " + module + " was not found in project ( available modules: " + workingSetInfo.getSourceparts() + ")");

  }


  @Override public Command getCommand() {
    return new Command("modules", "m");
  }

  @Override public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {

    boolean disableActive = commandLineArguments.getArguments().contains("-" + KEY_DISABLE);
    boolean enableActive = commandLineArguments.getArguments().contains("-" + KEY_ENABLE);
    boolean removeActive = commandLineArguments.getArguments().contains("-" + KEY_REMOVE);

    InputParamGroup inputParamGroup = new InputParamGroup();
    InputParams inputParams = new InputParams(Arrays.asList(inputParamGroup));

    if (disableActive) {
      TextInputParam disable = new TextInputParam(1, KEY_DISABLE, "Disable modules (comma separated list)");
      inputParamGroup.getInputParams().add(disable);
    }

    if (enableActive) {
      TextInputParam disable = new TextInputParam(1, KEY_ENABLE, "Enable modules (comma separated list)");
      inputParamGroup.getInputParams().add(disable);
    }

    if (removeActive) {
      TextInputParam disable = new TextInputParam(1, KEY_REMOVE, "Remove modules (comma separated list)");
      inputParamGroup.getInputParams().add(disable);
    }

    return inputParams;
  }

}
