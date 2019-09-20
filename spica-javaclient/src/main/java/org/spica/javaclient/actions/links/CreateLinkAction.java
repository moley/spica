package org.spica.javaclient.actions.links;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.*;
import org.spica.javaclient.actions.projects.CreateProjectAction;
import org.spica.javaclient.links.LinkFinder;
import org.spica.javaclient.model.*;
import org.spica.javaclient.utils.RenderUtil;

import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class CreateLinkAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateProjectAction.class);

    public final static String KEY_NAME = "name";
    public final static String KEY_URL = "url";
    public final static String KEY_TYPE = "type";
    public final static String KEY_TOPIC = "topic";

    private String clipboard;

    @Override
    public String getDisplayname() {
        return "Create link";
    }

    @Override
    public String getDescription() {
        return "Creates a link (url is taken from clipboard, name from parameter)";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        ModelCache modelCache = actionContext.getModelCache();

        String name = getFirstValue(parameterList, inputParams.getInputParamAsString(KEY_NAME));
        String url = getFirstValue(clipboard, inputParams.getInputParamAsString(KEY_URL));
        LinkType selectedLinkType = inputParams.getInputParam(KEY_TYPE, LinkType.class);

        TopicInfo currentTopic = modelCache.getCurrentTopic() != null ? modelCache.getCurrentTopic() : inputParams.getInputParam(KEY_TOPIC, TopicInfo.class);

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setId(UUID.randomUUID().toString());
        linkInfo.setName(name);
        linkInfo.setUrl(url);
        linkInfo.setType(selectedLinkType);

        if (selectedLinkType.equals(LinkType.TOPIC)) {
            linkInfo.setReference(currentTopic.getId());
        }
        else if (selectedLinkType.equals(LinkType.PROJECT)) {
            if (currentTopic.getProject() == null)
                outputError("Current topic does not have project reference, cannot create project related link");
            else
                linkInfo.setReference(currentTopic.getProject().getId());
        }
        else if (selectedLinkType.equals(LinkType.PATH)) {
            linkInfo.setReference(new File ("").getAbsolutePath());
        }
        modelCache.getLinkInfos().add(linkInfo);

        outputOk("Created link " + linkInfo.getName() + " with type " + selectedLinkType.getValue() + "-> " + linkInfo.getUrl() + " (" + linkInfo.getId() + ")");

        actionContext.saveModelCache();
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.LINKS;
    }

    @Override
    public Command getCommand() {
        return new Command ("create", "c");
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, String paramList) {


        clipboard = getClipBoard();

        System.out.println ("Clipboard: " + clipboard);
        System.out.println ("Parameter: " + paramList);

        InputParamGroup inputParamGroupGeneric = new InputParamGroup("Generic");

        ModelCache modelCache = actionContext.getModelCache();

        //General configurations
        if (paramList.trim().isBlank()) {
            TextInputParam name = new TextInputParam(1, KEY_NAME, "Name", "");
            inputParamGroupGeneric.getInputParams().add(name);
        }
        if (clipboard == null || clipboard.trim().isBlank()) {
            TextInputParam name = new TextInputParam(1, KEY_URL, "URL", "");
            inputParamGroupGeneric.getInputParams().add(name);
        }

        SelectInputParam<LinkType> type = new SelectInputParam<LinkType>(KEY_TYPE, "Type: ", Arrays.asList(LinkType.values()), new Renderer<LinkType>() {
            @Override
            public String toString(LinkType eventType) {
                return eventType.name();
            }
        });
        inputParamGroupGeneric.getInputParams().add(type);

        //Reference
        InputParamGroup inputParamGroupReference = new InputParamGroup("Reference", new Predicate<InputParams>() {
            @Override
            public boolean test(InputParams inputParams) {
                LinkType linkType = inputParams.getInputParam(KEY_TYPE, LinkType.class);
                boolean linkTypeNeedsTopic = linkType.equals(LinkType.TOPIC) || linkType.equals(LinkType.PROJECT);

                if (linkTypeNeedsTopic) {
                    TopicInfo currentTopic = modelCache.getCurrentTopic();
                    return currentTopic == null;
                }
                return false;
            }
        });

        List<TopicInfo> topicInfos = actionContext.getModelCache().getTopicInfos();
        RenderUtil renderUtil = new RenderUtil();
        SearchInputParam<TopicInfo> topicSearch = new SearchInputParam<TopicInfo>(KEY_TOPIC, "Topic: ", topicInfos, new Renderer<TopicInfo>() {
            @Override
            public String toString(TopicInfo topicInfo) {
                return renderUtil.getTopic(topicInfo);
            }
        });
        inputParamGroupReference.getInputParams().add(topicSearch);

        InputParams inputParams = new InputParams(Arrays.asList(inputParamGroupGeneric, inputParamGroupReference));

        return inputParams;
    }
}
