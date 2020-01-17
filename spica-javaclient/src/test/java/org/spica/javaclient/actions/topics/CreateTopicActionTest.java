package org.spica.javaclient.actions.topics;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.utils.InputParamsChecker;
import org.spica.javaclient.utils.ResultChecker;
import org.spica.javaclient.utils.TestUtils;

public class CreateTopicActionTest {

    private TestUtils testUtils = new TestUtils();

    @Test
    public void params () {
        ActionContext actionContext = testUtils.createActionContext(getClass());
        CreateTopicAction createTopicAction = new CreateTopicAction();
        InputParams inputParams = createTopicAction.getInputParams(actionContext, new CommandLineArguments(new String[]{}));
        InputParamsChecker inputParamsChecker = new InputParamsChecker(inputParams);
        inputParamsChecker.numberOfGroups(1);
        inputParamsChecker.inputParam(CreateTopicAction.KEY_DESCRIPTION).inputParam(CreateTopicAction.KEY_SUMMARY);
    }

    @Test
    public void execute () {
        ActionContext actionContext = testUtils.createActionContext(getClass());

        final String name = "Some name";

        InputParams inputParams = Mockito.mock(InputParams.class);
        Mockito.when(inputParams.getInputValueAsString(CreateTopicAction.KEY_SUMMARY)).thenReturn(name);
        CreateTopicAction createTopicAction = new CreateTopicAction();

        Assert.assertEquals ("Topic was created before test", 0, actionContext.getModelCache().getTopicInfos().size());

        createTopicAction.execute(actionContext, inputParams, new CommandLineArguments(new String [] {}));

        Assert.assertEquals ("Topic not created", 1, actionContext.getModelCache().getTopicInfos().size());
        TopicInfo topicInfo = actionContext.getModelCache().getTopicInfos().get(0);
        Assert.assertEquals ("Name invalid", name, topicInfo.getName());
        Assert.assertNotNull ("ID is null", topicInfo.getId());

    }

    @Test
    public void executeNoSummary () {
        ActionContext actionContext = testUtils.createActionContext(getClass());

        InputParams inputParams = Mockito.mock(InputParams.class);
        CreateTopicAction createTopicAction = new CreateTopicAction();

        Assert.assertEquals ("Topic was created before test", 0, actionContext.getModelCache().getTopicInfos().size());

        createTopicAction.execute(actionContext, inputParams, new CommandLineArguments(new String [] {}));

        Assert.assertEquals ("Topic not created", 0, actionContext.getModelCache().getTopicInfos().size());

        new ResultChecker (createTopicAction).numberOfErrors(1).contains(CreateTopicAction.ERROR_PARAM_NAME);

    }
}
