package org.spica.fx.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.api.UserApi;
import org.spica.javaclient.model.SkillInfo;
import org.spica.javaclient.model.UserInfo;

public class MeFxController extends AbstractFxController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MeFxController.class);

  @FXML private TextField txtSkills;

  @FXML private ListView<SkillInfo> lviUserSkills;

  @FXML private Button btnSave;

  private ObservableList<SkillInfo> userSkills;

  @FXML public void initialize() {

    userSkills = FXCollections.observableList(new ArrayList<SkillInfo>());
    lviUserSkills.setItems(userSkills);
    lviUserSkills.setCellFactory(param -> new ListCell<SkillInfo>() {
      @Override protected void updateItem(SkillInfo item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null || item.getName() == null) {
          setText(null);
        } else {
          setText(item.getName());
        }
      }

    });

    txtSkills.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.DOWN)) {
          lviUserSkills.requestFocus();
          lviUserSkills.getSelectionModel().selectFirst();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
          String newOne = txtSkills.getText();
          LOGGER.info("Want to create a new skill " + newOne + "?");

          SkillInfo foundSkillInfo = getActionContext().getModel().findSkillByName(newOne);
          if (foundSkillInfo == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("The skill '" + newOne + "' does not exist.");
            alert.setContentText("Do you want to create a new skill with this name?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

              UserApi userApi = getActionContext().getApi().getUserApi();
              try {
                foundSkillInfo = userApi.createSkill(newOne);

                if (foundSkillInfo != null) {
                  LOGGER.info("Add a new skill " + foundSkillInfo.getName() + " to all skills");
                  getActionContext().getModel().getAllSkills().add(foundSkillInfo);
                }
              } catch (ApiException e) {
                LOGGER.error(e.getLocalizedMessage(), e);
              }

              alert.close();

            }
          }

          if (foundSkillInfo != null) {
            LOGGER.info("Add skill " + foundSkillInfo.getName() + " to userskills (before: " + userSkills.size() + ") -> " + getActionContext().getModel().getUserSkills().size());
            userSkills.add(foundSkillInfo);
            getActionContext().getModel().setUserSkills(userSkills);
            LOGGER.info("Added skill " + foundSkillInfo.getName() + " to userskills (afterwards: " + userSkills.size() + ") -> " + getActionContext().getModel().getUserSkills().size());
          }


          txtSkills.setText("");
          txtSkills.requestFocus();
        }
      }
    });

    lviUserSkills.getItems().addListener(new ListChangeListener() {
      @Override
      public void onChanged(ListChangeListener.Change change) {
        UserInfo me = getActionContext().getModel().getMe();

        LOGGER.info("lviUserSkills changed detected, settings " + userSkills.size() + " for user skills for user " + me.getId());
        getActionContext().getModel().setUserSkills(userSkills);
      }
    });

    lviUserSkills.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        int selected = lviUserSkills.getSelectionModel().getSelectedIndex();
        if (event.getCode().equals(KeyCode.UP) && selected == 0)
          txtSkills.requestFocus();

        if (event.getCode().equals(KeyCode.DELETE)) {
          userSkills.remove(selected);
          if (userSkills.isEmpty()) {
            event.consume();
            txtSkills.requestFocus();
            txtSkills.clear();
          }
        }

      }
    });

    btnSave.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent event) {
        try {
          UserInfo meInfo = getActionContext().getModel().getMe();
          getActionContext().getApi().getUserApi().setUserSkills(userSkills, meInfo.getId());
        } catch (ApiException e) {
          LOGGER.error(e.getLocalizedMessage());
        }

      }
    });

  }


  @Override
  public void setActionContext(ActionContext actionContext) {
    super.setActionContext(actionContext);

    List<SkillInfo> allSkills = getActionContext().getModel().getAllSkills();
    userSkills.setAll(getActionContext().getModel().getUserSkills());

    TextFields
        .bindAutoCompletion(txtSkills, new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<SkillInfo>>() {
          @Override public Collection<SkillInfo> call(AutoCompletionBinding.ISuggestionRequest param) {
            Collection<SkillInfo> skillInfos = new ArrayList<SkillInfo>();
            for (SkillInfo next : allSkills) {
              if (next.getName().contains(param.getUserText()))
                skillInfos.add(next);
            }
            return skillInfos;
          }
        }, new StringConverter<SkillInfo>() {
          @Override public String toString(SkillInfo object) {
            return object.getName();
          }

          @Override public SkillInfo fromString(String string) {
            for (SkillInfo next : allSkills) {
              if (next.getName().equals(string))
                return next;
            }
            return null;
          }
        });

  }
}
