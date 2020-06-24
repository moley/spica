package org.spica.fx;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.fx.controllers.AbstractController;

public class Mask<T extends AbstractController> {

  private static final Logger LOGGER = LoggerFactory.getLogger(Mask.class);

  private Parent parent;

  private Scene scene;

  private Stage stage;

  private T controller;

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public Scene getScene() {
    return scene;
  }

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  public void show () {
    LOGGER.info("Show mask for controller " + controller.getClass().getName());
    getStage().show();
  }

  public void setSize (final int width, final int height) {
    getStage().setWidth(width);
    getStage().setHeight(height);
  }

  public void setPosition (final double x, final double y) {
    getStage().setX(x);
    getStage().setY(y);
  }


  public T getController() {
    return controller;
  }

  public void setController(T controller) {
    this.controller = controller;
  }

  public Parent getParent() {
    return parent;
  }

  public void setParent(Parent parent) {
    this.parent = parent;
  }
}
