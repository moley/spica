package org.spica.javaclient.actions.projects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.projects.template.InitializeFrom;
import org.spica.javaclient.actions.projects.template.InitializeFromFactory;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;

public class CreateProjectAction extends AbstractAction {

  private final static Logger LOGGER = LoggerFactory.getLogger(CreateProjectAction.class);

  public final static String KEY_NAME = "name";
  public final static String KEY_FROM = "from";
  public final static String KEY_BRANCH = "branch";

  private InitializeFromFactory initializeFromFactory = new InitializeFromFactory();

  @Override public String getDisplayname() {
    return "Create project";
  }

  @Override public String getDescription() {
    return "Creates a project from scratch / stash url ";
  }

  @Override public void execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    String name = inputParams.getInputValueAsString(KEY_NAME);
    String from = inputParams.getInputValueAsString(KEY_FROM);
    String branch = inputParams.getInputValueAsString(KEY_BRANCH);

    ProjectInfo projectInfo = new ProjectInfo();
    projectInfo.setId(UUID.randomUUID().toString());
    projectInfo.setName(name);

    if (from != null) {
      InitializeFrom initializeFrom = initializeFromFactory.create(from);
      outputDefault("Using project initializer " + initializeFrom.getClass().getSimpleName());
      initializeFrom.initialize(projectInfo, from, branch);
    } else
      projectInfo.setSourceparts(new ArrayList<>());

    ModelCache modelCache = actionContext.getModelCache();
    modelCache.getProjectInfos().add(projectInfo);

    outputOk("Created project " + projectInfo.getName() + "(" + projectInfo.getId() + ") with " + projectInfo
        .getSourceparts().size() + " source parts");

    actionContext.saveModelCache(getClass().getName());
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

    TextInputParam name = new TextInputParam(1, KEY_NAME, "Name");
    inputParamGroup.getInputParams().add(name);

    TextInputParam from = new TextInputParam(1, KEY_FROM, "From");
    inputParamGroup.getInputParams().add(from);

    TextInputParam branch = new TextInputParam(1, KEY_BRANCH, "Branch");
    inputParamGroup.getInputParams().add(branch);

    return inputParams;

  }
}
