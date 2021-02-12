package org.spica.fx.controllers;

import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.extern.slf4j.Slf4j;
import org.spica.fx.renderer.TaskTreeItem;
import org.spica.fx.renderer.UserInfoCellFactory;
import org.spica.javaclient.model.UserInfo;

@Slf4j public class UsersController extends AbstractController {
  @FXML private TextField txtDisplayname;
  @FXML private TextField txtSource;
  @FXML private TextField txtPhone;
  @FXML private TextField txtMail;
  @FXML private TextField txtUsername;
  @FXML private TextField txtName;
  @FXML private TextField txtFirstname;
  @FXML private TextField txtUserSearch;
  @FXML private ListView<UserInfo> lviUsers;

  private FilteredList<UserInfo> userInfoFilteredList;

  @FXML public void initialize() {
    lviUsers.setCellFactory(renderer -> new UserInfoCellFactory());
    lviUsers.setOnKeyPressed(event -> {
      if (!lviUsers.getSelectionModel().isEmpty()) {
        UserInfo selectedItem = lviUsers.getSelectionModel().getSelectedItem();

        if (event.getCode().equals(KeyCode.SLASH)) {
          removeUser(selectedItem);
        }
      }

    });

    txtUserSearch.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {

        if (txtUserSearch.getText().trim().isEmpty())
          userInfoFilteredList.setPredicate(null);
        else {
          userInfoFilteredList.setPredicate(new Predicate<UserInfo>() {
            @Override public boolean test(UserInfo userInfo) {
              return userInfo.toString().contains(txtUserSearch.getText());
            }
          });
        }

      }
    });

    lviUsers.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> reloadDetail(newValue));

  }

  public void reloadDetail(final UserInfo userInfo) {
    txtName.setText(userInfo != null ? userInfo.getName() : "");
    txtFirstname.setText(userInfo != null ? userInfo.getFirstname() : "");
    txtUsername.setText(userInfo != null ? userInfo.getUsername() : "");
    txtMail.setText(userInfo != null ? userInfo.getEmail() : "");
    txtPhone.setText(userInfo != null ? userInfo.getPhone() : "");
    txtSource.setText(userInfo != null ? userInfo.getSource() : "");
    txtDisplayname.setText(userInfo != null ? userInfo.getDisplayname(): "");
  }

  public void removeUser(UserInfo userInfo) {
    getModel().getUserInfos().remove(userInfo);
    saveModel("Removed user " + userInfo);
    refreshData();
  }

  @Override public void refreshData() {
    getMainController().refreshData();
    userInfoFilteredList = new FilteredList<UserInfo>(FXCollections.observableArrayList(getModel().getUserInfos()));
    lviUsers.setItems(userInfoFilteredList);

  }
}
