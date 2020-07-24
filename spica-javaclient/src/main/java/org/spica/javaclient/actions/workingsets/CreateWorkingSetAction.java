package org.spica.javaclient.actions.workingsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;

@Slf4j
public class CreateWorkingSetAction extends AbstractWorkingSetAction {
  public final static String KEY_NAME = "name";

  @Override public String getDisplayname() {
    return "Create workingset";
  }

  @Override public String getDescription() {
    return "Creates a new workingset (name as parameter)";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    String name = inputParams.getInputValueAsString(KEY_NAME) != null ? inputParams.getInputValueAsString(KEY_NAME): commandLineArguments.getSingleMainArgument();

    WorkingSetInfo workingSetInfo = new WorkingSetInfo();
    workingSetInfo.setId(UUID.randomUUID().toString());
    workingSetInfo.setName(name);
    workingSetInfo.setSourceparts(new ArrayList<>());

    Model model = actionContext.getModel();
    model.getWorkingsetInfos().add(workingSetInfo);

    outputOk("Created workingset " + workingSetInfo.getName() + "(" + workingSetInfo.getId() + ")");

    actionContext.saveModel(getClass().getName());

    return null;
  }

  @Override public Command getCommand() {
    return new Command("create", "c");
  }

  @Override public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {

    InputParamGroup inputParamGroup = new InputParamGroup();
    InputParams inputParams = new InputParams(Arrays.asList(inputParamGroup));

    if (!commandLineArguments.hasMainArgument()) {
      TextInputParam name = new TextInputParam(1, KEY_NAME, "Name");
      inputParamGroup.getInputParams().add(name);
    }

    return inputParams;

  }
}
