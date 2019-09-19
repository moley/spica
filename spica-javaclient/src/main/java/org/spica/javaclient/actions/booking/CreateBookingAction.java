package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.*;
import org.spica.javaclient.actions.topics.CreateTopicAction;
import org.spica.javaclient.model.*;
import org.spica.javaclient.timetracker.TimetrackerCreationParam;
import org.spica.javaclient.timetracker.TimetrackerService;
import org.spica.javaclient.utils.DateUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CreateBookingAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateTopicAction.class);

    public final static String KEY_FROM = "from";
    public final static String KEY_UNTIL = "until";
    public final static String KEY_TYPE = "type";

    public final static String KEY_TOPIC = "topic";

    public final static String KEY_TEXT = "text";
    public final static String KEY_FOREIGN_USER = "user";

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

        EventType eventType = EventType.fromValue(inputParams.getInputParamAsString(KEY_TYPE));
        UserInfo userInfo = (UserInfo) inputParams.getInputParam(KEY_FOREIGN_USER);
        String text = inputParams.getInputParamAsString(KEY_TEXT);

        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setFrom( fromTime);
        timetrackerCreationParam.setUntil(untilTime);
        timetrackerCreationParam.setDate(LocalDate.now());
        timetrackerCreationParam.setEventType(EventType.fromValue(inputParams.getInputParamAsString(KEY_TYPE)));
        timetrackerCreationParam.setTopicInfo((TopicInfo) inputParams.getInputParam(KEY_TOPIC));
        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getModelCacheService());

        if (eventType.equals(EventType.MESSAGE)) {
            if (text != null && ! eventType.equals(EventType.MESSAGE))
                throw new IllegalArgumentException("Text must not be set on event type " + eventType.getValue());

            if (userInfo == null && eventType.equals(EventType.MESSAGE))
                throw new IllegalArgumentException("User must be set on event type " + eventType.getValue());

            //TODO consolidate with StartPhoneCallAction
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setCreator(userInfo.getId());
            messageInfo.setMessage(text);
            messageInfo.setType(MessageType.PHONECALL);
            messageInfo.setId(UUID.randomUUID().toString());
            timetrackerCreationParam.setMessageInfo(messageInfo);
        }

        List<String> output = timetrackerService.createEvent(timetrackerCreationParam);
        System.out.println (String.join("\n", output));

        actionContext.saveModelCache();
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
    public InputParams getInputParams(ActionContext actionContext, String parameterList) {

        //General parameters
        TextInputParam started = new TextInputParam(1, KEY_FROM, "Started ", null);
        TextInputParam stopped = new TextInputParam(1, KEY_UNTIL, "Stopped (maybe empty)", null);
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
                return topicSearch;
            }
        });
        InputParamGroup inputParamGroupTopic = new InputParamGroup("Topic", inputParams -> inputParams.getInputParamAsString(KEY_TYPE).equalsIgnoreCase(EventType.TOPIC.getValue()));
        inputParamGroupTopic.getInputParams().add(topicSearch);

        //Message parameters
        TextInputParam summary = new TextInputParam(1, KEY_TEXT, "Message text: ", null);
        List<UserInfo> userInfoList = actionContext.getModelCache().getUserInfos();
        SearchInputParam<UserInfo> userInfoSearchInputParam = new SearchInputParam<UserInfo>(KEY_FOREIGN_USER, "User", userInfoList, new Renderer<UserInfo>() {
            @Override
            public String toString(UserInfo object) {
                return object.getName() + ", " + object.getFirstname();
            }
        });


        InputParamGroup inputParamGroupMessage = new InputParamGroup("Message", inputParams -> inputParams.getInputParamAsString(KEY_TYPE).equalsIgnoreCase(EventType.MESSAGE.getValue()));
        inputParamGroupMessage.getInputParams().add(summary);
        inputParamGroupMessage.getInputParams().add(userInfoSearchInputParam);

        return new InputParams(Arrays.asList(inputParamGroup, inputParamGroupTopic, inputParamGroupMessage));
    }
}
