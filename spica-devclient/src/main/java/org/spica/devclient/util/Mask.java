package org.spica.devclient.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mask<T> {


  private Parent node;

  private T controller;

  public T getController() {
    return controller;
  }

  public void setController(T controller) {
    this.controller = controller;
  }

  public Parent getNode() {
    return node;
  }

  public void setNode(Parent node) {
    this.node = node;
  }
}
