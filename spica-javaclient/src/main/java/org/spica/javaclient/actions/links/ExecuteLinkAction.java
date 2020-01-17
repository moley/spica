package org.spica.javaclient.actions.links;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.ConfirmInputParam;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.utils.RenderUtil;

public class ExecuteLinkAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExecuteLinkAction.class);

    public final static  String KEY_CONFIRM = "KEY_CONFIRM";

    private RenderUtil renderUtil = new RenderUtil();

    @Override public String getDisplayname() {
        return "Execute link";
    }

    @Override
    public String getDescription() {
        return "Download all links and execute them";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {


        String query = commandLineArguments.getMandatoryFirstArgument("You have to add a parameter containing a name, id or url to your command");
        List<LinkInfo> linkInfos = actionContext.getModelCache().findLinkInfosByQuery(query);
        if (linkInfos.isEmpty())
            outputError("No links found for query <" + query + ">");
        else {

            boolean confirmed = inputParams.getInputValueAsBoolean(KEY_CONFIRM, false);
            if (confirmed) {
                outputDefault("\n");
                for (LinkInfo next : linkInfos) {

                    outputOk("Executed " + next.getUrl());
                }

            }
        }



    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.LINKS;
    }

    @Override
    public Command getCommand() {
        return new Command("execute", "e");
    }

    public InputParams getInputParams(ActionContext actionContext, String parameterList) {

        InputParams inputParams = new InputParams();
        List<LinkInfo> linkInfos = actionContext.getModelCache().findLinkInfosByQuery(parameterList);

        for (LinkInfo next: linkInfos) {
            outputDefault(renderUtil.getLink(next));
        }

        if (! linkInfos.isEmpty()) {
            ConfirmInputParam confirmInputParam = new ConfirmInputParam(KEY_CONFIRM, "Do you really want to execute " + linkInfos.size() + " actions?", "Y");
            InputParamGroup inputParamGroupTopic = new InputParamGroup();
            inputParamGroupTopic.getInputParams().add(confirmInputParam);
            inputParams.getInputParamGroups().add(inputParamGroupTopic);
        }

        return inputParams;

    }
}
