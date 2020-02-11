package org.spica.fx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;

public class UserFxController extends AbstractFxController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserFxController.class);


  @FXML private TextField txtUserSearch;

  @FXML private ListView<UserInfo> lviUsers;

  private FilteredList<UserInfo> filteredData;

  @FXML
  public void initialize () {
    txtUserSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        System.out.println ("event keyreleased " + event.getCode() + " notified at txtSearchQuery (" + txtUserSearch.getText() + ")");
        if (event.getCode().equals(KeyCode.ESCAPE)) {
          filteredData.setPredicate(s-> true);
          txtUserSearch.setText("");
        }
      }
    });
    txtUserSearch.textProperty().addListener(obs->{

      String filter = txtUserSearch.getText();

      if(filter == null || filter.length() == 0) {
        System.out.println ("Reset filter");
        filteredData.setPredicate(s -> true);
      }
      else {
        System.out.println ("Set filter with token " + filter);
        filteredData.setPredicate(s -> {
          //System.out.println ("Check " + s.getName() + "-" + s.getFirstname());
          return (s.getName().toUpperCase().contains(filter.toUpperCase()) || s.getFirstname().toUpperCase().contains(filter.toUpperCase()));
        });
      }
    });

  }

  public void setActionContext(ActionContext actionContext) {
    super.setActionContext(actionContext);

    Model model = actionContext.getModel();
    filteredData = new FilteredList<UserInfo>(FXCollections.observableArrayList(model.getUserInfos()), s -> true);
    lviUsers.setItems(filteredData);
    lviUsers.setCellFactory(new Callback<ListView<UserInfo>, ListCell<UserInfo>>() {
      @Override
      public ListCell<UserInfo> call(ListView<UserInfo> studentListView) {
        return new UserListCellFactory();
      }
    });








  }
}
