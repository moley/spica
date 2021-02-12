package org.spica.javaclient;

import com.sun.jersey.core.util.Base64;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.Api;
import org.spica.javaclient.api.UserApi;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.SkillInfo;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.params.ActionParamFactory;
import org.spica.javaclient.services.ModelCacheService;
import org.spica.javaclient.services.Services;
import org.spica.javaclient.services.UserDisplayName;

@Slf4j
public class StandaloneActionContext implements ActionContext {


  private SpicaProperties spicaProperties = new SpicaProperties();

  private Services services = new Services();

  private ActionHandler actionHandler = new ActionHandler();


  private ActionParamFactory actionParamFactory;

  private Api api = new Api();


  @Override public File getCurrentWorkingDir() {
    return new File("").getAbsoluteFile();
  }

  @Override public Services getServices() {
    return services;
  }

  @Override public Api getApi() {
    return api;
  }

  public void refreshServer () {
    //refresh all skills


    try {
      UserDisplayName userDisplayName = new UserDisplayName();

      String username = getProperties().getValueNotNull("spica.cli.username");
      String password = getProperties().getValueNotNull("spica.cli.password");
      String basePath = getProperties().getValueNotNull("spica.cli.serverurl");

      ApiClient apiClient = new ApiClient();
      apiClient.setBasePath(basePath);
      String encodedAuth = new String (Base64.encode(username + ":" + password));
      apiClient.addDefaultHeader("Authorization", "Basic " + encodedAuth);
      getApi().setApiClient(apiClient);

      log.info("refresh server data from " + getApi().getCurrentServer());

      List<UserInfo> infosFromApi = getModel().findUsersBySource("api");
      getModel().getUserInfos().removeAll(infosFromApi);
      log.info("Deleted " + infosFromApi.size() + " users");

      List<UserInfo> users = getApi().getUserApi().getUsers();
      log.info("Refreshing " + users.size() + " users");
      for (UserInfo next: users) {
        next.setSource("api");
        String newDisplayname = userDisplayName.getDisplayname(next);
        if (next.getDisplayname() == null || ! newDisplayname.equals(next.getDisplayname())) {
          next.setDisplayname(userDisplayName.getDisplayname(next));
        }
      }
      getModel().getUserInfos().addAll(users);
    } catch (ApiException e) {
      log.error("Exception when reading users: " + e.getCode() + ":" + e.getResponseBody() + ":" + e.getLocalizedMessage(), e);
      throw new IllegalStateException(e);
    }

    String usermail = getProperties().getValueNotNull("spica.cli.usermail");
    UserInfo meUser = getModel().findUserByMail(usermail);
    if (meUser == null) {
      String message = "User with mail adress " + usermail + " not found, can not detect me. Configure a valid user in property 'spica.cli.usermail'";
      log.error(message);
      System.exit(1); //TODO message

    }

    //set me
    getModel().setMe(meUser);

    //refresh skills
    UserApi userApi = getApi().getUserApi();
    try {
      List<SkillInfo> skills = userApi.getSkills();
      log.info("Reloaded " + skills.size() + " skills");
      getModel().setAllSkills(skills);
    } catch (ApiException e) {
      log.info("Exception when reading all skills: " + e.getLocalizedMessage(), e);
    }

    try {
      getModel().setUserSkills(getApi().getUserApi().getUserSkills(getModel().getMe().getId()));
    } catch (ApiException e) {
      log.info("Exception when reading userskills of me: " + e.getLocalizedMessage(), e);
    }

    services.getModelCacheService().save(getModel(), "Refresh api data at application start");

  }

  @Override public Model getModel() {
    return services.getModelCacheService().load();
  }
  

  @Override public void saveModel(String lastAction) {
    ModelCacheService modelCacheService = services.getModelCacheService();
    Model model = modelCacheService.load();
    modelCacheService.save(model, lastAction);
  }

  @Override public SpicaProperties getProperties() {
    return spicaProperties;
  }

  @Override public ActionHandler getActionHandler() {
    return actionHandler;
  }

  @Override public ActionParamFactory getActionParamFactory() {
    return actionParamFactory;
  }

  public void setActionParamFactory (final ActionParamFactory actionParamFactory) {
    this.actionParamFactory = actionParamFactory;
  }

  public void setActionHandler(ActionHandler actionHandler) {
    this.actionHandler = actionHandler;
  }

}
