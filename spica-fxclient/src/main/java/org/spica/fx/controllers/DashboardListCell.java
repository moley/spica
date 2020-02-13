package org.spica.fx.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import org.spica.commons.DashboardItemType;
import org.spica.fx.Consts;
import org.spica.javaclient.model.DashboardItemInfo;

public class DashboardListCell extends ListCell<DashboardItemInfo> {
  @Override
  public void updateItem(DashboardItemInfo obj, boolean empty) {
    super.updateItem(obj, empty);
    if (empty) {
      setText(null);
      setGraphic(null);
    } else {
      setText(obj.getDescription());

      Button button = new Button();
      if (obj.getItemType().equals(DashboardItemType.EVENT.name())) {
        button.setGraphic(Consts.createIcon("fa-book", Consts.ICONSIZE_MENU));
      }

      setGraphic(button);
    }
  }
}