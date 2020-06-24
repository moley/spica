package org.spica.javaclient.actions.tasks;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.utils.InputParamsChecker;
import org.spica.javaclient.utils.ResultChecker;
import org.spica.javaclient.utils.TestUtils;

public class CreateTaskActionTest {

    private TestUtils testUtils = new TestUtils();

    @Test
    public void params () {
        ActionContext actionContext = testUtils.createActionContext(getClass());
        CreateTaskAction createTaskAction = new CreateTaskAction();
        InputParams inputParams = createTaskAction.getInputParams(actionContext, new CommandLineArguments(new String[]{}));
        InputParamsChecker inputParamsChecker = new InputParamsChecker(inputParams);
        inputParamsChecker.numberOfGroups(1);
        inputParamsChecker.inputParam(CreateTaskAction.KEY_DESCRIPTION).inputParam(CreateTaskAction.KEY_SUMMARY);
    }

    @Test
    public void execute () {
        ActionContext actionContext = testUtils.createActionContext(getClass());

        final String name = "Some name";

        InputParams inputParams = Mockito.mock(InputParams.class);
        Mockito.when(inputParams.getInputValueAsString(CreateTaskAction.KEY_SUMMARY)).thenReturn(name);
        CreateTaskAction createTaskAction = new CreateTaskAction();

        Assert.assertEquals ("Task was created before test", 0, actionContext.getModel().getTaskInfos().size());

        createTaskAction.execute(actionContext, inputParams, new CommandLineArguments(new String [] {}));

        Assert.assertEquals ("Task not created", 1, actionContext.getModel().getTaskInfos().size());
        TaskInfo topicInfo = actionContext.getModel().getTaskInfos().get(0);
        Assert.assertEquals ("Name invalid", name, topicInfo.getName());
        Assert.assertNotNull ("ID is null", topicInfo.getId());

    }

    @Test
    public void executeNoSummary () {
        ActionContext actionContext = testUtils.createActionContext(getClass());

        InputParams inputParams = Mockito.mock(InputParams.class);
        CreateTaskAction createTaskAction = new CreateTaskAction();

        Assert.assertEquals ("Task was created before test", 0, actionContext.getModel().getTaskInfos().size());

        createTaskAction.execute(actionContext, inputParams, new CommandLineArguments(new String [] {}));

        Assert.assertEquals ("Task not created", 0, actionContext.getModel().getTaskInfos().size());

        new ResultChecker (createTaskAction).numberOfErrors(1).contains(CreateTaskAction.ERROR_PARAM_NAME);

    }
}
