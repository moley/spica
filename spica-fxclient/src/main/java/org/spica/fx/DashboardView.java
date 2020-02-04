package org.spica.fx;

import javafx.scene.Parent;

public class DashboardView extends AbstractView {
  @Override public String getName() {
    return "Dashboard";
  }

  @Override public Parent getParent() {
    Mask mask = getMask("dashboard");
    return mask.getScene().getRoot();
  }

  @Override public String getIcon() {
    return "fa-dashboard";
  }
}
