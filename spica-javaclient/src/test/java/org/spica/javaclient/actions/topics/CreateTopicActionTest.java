package org.spica.javaclient.actions.topics;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.utils.InputParamsChecker;
import org.spica.javaclient.utils.TestUtils;

public class CreateTopicActionTest {

    private TestUtils testUtils = new TestUtils();

    private ActionContext actionContext = testUtils.createActionContext(getClass());

    @Test
    public void params () {
        CreateTopicAction createTopicAction = new CreateTopicAction();
        InputParams inputParams = createTopicAction.getInputParams(actionContext, "");
        InputParamsChecker inputParamsChecker = new InputParamsChecker(inputParams);
        inputParamsChecker.numberOfGroups(1);
        inputParamsChecker.inputParam(CreateTopicAction.KEY_DESCRIPTION).inputParam(CreateTopicAction.KEY_SUMMARY);
    }

    @Test
    public void execute () {

        InputParams inputParams = Mockito.mock(InputParams.class);
        CreateTopicAction createTopicAction = new CreateTopicAction();

        Assert.assertEquals ("Topic was created before test", 0, actionContext.getModelCache().getTopicInfos().size());

        createTopicAction.execute(actionContext, inputParams, "");

        Assert.assertEquals ("Topic not created", 1, actionContext.getModelCache().getTopicInfos().size());

    }

    @Test
    public void executeNoSummary () {

        InputParams inputParams = Mockito.mock(InputParams.class);
        CreateTopicAction createTopicAction = new CreateTopicAction();

        Assert.assertEquals ("Topic was created before test", 0, actionContext.getModelCache().getTopicInfos().size());

        createTopicAction.execute(actionContext, inputParams, "");

        Assert.assertEquals ("Topic not created", 1, actionContext.getModelCache().getTopicInfos().size());

    }
}
