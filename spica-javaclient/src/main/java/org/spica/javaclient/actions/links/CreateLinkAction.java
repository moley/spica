package org.spica.javaclient.actions.links;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.LinkType;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.Renderer;
import org.spica.javaclient.params.SearchInputParam;
import org.spica.javaclient.params.SelectInputParam;
import org.spica.javaclient.params.TextInputParam;
import org.spica.javaclient.utils.RenderUtil;

public class CreateLinkAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateLinkAction.class);

    public final static String KEY_NAME = "name";
    public final static String KEY_URL = "url";
    public final static String KEY_TYPE = "type";
    public final static String KEY_TOPIC = "topic";
    public final static String KEY_PROJECT = "project";

    @Override public String getDisplayname() {
        return "Create link";
    }

    @Override
    public String getDescription() {
        return "Creates a link (url is taken from clipboard, name from parameter)";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        String name = getFirstValue(commandLineArguments.getOptionalMainArgument(), inputParams.getInputValueAsString(KEY_NAME));
        String url = getFirstValue(inputParams.getInputValueAsString(KEY_URL));
        LinkType selectedLinkType = inputParams.getInputValue(KEY_TYPE, LinkType.class);

        TaskInfo currentTask = model.getCurrentTask() != null ? model.getCurrentTask() : inputParams.getInputValue(KEY_TOPIC, TaskInfo.class);
        ProjectInfo currentProject = inputParams.getInputValue(KEY_PROJECT, ProjectInfo.class);

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setId(UUID.randomUUID().toString());
        linkInfo.setName(name);
        linkInfo.setUrl(url);
        linkInfo.setType(selectedLinkType);

        if (selectedLinkType.equals(LinkType.TOPIC)) {
            linkInfo.setReference(currentTask.getId());
        }
        else if (selectedLinkType.equals(LinkType.PROJECT)) {
            currentProject = model.findProjectInfoById(currentTask.getProjectId());
            if (currentProject == null) {
                outputError("No project reference found, cannot create project related link");
                return null;
            }
            else
                linkInfo.setReference(currentProject.getId());
        }
        else if (selectedLinkType.equals(LinkType.PATH)) {
            linkInfo.setReference(new File ("").getAbsolutePath());
        }
        model.getLinkInfos().add(linkInfo);

        outputOk("Created link " + linkInfo.getName() + " with type " + selectedLinkType.getValue() + "-> " + linkInfo.getUrl() + " (" + linkInfo.getId() + ")");

        actionContext.saveModel(getClass().getName());
        return null;
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
    public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {
        InputParamGroup inputParamGroupGeneric = new InputParamGroup("Generic");

        Model model = actionContext.getModel();

        //General configurations
        TextInputParam name = new TextInputParam(1, KEY_NAME, "Name");
        inputParamGroupGeneric.getInputParams().add(name);

        TextInputParam url = new TextInputParam(1, KEY_URL, "URL", getClipBoard());
        inputParamGroupGeneric.getInputParams().add(url);

        SelectInputParam<LinkType> type = new SelectInputParam<LinkType>(KEY_TYPE, "Type: ", Arrays.asList(LinkType.values()), new Renderer<LinkType>() {
            @Override
            public String toString(LinkType eventType) {
                return eventType.name();
            }
        });
        inputParamGroupGeneric.getInputParams().add(type);

        //Reference Task
        InputParamGroup inputParamGroupReferenceTask = new InputParamGroup("Reference Task", new Predicate<InputParams>() {
            @Override
            public boolean test(InputParams inputParams) {
                LinkType linkType = inputParams.getInputValue(KEY_TYPE, LinkType.class);
                boolean linkTypeNeedsTask = linkType.equals(LinkType.TOPIC) || linkType.equals(LinkType.PROJECT);

                if (linkTypeNeedsTask) {
                    TaskInfo currentTask = model.getCurrentTask();
                    return currentTask == null;
                }
                return false;
            }
        });

        List<TaskInfo> topicInfos = actionContext.getModel().getTaskInfos();
        RenderUtil renderUtil = new RenderUtil();
        SearchInputParam<TaskInfo> topicSearch = new SearchInputParam<TaskInfo>(KEY_TOPIC, "Task: ", topicInfos, new Renderer<TaskInfo>() {
            @Override
            public String toString(TaskInfo topicInfo) {
                return renderUtil.getTask(topicInfo);
            }
        });
        inputParamGroupReferenceTask.getInputParams().add(topicSearch);

        //Reference Project
        InputParamGroup inputParamGroupReferenceProject = new InputParamGroup("Reference Task", new Predicate<InputParams>() {
            @Override
            public boolean test(InputParams inputParams) {
                LinkType linkType = inputParams.getInputValue(KEY_TYPE, LinkType.class);
                boolean linkTypeNeedsProject = linkType.equals(LinkType.PROJECT);

                if (linkTypeNeedsProject) {
                    TaskInfo currentTask = model.getCurrentTask();
                    return currentTask == null || currentTask.getProjectId() == null;
                }
                return false;
            }
        });

        List<ProjectInfo> projectInfos = actionContext.getModel().getProjectInfos();
        SearchInputParam<ProjectInfo> projectSearch = new SearchInputParam<ProjectInfo>(KEY_PROJECT, "Project: ", projectInfos, new Renderer<ProjectInfo>() {
            @Override
            public String toString(ProjectInfo projectInfo) {
                return renderUtil.getProject(projectInfo);
            }
        });
        inputParamGroupReferenceProject.getInputParams().add(projectSearch);

        InputParams inputParams = new InputParams(Arrays.asList(inputParamGroupGeneric, inputParamGroupReferenceTask, inputParamGroupReferenceProject));

        return inputParams;
    }
}
