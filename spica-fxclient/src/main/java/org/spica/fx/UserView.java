package org.spica.fx;

import javafx.scene.Parent;

public class UserView extends AbstractView {

  @Override public String getName() {
    return "Users";
  }

  @Override public Parent getParent() {
    Mask mask = getMask("users");
    return mask.getScene().getRoot();
  }

  @Override public String getIcon() {
    return "fa-user";
  }
}
