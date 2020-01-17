package org.spica.javaclient.actions.booking;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.Renderer;
import org.spica.javaclient.params.SearchInputParam;
import org.spica.javaclient.params.TextInputParam;
import org.spica.javaclient.timetracker.TimetrackerService;
import org.spica.javaclient.utils.RenderUtil;

public class StartPhonecallAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartPhonecallAction.class);

    private RenderUtil renderUtil = new RenderUtil();

    public final static String KEY_FOREIGN_USER = "foreignUser";
    public final static String KEY_MESSAGE = "description";

    @Override public String getDisplayname() {
        return "Start phonecall";
    }

    @Override
    public String getDescription() {
        return "Starts a phone call";
    }

    public void beforeParam(ActionContext actionContext, String parameterList) {
        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getModelCacheService());
        timetrackerService.startTelephoneCall();
        actionContext.saveModelCache(getClass().getName() + "beforeParam");
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
        inputParams.setEndTime(LocalDateTime.now());
        TimetrackerService timetrackerService = new TimetrackerService();

        UserInfo selectedUser = (UserInfo) inputParams.getInputValue(KEY_FOREIGN_USER);
        String message = inputParams.getInputValueAsString(KEY_MESSAGE);

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setCreator(selectedUser.getId());
        messageInfo.setMessage(message);
        messageInfo.setType(MessageType.PHONECALL);
        messageInfo.setId(UUID.randomUUID().toString());

        MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo();
        messagecontainerInfo.setMessage(Arrays.asList(messageInfo));

        ModelCache modelCache = actionContext.getModelCache();
        modelCache.getMessagecontainerInfos().add(messagecontainerInfo);

        timetrackerService.setModelCacheService(actionContext.getModelCacheService());
        timetrackerService.finishTelephoneCall(messageInfo, selectedUser);

        actionContext.saveModelCache(getClass().getName());
        outputOk("Saved phonecall with " + renderUtil.getUser(selectedUser) + " with message " + message);

    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command("call", "a");
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {

        List<UserInfo> userInfoList = actionContext.getModelCache().getUserInfos();

        SearchInputParam<UserInfo> userInfoSearchInputParam = new SearchInputParam<UserInfo>(KEY_FOREIGN_USER, "User", userInfoList, new Renderer<UserInfo>() {
            @Override
            public String toString(UserInfo object) {
                return object.getName() + ", " + object.getFirstname();
            }
        });

        TextInputParam description = new TextInputParam(5, KEY_MESSAGE, "Description");

        InputParamGroup inputParamGroup = new InputParamGroup();
        inputParamGroup.getInputParams().add(userInfoSearchInputParam);
        inputParamGroup.getInputParams().add(description);

        return new InputParams(Arrays.asList(inputParamGroup));
    }
}