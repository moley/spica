package org.spica.cli.actions;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.params.ActionParamFactory;
import org.spica.javaclient.services.ModelCacheService;
import org.spica.javaclient.services.Services;

public class StandaloneActionContext implements ActionContext {

  private final static Logger LOGGER = LoggerFactory.getLogger(StandaloneActionContext.class);

  private SpicaProperties spicaProperties = new SpicaProperties();

  private Services services = new Services();

  private ActionHandler actionHandler = new ActionHandler();

  private ActionParamFactory actionParamFactory;


  @Override public File getCurrentWorkingDir() {
    return new File("").getAbsoluteFile();
  }

  @Override public Services getServices() {
    return services;
  }

  @Override public Model getModel() {
    return services.getModelCacheService().get();
  }


  @Override
  public Model reloadModel () {
    services.getModelCacheService().close();
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
