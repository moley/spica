package org.spica.javaclient.actions.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.actions.topics.ImportTopicAction;
import org.spica.javaclient.api.UserApi;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.UserInfo;

import java.util.List;

public class ImportUsersAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImportTopicAction.class);

    private UserApi userApi = new UserApi();


    @Override
    public String getDisplayname() {
        return "Import users";
    }

    @Override
    public String getDescription() {
        return "Imports users from external system";
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        ModelCache modelCache = actionContext.getModelCache();

        try {

            List<UserInfo> userInfos = userApi.getUsers();
            if (userInfos.isEmpty())
                userApi.refreshUsers();

            //TODO make refreshing in server periodically
            userInfos = userApi.getUsers();

            modelCache.setUserInfos(userInfos);
            outputOk("Imported " + userInfos.size() + " users");
        } catch (ApiException e) {
            LOGGER.error("Error importing users: " + e.getResponseBody(), e);
        }
        actionContext.saveModelCache(getClass().getName());
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.ADMIN;
    }

    @Override
    public Command getCommand() {
        return new Command("import", "i");
    }
}