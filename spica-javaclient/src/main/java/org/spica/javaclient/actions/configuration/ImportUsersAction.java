package org.spica.javaclient.actions.configuration;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.api.UserApi;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

@Slf4j
public class ImportUsersAction extends AbstractAction {

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
            log.error("Error importing users: " + e.getResponseBody(), e);
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