package org.spica.javaclient.actions.links;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.*;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.utils.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ExecuteLinkAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExecuteLinkAction.class);

    public final static  String KEY_CONFIRM = "KEY_CONFIRM";

    private RenderUtil renderUtil = new RenderUtil();

    @Override
    public String getDisplayname() {
        return "Execute links";
    }

    @Override
    public String getDescription() {
        return "Download all links and execute them";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {


        List<LinkInfo> linkInfos = actionContext.getModelCache().findLinkInfosByQuery(parameterList);
        if (linkInfos.isEmpty())
            outputError("No links found for query <" + parameterList + ">");
        else {

            boolean confirmed = inputParams.getInputParamAsBoolean(KEY_CONFIRM, false);
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
