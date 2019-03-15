package org.spica.devclient.util;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mask<T> {

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
}
