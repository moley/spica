package org.spica.fx.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import org.spica.commons.DashboardItemType;
import org.spica.commons.mail.MailAdapter;
import org.spica.fx.Consts;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.utils.DateUtil;

public class DashboardListCell extends ListCell<DashboardItemInfo> {

  private DateUtil dateUtil = new DateUtil();
  private DashboardFxController controller;
  private ActionContext actionContext;

  public DashboardListCell(DashboardFxController controller, ActionContext actionContext) {
    this.controller = controller;
    this.actionContext = actionContext;
  }

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
      setContextMenu(null);
    } else {

      setText(dateUtil.getDateAndTimeAsString(obj.getCreated()) + " - " + obj.getDescription());
      getChildren().add(new Button("..."));

      ContextMenu contextMenu = new ContextMenu();

      MenuItem menuItemClose = new MenuItem();
      menuItemClose.setText("Close...");
      menuItemClose.setUserData(obj);
      menuItemClose.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent event) {
          MenuItem menuItem = (MenuItem) event.getSource();

          DashboardItemInfo dashboardItemInfo = (DashboardItemInfo) menuItem.getUserData();

          MailAdapter mailAdapter = new MailAdapter();
          mailAdapter.deleteMail(dashboardItemInfo.getItemReference());

          dashboardItemInfo.setOpen(Boolean.FALSE);
          actionContext.saveModel("Closed " + dashboardItemInfo.getId());
          controller.setActionContext(actionContext);
        }
      });

      Button button = new Button();
      button.setStyle("-fx-background-color:transparent; -fx-padding: 3 30 3 10;");
      if (obj.getItemType().equals(DashboardItemType.EVENT.name())) {
        button.setGraphic(Consts.createIcon("fa-book", Consts.ICONSIZE_MENU));
      } else if (obj.getItemType().equals(DashboardItemType.MAIL.name())) {
        button.setGraphic(Consts.createIcon("fa-envelope", Consts.ICONSIZE_MENU));
        contextMenu.getItems().add(menuItemClose);
      }

      if (! contextMenu.getItems().isEmpty())
      setContextMenu(contextMenu);



      setGraphic(button);
    }
  }
}