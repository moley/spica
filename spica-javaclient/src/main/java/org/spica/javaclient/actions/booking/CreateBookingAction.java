package org.spica.javaclient.actions.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.Renderer;
import org.spica.javaclient.params.SearchInputParam;
import org.spica.javaclient.params.SelectInputParam;
import org.spica.javaclient.params.TextInputParam;
import org.spica.javaclient.timetracker.TimetrackerCreationParam;
import org.spica.javaclient.timetracker.TimetrackerService;
import org.spica.javaclient.utils.DateUtil;
import org.spica.javaclient.utils.RenderUtil;

public class CreateBookingAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateBookingAction.class);

    public final static String KEY_DAY = "day";
    public final static String KEY_FROM = "from";
    public final static String KEY_UNTIL = "until";
    public final static String KEY_TYPE = "type";

    public final static String KEY_TOPIC = "topic";

    public final static String KEY_TEXT = "text";
    public final static String KEY_FOREIGN_USER = "user";

    private DateUtil dateUtil = new DateUtil();

    @Override public String getDisplayname() {
        return "Create booking";
    }

    @Override
    public String getDescription() {
        return "Creates a booking";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        LocalDate date = dateUtil.getDate(inputParams.getInputValueAsString(KEY_DAY));

        LocalTime fromTime = dateUtil.getTime(inputParams.getInputValueAsString(KEY_FROM));
        LocalTime untilTime = inputParams.isAvailable(KEY_UNTIL) ? dateUtil.getTime(inputParams.getInputValueAsString(KEY_UNTIL)): null;

        EventType eventType = EventType.fromValue(inputParams.getInputValueAsString(KEY_TYPE));
        UserInfo userInfo = inputParams.getInputValue(KEY_FOREIGN_USER, UserInfo.class);
        String text = inputParams.getInputValueAsString(KEY_TEXT);

        TimetrackerCreationParam timetrackerCreationParam = new TimetrackerCreationParam();
        timetrackerCreationParam.setFrom( fromTime);
        timetrackerCreationParam.setUntil(untilTime);
        timetrackerCreationParam.setDate(date);
        timetrackerCreationParam.setEventType(EventType.fromValue(inputParams.getInputValueAsString(KEY_TYPE)));
        timetrackerCreationParam.setTaskInfo(inputParams.getInputValue(KEY_TOPIC, TaskInfo.class));
        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getServices().getModelCacheService());

        if (eventType.equals(EventType.MESSAGE)) {
            if (text != null && ! eventType.equals(EventType.MESSAGE))
                throw new IllegalArgumentException("Text must not be set on event type " + eventType.getValue());

            //TODO consolidate with StartPhoneCallAction
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setCreatorId(userInfo != null ? userInfo.getId(): null);
            messageInfo.setMessage(text);
            messageInfo.setType(MessageType.PHONECALL);
            messageInfo.setId(UUID.randomUUID().toString());
            timetrackerCreationParam.setMessageInfo(messageInfo);
        }

        List<String> output = timetrackerService.createEvent(timetrackerCreationParam);
        outputDefault(String.join("\n", output));

        actionContext.saveModel(getClass().getName());
        return null;
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
    public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {

        //General parameters
        TextInputParam day = new TextInputParam(1, KEY_DAY, "Day (let empty for today)");
        TextInputParam started = new TextInputParam(1, KEY_FROM, "Started ");
        TextInputParam stopped = new TextInputParam(1, KEY_UNTIL, "Stopped (maybe empty)");
        SelectInputParam<EventType> type = new SelectInputParam<EventType>(KEY_TYPE, "Type: ", Arrays.asList(EventType.values()), new Renderer<EventType>() {
            @Override
            public String toString(EventType eventType) {
                return eventType.name();
            }
        });

        InputParamGroup inputParamGroup = new InputParamGroup("Basic");
        inputParamGroup.getInputParams().add(day);
        inputParamGroup.getInputParams().add(started);
        inputParamGroup.getInputParams().add(stopped);
        inputParamGroup.getInputParams().add(type);

        //Task parameters
        List<TaskInfo> topicInfos = actionContext.getModel().getTaskInfos();
        RenderUtil renderUtil = new RenderUtil();
        SearchInputParam<TaskInfo> topicSearch = new SearchInputParam<TaskInfo>(KEY_TOPIC, "Task: ", topicInfos, new Renderer<TaskInfo>() {
            @Override
            public String toString(TaskInfo topicInfo) {
                return renderUtil.getTask(topicInfo);
            }
        });
        InputParamGroup inputParamGroupTask = new InputParamGroup("Task", inputParams -> inputParams.getInputValueAsString(KEY_TYPE).equalsIgnoreCase(EventType.TOPIC.getValue()));
        inputParamGroupTask.getInputParams().add(topicSearch);

        //Message parameters
        TextInputParam summary = new TextInputParam(1, KEY_TEXT, "Message text: ");
        List<UserInfo> userInfoList = actionContext.getModel().getUserInfos();
        SearchInputParam<UserInfo> userInfoSearchInputParam = new SearchInputParam<UserInfo>(KEY_FOREIGN_USER, "User", userInfoList, new Renderer<UserInfo>() {
            @Override
            public String toString(UserInfo object) {
                return object.getName() + ", " + object.getFirstname();
            }
        });


        InputParamGroup inputParamGroupMessage = new InputParamGroup("Message", inputParams -> inputParams.getInputValueAsString(KEY_TYPE).equalsIgnoreCase(EventType.MESSAGE.getValue()));
        inputParamGroupMessage.getInputParams().add(summary);
        inputParamGroupMessage.getInputParams().add(userInfoSearchInputParam);

        return new InputParams(Arrays.asList(inputParamGroup, inputParamGroupTask, inputParamGroupMessage));
    }
}
