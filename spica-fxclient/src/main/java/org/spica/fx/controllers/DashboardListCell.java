package org.spica.fx.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import org.spica.commons.DashboardItemType;
import org.spica.fx.Consts;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.utils.DateUtil;

public class DashboardListCell extends ListCell<DashboardItemInfo> {

  private DateUtil dateUtil = new DateUtil();

  @Override
  public void updateItem(DashboardItemInfo obj, boolean empty) {
    super.updateItem(obj, empty);
    if (obj == null)
      return;

    if (obj.isOpen() == null || obj.isOpen().equals(Boolean.TRUE)) {
      setStyle("-fx-background-color: #5F9EA0;");
    }
    if (empty) {
      setText(null);
      setGraphic(null);
    } else {

      setText(dateUtil.getDateAndTimeAsString(obj.getCreated()) + " - " + obj.getDescription());


      Button button = new Button();
      button.setStyle("-fx-background-color:transparent; -fx-padding: 3 30 3 10;");
      if (obj.getItemType().equals(DashboardItemType.EVENT.name())) {
        button.setGraphic(Consts.createIcon("fa-book", Consts.ICONSIZE_MENU));
      } else if (obj.getItemType().equals(DashboardItemType.MAIL.name())) {
        button.setGraphic(Consts.createIcon("fa-envelope", Consts.ICONSIZE_MENU));
      }

      setGraphic(button);
    }
  }
}