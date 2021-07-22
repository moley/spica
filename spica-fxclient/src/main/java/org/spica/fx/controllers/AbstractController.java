package org.spica.fx.controllers;

import java.util.HashMap;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.spica.fx.ApplicationContext;
import org.spica.fx.Mask;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.services.Services;

@Slf4j
public abstract class AbstractController {



  private static HashMap<Pages, Mask> registeredMasks = new HashMap<Pages, Mask>();


  private BorderPane paRootPane;

  private ActionContext actionContext;

  private ApplicationContext applicationContext;

  private MainController mainController;

  private Stage stage;

  public Mask getMask (final Pages pages) {
    return registeredMasks.get(pages);
  }

  public void stepToPane (final Pages page) {
    stepToPane(page, true);
  }

  public void stepToPane (final Pages page, final boolean foldedOut) {
    log.info("step to pane " +  page.getDisplayname());
    for (Mask nextMask : registeredMasks.values()) {
      nextMask.getParent().setVisible(false);
    }

    Mask mask = registeredMasks.get(page);
    paRootPane.setCenter(mask.getParent());

    AbstractController controller = mask.getController();
    controller.refreshData();
    controller.getMainController().toggleVisibility(foldedOut);

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
  public Services getServices () {
    return actionContext != null ? actionContext.getServices(): null;
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

  public static HashMap<Pages, Mask> getRegisteredMasks() {
    return registeredMasks;
  }

  public MainController getMainController() {
    return mainController;
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
