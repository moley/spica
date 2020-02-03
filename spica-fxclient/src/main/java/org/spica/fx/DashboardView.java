package org.spica.fx;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DashboardView implements View {
  @Override public String getName() {
    return "Dashboard";
  }

  @Override public Pane getPane() {
    VBox vbox = new VBox();
    Label label = new Label();
    label.setText("Dashboard");
    vbox.getChildren().add(label);
    return vbox;
  }

  @Override public String getIcon() {
    return "fa-dashboard";
  }
}
