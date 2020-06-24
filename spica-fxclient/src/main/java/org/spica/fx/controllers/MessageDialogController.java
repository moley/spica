package org.spica.fx.controllers;

import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.fx.Consts;
import org.spica.fx.renderer.MessageInfoCellFactory;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;

public class MessageDialogController extends AbstractController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageDialogController.class);
  @FXML private Button btnSend;
  @FXML private ListView<MessageInfo> lviDialog;
  @FXML private TextArea txaNewMessage;

  @FXML public void initialize() {
    lviDialog.setCellFactory(cellfactory -> new MessageInfoCellFactory(getActionContext().getModel()));
    txaNewMessage.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        if (new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN).match(event)) {

          addMessage();
        }
      }
    });

    btnSend.setGraphic(Consts.createIcon("fa-paper-plane", Consts.ICON_SIZE_TOOLBAR));
    btnSend.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent event) {
        addMessage();
      }
    });
  }

  private void addMessage () {
    //TODO real implementation
    MessagecontainerInfo selectedMessageContainer = getActionContext().getModel().getSelectedMessageContainer();
    MessageInfo messageInfo = new MessageInfo();
    messageInfo.setCreationtime(LocalDateTime.now());
    messageInfo.setCreator(getActionContext().getModel().getMe().getId());
    messageInfo.setMessage(txaNewMessage.getText());
    messageInfo.setType(MessageType.CHAT); //TODO use messagetype of last message by default
    selectedMessageContainer.addMessageItem(messageInfo);

    txaNewMessage.clear();

    refreshData();

  }

  @Override public void refreshData() {
    MessagecontainerInfo selectedMessageContainer = getActionContext().getModel().getSelectedMessageContainer();
    if (selectedMessageContainer != null)
      lviDialog.setItems(FXCollections.observableArrayList(selectedMessageContainer.getMessage()));
    else
      lviDialog.setItems(FXCollections.emptyObservableList());
  }
}
