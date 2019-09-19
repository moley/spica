package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.*;
import org.spica.javaclient.model.*;
import org.spica.javaclient.timetracker.TimetrackerService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class StartPhonecallAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartPhonecallAction.class);

    public final static String KEY_FOREIGN_USER = "foreignUser";
    public final static String KEY_MESSAGE = "description";


    @Override
    public String getIcon() {
        return "fa-PHONE";
    }

    @Override
    public boolean fromButton() {
        return true;
    }

    @Override
    public String getDisplayname() {
        return "Call";
    }

    @Override
    public String getDescription() {
        return "Starts a phone call";
    }

    public void beforeParam (ActionContext actionContext, String parameterList) {
        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getModelCacheService());
        timetrackerService.startTelephoneCall();
        actionContext.saveModelCache();
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterlist) {
        inputParams.setEndTime(LocalDateTime.now());
        TimetrackerService timetrackerService = new TimetrackerService();

        UserInfo selectedUser = (UserInfo) inputParams.getInputParam(KEY_FOREIGN_USER);
        String message = inputParams.getInputParamAsString(KEY_MESSAGE);

        System.out.println ("Save phonecall with " + selectedUser.getFirstname() + " " + selectedUser.getName() + " with message " + message);

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

        actionContext.saveModelCache();
        LOGGER.info("Start phonecall with parameter " + inputParams);

    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public Command getCommand() {
        return new Command("call", "c");
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, String parameterlist) {

        List<UserInfo> userInfoList = actionContext.getModelCache().getUserInfos();

        SearchInputParam<UserInfo> userInfoSearchInputParam = new SearchInputParam<UserInfo>(KEY_FOREIGN_USER, "User", userInfoList, new Renderer<UserInfo>() {
            @Override
            public String toString(UserInfo object) {
                return object.getName() + ", " + object.getFirstname();
            }
        });

        TextInputParam description = new TextInputParam (5, KEY_MESSAGE, "Description", "");

        InputParamGroup inputParamGroup = new InputParamGroup();
        inputParamGroup.getInputParams().add(userInfoSearchInputParam);
        inputParamGroup.getInputParams().add(description);

        return new InputParams(Arrays.asList(inputParamGroup));
    }
}