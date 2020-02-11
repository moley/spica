package org.spica.fx.controllers;

import javafx.scene.control.ListCell;
import org.spica.javaclient.model.UserInfo;

public class UserListCellFactory extends ListCell<UserInfo> {

  @Override protected void updateItem(UserInfo item, boolean empty) {
    super.updateItem(item, empty);
    if (item != null) {
      setText(item.getName() + " " + item.getFirstname() + "," + item.getEmail());
    }
    else
      setText("");

  }
}
