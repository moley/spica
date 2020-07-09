package org.spica.javaclient.actions.projects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.projects.template.InitializeFromFactory;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;

@Slf4j
public class CreateProjectAction extends AbstractAction {

  public final static String KEY_NAME = "name";

  @Override public String getDisplayname() {
    return "Create project";
  }

  @Override public String getDescription() {
    return "Creates a new project";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    String name = inputParams.getInputValueAsString(KEY_NAME) != null ? inputParams.getInputValueAsString(KEY_NAME): commandLineArguments.getSingleMainArgument();

    ProjectInfo projectInfo = new ProjectInfo();
    projectInfo.setId(UUID.randomUUID().toString());
    projectInfo.setName(name);
    projectInfo.setSourceparts(new ArrayList<>());

    Model model = actionContext.getModel();
    model.getProjectInfos().add(projectInfo);

    outputOk("Created project " + projectInfo.getName() + "(" + projectInfo.getId() + ") with " + projectInfo
        .getSourceparts().size() + " source parts");

    actionContext.saveModel(getClass().getName());

    return null;
  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.PROJECT;
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
