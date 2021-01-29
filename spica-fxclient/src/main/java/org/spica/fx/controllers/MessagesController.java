package org.spica.fx.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.stage.PopupWindow;
import javax.mail.MessagingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.PopOver;
import org.spica.commons.mail.MailAdapter;
import org.spica.fx.Mask;
import org.spica.fx.MaskLoader;
import org.spica.fx.renderer.MessageContainerInfoCellFactory;
import org.spica.javaclient.exceptions.NotFoundException;
import org.spica.javaclient.mail.MailImporter;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.UserInfo;

@Slf4j
@Data
public class MessagesController extends AbstractController {

  public ButtonBar btbActions;
  @FXML private ListView<MessagecontainerInfo> lviMessages;
  @FXML private TextField txtSearch;

  private MessagesControllerModel viewModel = new MessagesControllerModel();

  @FXML public void initialize() {

    Button btnSetRead = new Button("Read");
    btnSetRead.setOnAction(event -> setReadtime(lviMessages.getSelectionModel().getSelectedItems()));

    Button btnClear = new Button ("Clear");
    btnClear.setOnAction(event -> clear ());

    Button btnDelete = new Button ("Remove");
    btnDelete.setOnAction(event -> removeMessage(lviMessages.getSelectionModel().getSelectedItems()));

    btbActions.getButtons().addAll(btnSetRead, btnClear, btnDelete);

    lviMessages.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    lviMessages.setCellFactory(cellfactory -> new MessageContainerInfoCellFactory(getModel()));
    lviMessages.setOnDragOver(event -> event.acceptTransferModes(TransferMode.ANY));
    lviMessages.setItems(viewModel.getMessageContainers());

    lviMessages.setOnKeyPressed(event -> {
      if (event.getCode().equals(KeyCode.SLASH)) {
        removeMessage(lviMessages.getSelectionModel().getSelectedItems());
      }
    });


    lviMessages.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        editExistingMessage (lviMessages.getSelectionModel().getSelectedItem());
        stepToPane(Pages.MESSAGEDIALOG);
      }

    });
    Bindings.bindBidirectional(txtSearch.textProperty(), viewModel.getSearchProperty());

    txtSearch.setOnKeyReleased(event -> {

      if (event.getCode().equals(KeyCode.ENTER)) {
        editNewMessage();
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

  public void editExistingMessage(MessagecontainerInfo selectedItem) {
    MessagecontainerInfo selectedMessageContainer = selectedItem;
    if (getApplicationContext().setSelectedMessageContainer(selectedMessageContainer)) {
      getActionContext().saveModel("set " + selectedMessageContainer.getTopic() + " as read");
    }

    MessageInfo lastMessageInContainer = getModel().getLastMessageInMessageContainer(selectedMessageContainer);
    //Answer last message
    MessageInfo newMessageInfo = getModel().createNewMessage(lastMessageInContainer.getType());
    newMessageInfo.addRecieversToItem(lastMessageInContainer.getCreatorMailadresse());
    if (lastMessageInContainer.getRecieversCC() != null) {
      for (String next : lastMessageInContainer.getRecieversCC())
        newMessageInfo.addRecieversCCItem(next);
    }
    if (lastMessageInContainer.getRecieversBCC() != null) {
      for (String next : lastMessageInContainer.getRecieversBCC())
        newMessageInfo.addRecieversBCCItem(next);
    }

    getApplicationContext().setSelectedMessageContainer(selectedMessageContainer);
    getApplicationContext().setSelectedMessageInfo(newMessageInfo);

  }

  public void editNewMessage() {
    String [] tokens = viewModel.getSearchProperty().get().split(" ");
    String type = tokens[0];
    String recipient = tokens[1];
    if (recipient.startsWith("#"))
      recipient = recipient.substring(1);

    String subject = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));

    String recipientMail = recipient.contains("@") ? recipient: null;
    UserInfo recipientUser = null;

    if (recipientMail == null) { //if no mail adress we expect an internal user
      try {
        recipientUser = getModel().findUserByUsername(recipient);
        getApplicationContext().getMessages().text("Found user " + recipientUser.getDisplayname()).showInformation();
      } catch (NotFoundException e) {
        getApplicationContext().getMessages().text("User " + recipient + " not found").showError();
      }
      recipientMail = recipientUser.getEmail();
    } else {
      recipientUser = getModel().findUserByMail(recipientMail);
    }

    MessagecontainerInfo newMessageContainer = getModel().createNewMessageContainer();
    newMessageContainer.setTopic(subject);

    MessageInfo messageInfo = getModel().createNewMessage(MessageType.fromValue(type.toLowerCase()));
    messageInfo.addRecieversToItem(recipientUser != null ? "@" + recipientUser.getId(): recipientMail);
    getApplicationContext().setSelectedMessageContainer(newMessageContainer);
    getApplicationContext().setSelectedMessageInfo(messageInfo);
    getApplicationContext().getMessages().text("Creating a new message of type " + messageInfo.getType() + " for recipient " + messageInfo.getRecieversTo().get(0)).showInformation();
    viewModel.getSearchProperty().set("");
  }

  private void clear() {
    getModel().getMessagecontainerInfos().clear();
    saveModel("Cleared local messages");
    MailImporter mailImporter = new MailImporter();
    try {
      mailImporter.importMails(getModel());
    } catch (MessagingException e) {
      log.error(e.getLocalizedMessage(), e);
    } catch (IOException e) {
      log.error(e.getLocalizedMessage(), e);
    }
    refreshData();
  }

  private void removeMessage (final List<MessagecontainerInfo> messagecontainerInfos) {
    int numberOfMessages = 0;
    MailAdapter mailAdapter = getActionContext().getServices().getMailImporter().getMailAdapter();
    getModel().getMessagecontainerInfos().removeAll(messagecontainerInfos);
    for (MessagecontainerInfo messagecontainerInfo: messagecontainerInfos) {
      for (MessageInfo nextMessage : messagecontainerInfo.getMessage()) {
        if (nextMessage.getType().equals(MessageType.MAIL)) {
          mailAdapter.deleteMail(nextMessage.getId());
          numberOfMessages++;
        }
      }
    }
    saveModel("Removed " + messagecontainerInfos.size() + " messages with " + numberOfMessages + " messages");
    refreshData();
  }

  private void setReadtime (final List<MessagecontainerInfo> messagecontainerInfos) {
    int numberOfMessages = 0;
    for (MessagecontainerInfo messagecontainerInfo: messagecontainerInfos) {
      for (MessageInfo nextMessage : messagecontainerInfo.getMessage()) {
        if (nextMessage.getReadtime() == null) {
          nextMessage.readtime(LocalDateTime.now());
          numberOfMessages++;
        }
      }
    }
    saveModel("Set readtime for " + numberOfMessages);
    refreshData();
  }



  @Override public void refreshData() {

    if (getModel() != null)
      viewModel.getMessageContainers().setAll(FXCollections.observableArrayList(getModel().getMessagecontainerInfos()));

    getMainController().refreshData();
  }
}
