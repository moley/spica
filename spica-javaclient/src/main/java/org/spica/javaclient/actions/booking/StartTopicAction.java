package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.Renderer;
import org.spica.javaclient.params.SearchInputParam;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.timetracker.TimetrackerService;
import org.spica.javaclient.utils.RenderUtil;

import java.util.List;

public class StartTopicAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartTopicAction.class);

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
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        ModelCache modelCache = actionContext.getModelCache();
        String query = commandLineArguments.getMandatoryFirstArgument("You have to add a parameter id, name or external system id to your command");
        List<TopicInfo> infos = modelCache.findTopicInfosByQuery(query);
        TopicInfo selectedTopicInfo = infos.size() == 1 ? infos.get(0): (TopicInfo) inputParams.getInputValue(KEY_TOPIC);

        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getModelCacheService());
        timetrackerService.startWorkOnTopic(selectedTopicInfo);
        actionContext.saveModelCache(getClass().getName());



        outputOk("Start work on topic " + renderUtil.getTopic(selectedTopicInfo));
    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    public InputParams getInputParams(ActionContext actionContext, String parameterList) {

        InputParams inputParams = new InputParams();
        ModelCache modelCache = actionContext.getModelCache();
        List<TopicInfo> filteredTopicInfos = modelCache.findTopicInfosByQuery(parameterList);
        if (filteredTopicInfos.size() != 1) {
            List<TopicInfo> allTopicInfos = modelCache.getTopicInfos();
            System.out.println("Did not find exactly one topic with query <" + parameterList + ">");
            SearchInputParam<TopicInfo> topicSearch = new SearchInputParam<TopicInfo>(KEY_TOPIC, "Topic: ", allTopicInfos, new Renderer<TopicInfo>() {
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
