package org.spica.fx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import org.spica.fx.renderer.UserInfoCellFactory;
import org.spica.javaclient.model.UserInfo;

public class SearchboxController extends AbstractController {
  public TextField txtSearch;
  public ListView<UserInfo> lviSearchResults;
  public VBox panDialog;

  private FilteredList<UserInfo> filteredList;

  private UserInfo selectedUser;

  @FXML public void initialize() {
    lviSearchResults.setCellFactory(cellfactory -> new UserInfoCellFactory());

  }

  @Override public void refreshData() {

    selectedUser = null;
    filteredList = new FilteredList<>(FXCollections.observableArrayList(getModel().getUserInfos()), userInfo -> true);
    lviSearchResults.setItems(filteredList);

    txtSearch.setOnKeyPressed(event -> {
      if (event.getCode().equals(KeyCode.DOWN))
        lviSearchResults.requestFocus();
    });
    txtSearch.setOnKeyTyped(event -> {

      if (event.getCode().equals(KeyCode.ENTER) && lviSearchResults.getItems().size() == 1) {
        event.consume();
      }

      filteredList.setPredicate(userInfo -> (userInfo.getEmail() != null && userInfo.getEmail().contains(txtSearch.getText())) || (userInfo
          .getDisplayname() != null && userInfo.getDisplayname().contains(txtSearch.getText())));
    });

    lviSearchResults.setOnKeyPressed(event -> {
      if (event.getCode().equals(KeyCode.ENTER)) {
        select(lviSearchResults.getSelectionModel().getSelectedItem());
      }

    });

  }

  public void select(final UserInfo userInfo) {
    selectedUser = userInfo;
    panDialog.setVisible(false);

  }

  public UserInfo getSelectedUser() {
    return selectedUser;
  }
}
