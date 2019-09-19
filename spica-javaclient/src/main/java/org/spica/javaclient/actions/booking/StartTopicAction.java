package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParamGroup;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.actions.params.Renderer;
import org.spica.javaclient.actions.params.SearchInputParam;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.timetracker.TimetrackerService;

import java.util.List;

public class StartTopicAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartTopicAction.class);

    public final static String KEY_TOPIC = "topic";

    @Override
    public String getIcon() {
        return "fa-PHONE"; //TODO
    }

    @Override
    public boolean fromButton() {
        return false; //TODO
    }

    @Override
    public String getDisplayname() {
        return "Start topic";
    }

    @Override
    public String getDescription() {
        return "Starts to work on a topic";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterlist) {

        ModelCache modelCache = actionContext.getModelCache();
        List<TopicInfo> infos = modelCache.findTopicInfosByQuery(parameterlist);
        TopicInfo selectedTopicInfo = infos.size() == 1 ? infos.get(0): (TopicInfo) inputParams.getInputParam(KEY_TOPIC);

        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getModelCacheService());
        timetrackerService.startWorkOnTopic(selectedTopicInfo);
        actionContext.saveModelCache();

        System.out.println("Start work on topic " + selectedTopicInfo.getExternalSystemKey() + "-" + selectedTopicInfo.getName());
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
        return new Command("start", "s");
    }
}
