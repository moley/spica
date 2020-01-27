package org.spica.javaclient.actions.booking;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.topics.CreateTopicAction;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.Renderer;
import org.spica.javaclient.params.SelectInputParam;
import org.spica.javaclient.timetracker.TimetrackerService;
import org.spica.javaclient.utils.RenderUtil;

public class StartTopicAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartTopicAction.class);

    private final static String SPECIAL_TASK_NEW = "_NEW";

    private RenderUtil renderUtil = new RenderUtil();

    public final static String KEY_TOPIC = "topic";

    @Override public String getDisplayname() {
        return "Start topic";
    }

    @Override
    public String getDescription() {
        return "Starts to work on a topic";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        String query = commandLineArguments.getOptionalMainArgument();
        List<TopicInfo> infos = model.findTopicInfosByQuery(query);
        TopicInfo selectedTopicInfo = infos.size() == 1 ? infos.get(0): (TopicInfo) inputParams.getInputValue(KEY_TOPIC);

        String context = "existing";
        if (selectedTopicInfo.getId().equals(SPECIAL_TASK_NEW)) {
            FoundAction foundAction = actionContext.getActionHandler().findAction(CreateTopicAction.class);
            ActionResult actionResult = actionContext.getActionHandler().handleAction(actionContext, foundAction);
            selectedTopicInfo = (TopicInfo) actionResult.getUserObject();
            context = "created";
        }

        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getServices().getModelCacheService());
        timetrackerService.startWorkOnTopic(selectedTopicInfo);
        actionContext.saveModel(getClass().getName());

        outputOk("Start work on " + context + " topic " + renderUtil.getTopic(selectedTopicInfo));

        return null;
    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, CommandLineArguments parameterList) {

        InputParams inputParams = new InputParams();
        Model model = actionContext.getModel();
        List<TopicInfo> filteredTopicInfos = model.findTopicInfosByQuery(parameterList.getOptionalMainArgument());
        if (filteredTopicInfos.size() != 1) {
            List<TopicInfo> allTopicInfosForSelection = new ArrayList<>();
            allTopicInfosForSelection.addAll(model.getTopicInfos());
            allTopicInfosForSelection.add(new TopicInfo().name("Create new task").id(SPECIAL_TASK_NEW));

            System.out.println("Did not find exactly one topic with query <" + parameterList + ">");
            SelectInputParam<TopicInfo> topicSearch = new SelectInputParam<TopicInfo>(KEY_TOPIC, "Topic: ", allTopicInfosForSelection, new Renderer<TopicInfo>() {
                @Override
                public String toString(TopicInfo topicInfo) {

                    String topicSearch = "";
                    if (topicInfo.getExternalSystemKey() != null)
                        topicSearch += topicInfo.getExternalSystemKey() + " ";

                    topicSearch += topicInfo.getName();
                    return topicSearch;
                }
            });
            InputParamGroup inputParamGroupTopic = new InputParamGroup("Topic");
            inputParamGroupTopic.getInputParams().add(topicSearch);
            inputParams.getInputParamGroups().add(inputParamGroupTopic);
        }

        return inputParams;
    }

    @Override
    public Command getCommand() {
        return new Command("topic", "t");
    }
}
