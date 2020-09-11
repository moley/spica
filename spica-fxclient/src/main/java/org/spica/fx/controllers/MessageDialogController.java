package org.spica.fx.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
import javafx.scene.layout.StackPane;
import javafx.scene.web.HTMLEditor;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.spica.commons.services.mail.MailService;
import org.spica.commons.xmpp.XMPPAdapter;
import org.spica.fx.Consts;
import org.spica.fx.renderer.MessageInfoCellFactory;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.UserInfo;

@Slf4j public class MessageDialogController extends AbstractController {

  @FXML private HTMLEditor hedNewMail;
  @FXML private StackPane panEditor;
  @FXML private Button btnSend;
  @FXML private ListView<MessageInfo> lviDialog;
  @FXML private TextArea txaNewMessage;

  @FXML public void initialize() {
    lviDialog.setCellFactory(cellfactory -> new MessageInfoCellFactory(getActionContext().getModel()));
    txaNewMessage.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        if (new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN).match(event)) {

          sendMessage();
        }
      }
    });

    btnSend.setGraphic(Consts.createIcon("fa-paper-plane", Consts.ICON_SIZE_TOOLBAR));
    btnSend.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent event) {
        sendMessage();
      }
    });
  }

  private void sendMessage() {

    MessageType messageType = getCurrentMessageType();
    MessagecontainerInfo selectedMessageContainer = getModel().getSelectedMessageContainer();

    MessageInfo messageInfo = new MessageInfo();

    if (selectedMessageContainer.getMessage().get(0)
        .getSendtime() == null) { //Created messagecontainer but did not send any mail
      messageInfo = selectedMessageContainer.getMessage().get(0);
    } else {
      messageInfo = new MessageInfo();
      messageInfo.setType(messageType);
      messageInfo.setCreationtime(LocalDateTime.now());
      selectedMessageContainer.addMessageItem(messageInfo);
    }

    messageInfo.setCreatorId(getModel().getMe().getId());
    messageInfo.setMessage((messageType != null && messageType.equals(MessageType.MAIL)) ?
        hedNewMail.getHtmlText() :
        txaNewMessage.getText());

    boolean firstMail = selectedMessageContainer.getMessage().get(0).equals(messageInfo);

    
    getActionContext().saveModel("Added new message to messagecontainer " + selectedMessageContainer.getTopic());

    if (messageType.equals(MessageType.MAIL)) {

      HashSet<String> recipients = new HashSet<>();
      recipients.add(messageInfo.getRecipientMailadresse()); //TODO send mail to all

      try {
        String subject = (firstMail ?
            selectedMessageContainer.getTopic() :
            "Re: " + selectedMessageContainer.getTopic());
        MailService mailService = getActionContext().getServices().getMailService();
        mailService.sendMail(subject, messageInfo.getMessage(), new ArrayList<>(recipients));
        messageInfo.setSendtime(LocalDateTime.now());
        Notifications.create().text("Mail sent to " + recipients).showInformation();
      } catch (MessagingException e) {
        log.error(e.getLocalizedMessage(), e);
        Notifications.create().text("Error on sending mail: " + e.getLocalizedMessage()).showError();
      }

    } else if (messageType.equals(MessageType.CHAT)) {
      XMPPAdapter xmppAdapter = getActionContext().getServices().getXmppAdapter();
      try {

        UserInfo recipient = getModel().getUsersOrMe(getModel().getSelectedMessageContainer());
        xmppAdapter.sendMessage(getActionContext().getProperties(), recipient.getUsername(), messageInfo.getMessage());
        messageInfo.setSendtime(LocalDateTime.now());
      } catch (InterruptedException | SmackException | IOException | XMPPException e) {
        log.error(e.getLocalizedMessage(), e);
        Notifications.create().text("Error on sending chat message: " + e.getLocalizedMessage()).showError();
      }
    } else {
      Notifications.create().text("MessageType " + messageType + " not yet supported");
    }

    txaNewMessage.clear();

    refreshData();

  }

  private MessageType getCurrentMessageType() {
    if (getModel().getSelectedMessageContainer() == null)
      throw new IllegalStateException("No messagecontainer selected");

    MessagecontainerInfo selectedMessageContainer = getModel().getSelectedMessageContainer();
    if (selectedMessageContainer.getMessage().isEmpty()) {
      return null;
    } else
      return selectedMessageContainer.getMessage().get(0).getType();
  }

  @Override public void refreshData() {

    MessagecontainerInfo selectedMessageContainer = getModel().getSelectedMessageContainer();
    if (selectedMessageContainer != null) {
      if (getCurrentMessageType().equals(MessageType.MAIL)) {
        hedNewMail.setHtmlText("");
        hedNewMail.toFront();
      } else {
        txaNewMessage.setText("");
        txaNewMessage.toFront();
      }
      lviDialog.setItems(FXCollections.observableArrayList(selectedMessageContainer.getMessage()));
    } else
      lviDialog.setItems(FXCollections.emptyObservableList());
  }
}
