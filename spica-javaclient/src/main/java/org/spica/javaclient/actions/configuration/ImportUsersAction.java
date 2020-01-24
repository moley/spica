package org.spica.javaclient.actions.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.actions.topics.ImportTopicAction;
import org.spica.javaclient.api.UserApi;
import org.spica.javaclient.model.UserInfo;

import java.util.List;

public class ImportUsersAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImportTopicAction.class);

    private UserApi userApi = new UserApi();

    @Override public String getDisplayname() {
        return "Import users";
    }

    @Override
    public String getDescription() {
        return "Imports users from external system";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        try {

            List<UserInfo> userInfos = userApi.getUsers();
            if (userInfos.isEmpty())
                userApi.refreshUsers();

            //TODO make refreshing in server periodically
            userInfos = userApi.getUsers();

            model.setUserInfos(userInfos);
            outputOk("Imported " + userInfos.size() + " users");
        } catch (ApiException e) {
            LOGGER.error("Error importing users: " + e.getResponseBody(), e);
        }
        actionContext.saveModel(getClass().getName());

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.CONFIGURATION;
    }

    @Override
    public Command getCommand() {
        return new Command("users", "u");
    }
}