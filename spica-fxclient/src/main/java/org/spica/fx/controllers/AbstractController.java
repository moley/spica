package org.spica.fx.controllers;

import java.util.HashMap;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.fx.ApplicationContext;
import org.spica.fx.Mask;
import org.spica.fx.MaskLoader;
import org.spica.fx.ScreenManager;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.Model;

public abstract class AbstractController {

  private static HashMap<Pages, Mask> registeredPanes = new HashMap<Pages, Mask>();

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

  private BorderPane paRootPane;

  private ActionContext actionContext;

  private ApplicationContext applicationContext;



  public void registerPane (final Pages pages) {
    MaskLoader maskLoader = new MaskLoader();
    try {
      Mask mask = maskLoader.load(pages.getFilename());
      registeredPanes.put(pages, mask);
    } catch (Exception e) {
      LOGGER.error("Error loading page " + pages.getFilename(), e);
    }

  }

  public Mask getMask (final Pages pages) {
    return registeredPanes.get(pages);
  }

  public void stepToPane (final Pages page) {
    LOGGER.info("step to pane " +  page.getDisplayname());
    for (Mask nextMask : registeredPanes.values()) {
      nextMask.getParent().setVisible(false);
    }

    Mask mask = registeredPanes.get(page);
    paRootPane.setCenter(mask.getParent());

    AbstractController controller = mask.getController();

    controller.setPaRootPane(getPaRootPane());
    controller.setActionContext(getActionContext());
    controller.setApplicationContext(getApplicationContext());

    controller.refreshData();

    mask.getParent().setVisible(true);
  }

  public abstract void refreshData();

  public BorderPane getPaRootPane() {
    return paRootPane;
  }

  public void setPaRootPane(BorderPane paRootPane) {
    this.paRootPane = paRootPane;
  }

  public ActionContext getActionContext() {
    return actionContext;
  }

  public Model getModel () {
    return actionContext != null ? actionContext.getModel(): null;
  }

  public void saveModel (final String lastAction) {
    actionContext.saveModel(lastAction);
  }

  public void setActionContext(ActionContext actionContext) {
    this.actionContext = actionContext;
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
