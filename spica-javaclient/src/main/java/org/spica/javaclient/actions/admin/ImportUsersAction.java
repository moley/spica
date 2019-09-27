package org.spica.javaclient.actions.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.actions.topics.ImportTopicAction;
import org.spica.javaclient.api.TopicApi;
import org.spica.javaclient.api.UserApi;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.TopicContainerInfo;
import org.spica.javaclient.model.UserInfo;

import java.util.List;

public class ImportUsersAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImportTopicAction.class);

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
        UserApi userApi = new UserApi();

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
        actionContext.saveModelCache();
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.ADMIN;
    }

    @Override
    public Command getCommand() {
        return new Command ("import", "i");
    }
}