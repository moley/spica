package org.spica.cli.actions;

import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.ApiException;
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

public class StandaloneActionContext implements ActionContext {

  private final static Logger LOGGER = LoggerFactory.getLogger(StandaloneActionContext.class);

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
    LOGGER.info("refresh server data from " + getApi().getCurrentServer());

    try {
      getModel().setUserInfos(getApi().getUserApi().getUsers());
    } catch (ApiException e) {
      LOGGER.info("Exception when reading users: " + e.getLocalizedMessage(), e);
    }

    String usermail = getProperties().getValueNotNull("spica.cli.usermail");
    getModel().setMe(getModel().findUserByMail(usermail));

    UserApi userApi = getApi().getUserApi();
    try {
      List<SkillInfo> skills = userApi.getSkills();
      LOGGER.info("Reloaded " + skills.size() + " skills");
      getModel().setAllSkills(skills);
    } catch (ApiException e) {
      LOGGER.info("Exception when reading all skills: " + e.getLocalizedMessage(), e);
    }

    try {
      getModel().setUserSkills(getApi().getUserApi().getUserSkills(getModel().getMe().getId()));
    } catch (ApiException e) {
      LOGGER.info("Exception when reading userskills of me: " + e.getLocalizedMessage(), e);
    }

  }

  @Override public Model getModel() {
    return services.getModelCacheService().get();
  }
  

  @Override public void saveModel(String lastAction) {
    ModelCacheService modelCacheService = services.getModelCacheService();
    Model model = modelCacheService.get();
    modelCacheService.set(model, lastAction);
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
