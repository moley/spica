package org.spica.fx.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.stage.PopupWindow;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.spica.commons.mail.MailAdapter;
import org.spica.fx.Mask;
import org.spica.fx.MaskLoader;
import org.spica.fx.renderer.MessageContainerInfoCellFactory;
import org.spica.javaclient.exceptions.NotFoundException;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.UserInfo;

@Slf4j
public class MessagesController extends AbstractController {

  @FXML private ListView<MessagecontainerInfo> lviMessages;
  @FXML private TextField txtSearch;

  @FXML public void initialize() {
    lviMessages.setCellFactory(cellfactory -> new MessageContainerInfoCellFactory(getModel()));
    lviMessages.setOnDragOver(event -> event.acceptTransferModes(TransferMode.ANY));

    lviMessages.setOnKeyPressed(event -> {
      if (event.getCode().equals(KeyCode.SLASH)) {
        removeMessage(lviMessages.getSelectionModel().getSelectedItem());
      }
    });


    lviMessages.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        MessagecontainerInfo selectedItem = lviMessages.getSelectionModel().getSelectedItem();
        if (getModel().setSelectedMessageContainer(selectedItem)) {
          getActionContext().saveModel("set " + selectedItem.getTopic() + " as read");
        }
        stepToPane(Pages.MESSAGEDIALOG);
      }

    });

    txtSearch.setOnKeyReleased(event -> {

      if (event.getCode().equals(KeyCode.ENTER)) {

        String [] tokens = txtSearch.getText().split(" ");
        String type = tokens[0];
        String recipient = tokens[1];
        if (recipient.startsWith("#"))
          recipient = recipient.substring(1);

        String subject = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));

        String recipientMail = recipient.contains("@") ? recipient: null;
        UserInfo recipientUser;

        if (recipientMail == null) { //if no mail adress we expect an internal user
          try {
            recipientUser = getModel().findUserByUsername(recipient);
            Notifications.create().text("Found user " + recipientUser.getDisplayname()).showInformation();
          } catch (NotFoundException e) {
            Notifications.create().text("User " + recipient + " not found").showError();
            return;
          }
          recipientMail = recipientUser.getEmail();
        } else {
          recipientUser = getModel().findUserByMail(recipientMail);
        }

        MessagecontainerInfo newMessageContainer = new MessagecontainerInfo();
        newMessageContainer.setTopic(subject);

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setCreationtime(LocalDateTime.now());
        messageInfo.setCreatorId(getModel().getMe().getId());
        messageInfo.setType(MessageType.fromValue(type.toLowerCase()));
        messageInfo.setRecipientId(recipientUser != null ? recipientUser.getId(): null);
        messageInfo.setRecipientMailadresse(recipientMail);
        messageInfo.setId(UUID.randomUUID().toString());
        newMessageContainer.addMessageItem(messageInfo);
        getModel().getMessagecontainerInfos().add(newMessageContainer);
        saveModel("Created new messagecontainer with type " + type + " and recipient " + recipient);
        getModel().setSelectedMessageContainer(newMessageContainer);

        Notifications.create().text("Creating a new message of type " + messageInfo.getType() + " for recipient " + messageInfo.getRecipientMailadresse() + "-" + messageInfo.getRecipientId()).show();
        stepToPane(Pages.MESSAGEDIALOG);


      }
      if (event.getText().equalsIgnoreCase("#")) {

        MaskLoader<SearchboxController> maskLoader = new MaskLoader<>();
        Mask<SearchboxController> mask = maskLoader.load("searchbox");

        SearchboxController controller = mask.getController();
        controller.refreshData();

        PopOver popOver = new PopOver(mask.getParent());
        mask.getParent().visibleProperty().addListener((observable, oldValue, newValue) -> {
          if (oldValue && !newValue) {
            txtSearch.setText(txtSearch.getText() + controller.getSelectedUser().getUsername());
            popOver.hide();
          }
        });
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
        popOver.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
        popOver.show(txtSearch);
        popOver.requestFocus();
        event.consume();
      }
    });
  }

  private void removeMessage (final MessagecontainerInfo messagecontainerInfo) {

    MailAdapter mailAdapter = getActionContext().getServices().getMailImporter().getMailAdapter();
    getModel().getMessagecontainerInfos().remove(messagecontainerInfo);
    for (MessageInfo nextMessage: messagecontainerInfo.getMessage()) {
      if (nextMessage.getType().equals(MessageType.MAIL)) {
        mailAdapter.deleteMail(nextMessage.getId());
      }
    }
    saveModel("Removed message " + messagecontainerInfo.getTopic() + " with " + messagecontainerInfo.getMessage().size() + " messages");
    refreshData();
  }



  @Override public void refreshData() {

    if (getModel() != null)
      lviMessages.setItems(FXCollections.observableArrayList(getModel().getMessagecontainerInfos()));

    getMainController().refreshData();
  }
}
