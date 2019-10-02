package org.spica.javaclient.actions.projects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.InputParamGroup;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.actions.params.TextInputParam;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.utils.LogUtil;

import java.util.Arrays;
import java.util.UUID;

public class CreateProjectAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateProjectAction.class);

    public final static String KEY_NAME = "name";


    @Override
    public String getDisplayname() {
        return "Create project";
    }

    @Override
    public String getDescription() {
        return "Creates a project (parameter is projectname)";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        boolean parameterListAvailable = parameterList != null && ! parameterList.trim().isBlank();
        String name = parameterListAvailable ? parameterList.trim() : inputParams.getInputParamAsString(KEY_NAME);

        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setId(UUID.randomUUID().toString());
        projectInfo.setName(name);
        ModelCache modelCache = actionContext.getModelCache();
        modelCache.getProjectInfos().add(projectInfo);

        outputOk("Created project " + projectInfo.getName() + "(" + projectInfo.getId() + ")");

        actionContext.saveModelCache(getClass().getName());
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.PROJECT;
    }

    @Override
    public Command getCommand() {
        return new Command ("create", "c");
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, String paramList) {


        InputParamGroup inputParamGroup = new InputParamGroup();
        InputParams inputParams = new InputParams(Arrays.asList(inputParamGroup));

        if (paramList.trim().isBlank()) {
            TextInputParam name = new TextInputParam(1, KEY_NAME, "Name", "");
            inputParamGroup.getInputParams().add(name);
        }

        return inputParams;

    }
}
