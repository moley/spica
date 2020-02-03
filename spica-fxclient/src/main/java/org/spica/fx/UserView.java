package org.spica.fx;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class UserView implements View {

  @Override public String getName() {
    return "Users";
  }

  @Override public Pane getPane() {
    VBox vbox = new VBox();
    Label label = new Label();
    label.setText("Users");
    vbox.getChildren().add(label);
    return vbox;
  }

  @Override public String getIcon() {
    return "fa-user";
  }
}
