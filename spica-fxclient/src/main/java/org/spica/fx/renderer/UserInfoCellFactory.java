package org.spica.fx.renderer;

import javafx.scene.control.ListCell;
import org.spica.fx.Consts;
import org.spica.javaclient.model.UserInfo;

public class UserInfoCellFactory extends ListCell<UserInfo> {

  @Override protected void updateItem(UserInfo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {

      String name = item.getDisplayname() != null ? item.getDisplayname(): item.getEmail();
      setText(name + (item.getSource() != null ? "(" + item.getSource() + ")": ""));
      setGraphic(Consts.createIcon("fa-user", Consts.ICON_SIZE_MEDIUM));
    }
  }
}
