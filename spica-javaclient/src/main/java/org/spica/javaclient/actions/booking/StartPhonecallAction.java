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
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.ConfirmInputParam;
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
    public final static String KEY_CONTINUE = "finish";

    @Override public String getDisplayname() {
        return "Start phonecall";
    }

    @Override
    public String getDescription() {
        return "Starts a phone call";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {
        inputParams.setEndTime(LocalDateTime.now());
        TimetrackerService timetrackerService = new TimetrackerService();

        UserInfo selectedUser = (UserInfo) inputParams.getInputValue(KEY_FOREIGN_USER);
        String message = inputParams.getInputValueAsString(KEY_MESSAGE);
        boolean continuePreviousWork = inputParams.getInputValueAsBoolean(KEY_CONTINUE, false);

        MessageInfo messageInfo = new MessageInfo();
        if (selectedUser != null)
          messageInfo.setCreator(selectedUser.getId());
        messageInfo.setMessage(message);
        messageInfo.setType(MessageType.PHONECALL);
        messageInfo.setId(UUID.randomUUID().toString());

        MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo();
        messagecontainerInfo.setMessage(Arrays.asList(messageInfo));

        Model model = actionContext.getModel();
        model.getMessagecontainerInfos().add(messagecontainerInfo);

        timetrackerService.setModelCacheService(actionContext.getServices().getModelCacheService());
        timetrackerService.finishTelephoneCall(messageInfo, selectedUser, continuePreviousWork);

        actionContext.saveModel(getClass().getName());
        outputOk("Saved phonecall with " + renderUtil.getUser(selectedUser) + " with message " + message);
        if (continuePreviousWork)
            outputOk("Previous work is continued");

        return null;

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

        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getServices().getModelCacheService());
        timetrackerService.startTelephoneCall();
        actionContext.saveModel(getClass().getName() + "beforeParam");

        List<UserInfo> userInfoList = actionContext.getModel().getUserInfos();

        SearchInputParam<UserInfo> userInfoSearchInputParam = new SearchInputParam<UserInfo>(KEY_FOREIGN_USER, "User", userInfoList, new Renderer<UserInfo>() {
            @Override
            public String toString(UserInfo object) {
                return object.getName() + ", " + object.getFirstname();
            }
        });

        TextInputParam description = new TextInputParam(5, KEY_MESSAGE, "Description");
        ConfirmInputParam confirmInputParam = new ConfirmInputParam(KEY_CONTINUE, "Continue previous work", null);

        InputParamGroup inputParamGroup = new InputParamGroup();
        inputParamGroup.getInputParams().add(userInfoSearchInputParam);
        inputParamGroup.getInputParams().add(description);
        inputParamGroup.getInputParams().add(confirmInputParam);

        return new InputParams(Arrays.asList(inputParamGroup));
    }
}