package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.*;
import org.spica.javaclient.actions.topics.CreateTopicAction;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.timetracker.TimetrackerCreationParam;
import org.spica.javaclient.timetracker.TimetrackerService;
import org.spica.javaclient.utils.DateUtil;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class CreateBookingAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateTopicAction.class);

    public final static String KEY_FROM = "from";
    public final static String KEY_UNTIL = "until";
    public final static String KEY_TYPE = "type";

    public final static String KEY_TOPIC = "topic";

    public final static String KEY_TEXT = "text";

    private DateUtil dateUtil = new DateUtil();

    @Override
    public String getIcon() {
        return "fa-CLIPBOARD";
    }

    @Override
    public boolean fromButton() {
        return true;
    }

    @Override
    public String getDisplayname() {
        return "Create booking";
    }

    @Override
    public String getDescription() {
        return "Creates a booking";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        LocalTime fromTime = dateUtil.getTime(inputParams.getInputParamAsString(KEY_FROM));
        LocalTime untilTime = inputParams.isAvailable(KEY_UNTIL) ? dateUtil.getTime(inputParams.getInputParamAsString(KEY_UNTIL)): null;

        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setFrom( fromTime);
        timetrackerCreationParam.setUntil(untilTime);
        timetrackerCreationParam.setEventType(EventType.fromValue(inputParams.getInputParamAsString(KEY_TYPE)));
        timetrackerCreationParam.setTopicInfo((TopicInfo) inputParams.getInputParam(KEY_TOPIC));
        timetrackerCreationParam.setMessageText(inputParams.getInputParamAsString(KEY_TEXT));
        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.createEvent(timetrackerCreationParam);
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command ("create", "c");
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext) {

        //General parameters
        TextInputParam started = new TextInputParam(1, KEY_FROM, "Started ", "");
        TextInputParam stopped = new TextInputParam(1, KEY_UNTIL, "Stopped (maybe empty)", "");
        SelectInputParam<EventType> type = new SelectInputParam<EventType>(KEY_TYPE, "Type: ", Arrays.asList(EventType.values()), new Renderer<EventType>() {
            @Override
            public String toString(EventType eventType) {
                return eventType.name();
            }
        });

        InputParamGroup inputParamGroup = new InputParamGroup("Basic");
        inputParamGroup.getInputParams().add(started);
        inputParamGroup.getInputParams().add(stopped);
        inputParamGroup.getInputParams().add(type);

        //Topic parameters
        List<TopicInfo> topicInfos = actionContext.getModelCache().getTopicInfos();
        SearchInputParam<TopicInfo> topicSearch = new SearchInputParam<TopicInfo>(KEY_TOPIC, "Topic: ", topicInfos, new Renderer<TopicInfo>() {
            @Override
            public String toString(TopicInfo topicInfo) {

                String topicSearch = "";
                if (topicInfo.getExternalSystemKey() != null)
                    topicSearch += topicInfo.getExternalSystemKey() + " ";

                topicSearch += topicInfo.getName();
                System.out.println ("Render: " + topicSearch);
                return topicSearch;
            }
        });
        InputParamGroup inputParamGroupTopic = new InputParamGroup("Topic", inputParams -> inputParams.getInputParamAsString(KEY_TYPE).equalsIgnoreCase(EventType.TOPIC.getValue()));
        inputParamGroupTopic.getInputParams().add(topicSearch);

        //Message parameters
        TextInputParam summary = new TextInputParam(1, KEY_TEXT, "Message text: ", "");

        InputParamGroup inputParamGroupMessage = new InputParamGroup("Message", inputParams -> inputParams.getInputParamAsString(KEY_TYPE).equalsIgnoreCase(EventType.MESSAGE.getValue()));
        inputParamGroupMessage.getInputParams().add(summary);

        return new InputParams(Arrays.asList(inputParamGroup, inputParamGroupTopic, inputParamGroupMessage));
    }
}
