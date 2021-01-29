package org.spica.fx.controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.HTMLEditor;
import javax.mail.MessagingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.spica.commons.services.mail.MailService;
import org.spica.commons.xmpp.XMPPAdapter;
import org.spica.fx.Consts;
import org.spica.fx.clipboard.ClipboardItem;
import org.spica.fx.renderer.MessageInfoCellFactory;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.UserInfo;

@Slf4j @Data public class MessageDetailController extends AbstractController {

  @FXML private TextField txtTopic;
  @FXML private Button btnType;
  @FXML private GridPane panMailEditor;
  @FXML private GridPane panChatEditor;
  @FXML private TextField txtMailTo;
  @FXML private TextField txtMailCC;
  @FXML private TextField txtMailBCC;
  @FXML private HTMLEditor hedMailContent;
  @FXML private TextField txtMessageTo;
  @FXML private TextArea txaMessageContent;
  @FXML private Label lblFiles;
  @FXML private Button btnSend;
  @FXML private Button btnAttachClipboard;
  @FXML private ListView<MessageInfo> lviDialog;

  private MessageDetailControllerModel viewModel = new MessageDetailControllerModel();

  @FXML public void initialize() {
    Bindings.bindBidirectional(panMailEditor.visibleProperty(), viewModel.getMailEditorVisibleProperty());
    Bindings.bindBidirectional(panChatEditor.visibleProperty(), viewModel.getChatEditorVisibleProperty());
    Bindings.bindBidirectional(txtTopic.textProperty(), viewModel.getTopicProperty());
    Bindings.bindBidirectional(btnType.textProperty(), viewModel.getTypeProperty());
    Bindings.bindBidirectional(txtMailTo.textProperty(), viewModel.getMailToProperty());
    Bindings.bindBidirectional(txtMailCC.textProperty(), viewModel.getMailCCProperty());
    Bindings.bindBidirectional(txtMailBCC.textProperty(), viewModel.getMailBCCProperty());
    Tooltip tooltip = new Tooltip();
    tooltip.textProperty().bind(viewModel.getIdProperty());
    btnType.setTooltip(tooltip);
    Bindings.bindBidirectional(lblFiles.textProperty(), viewModel.getFilesContentProperty());

    Bindings.bindBidirectional(txtMessageTo.textProperty(), viewModel.getChatToProperty());
    Bindings.bindBidirectional(txaMessageContent.textProperty(), viewModel.getChatContentProperty());
    lviDialog.setCellFactory(
        cellfactory -> new MessageInfoCellFactory(getActionContext().getServices().getFilestoreService(),
            getActionContext().getModel()));

    txaMessageContent.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        if (new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN).match(event)) {
          sendMessage();
        }
      }
    });

    btnSend.setGraphic(Consts.createIcon("fa-paper-plane", Consts.ICON_SIZE_TOOLBAR));
    btnSend.setOnAction(event -> sendMessage());

    btnAttachClipboard.setGraphic(Consts.createIcon("fa-clipboard", Consts.ICON_SIZE_TOOLBAR));
    btnAttachClipboard.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent event) {
        attachClipboard();
      }
    });
  }

  //TODO Workaround because HtmlEditor does not support bindings
  private void setMailEditorContent (final String content) {
    viewModel.getMailContentProperty().set(content);
    if (hedMailContent != null)
      hedMailContent.setHtmlText(content);
  }

  //TODO Workaround because HtmlEditor does not support bindings
  private String getMailEditorContent () {
    if (hedMailContent != null)
      getViewModel().getMailContentProperty().set(hedMailContent.getHtmlText());
    return getViewModel().getMailContentProperty().get();
 }

  private void adaptFileLabel() {
    List<File> files = viewModel.getFiles();
    if (files.isEmpty()) {
      viewModel.getFilesContentProperty().set("");
    } else {

      StringBuilder builder = new StringBuilder(files.size() + " files attached: ");
      for (File next : files) {
        builder = builder.append(next.getAbsolutePath());
        builder = builder.append(" ");
      }

      viewModel.getFilesContentProperty().set(builder.toString());
    }

  }

  private void attachClipboard() {

    List<File> files = viewModel.getFiles();

    boolean filesAdded = false;

    String completeClipboard = "";

    for (ClipboardItem nextItem : getApplicationContext().getClipboard().getItems()) {
      if (nextItem.getFiles() != null) {
        filesAdded = true;
        files.addAll(nextItem.getFiles());
      } else if (nextItem.getString() != null) {
        completeClipboard = completeClipboard + "\n\n" + nextItem.getString();
      } else {
        completeClipboard = completeClipboard + "\n\n" + nextItem.getString();

      }
    }

    viewModel.getChatContentProperty().set(completeClipboard);
    getApplicationContext().getClipboard().clear();

    if (filesAdded)
      adaptFileLabel();
  }

  public void sendMessage() {
    MessagecontainerInfo currentMessageContainer = viewModel.getCurrentMessageContainer();
    MessageInfo currentMessage = viewModel.getCurrentMessage();

    MessageType messageType = currentMessage.getType();

    currentMessageContainer.setTopic(viewModel.getTopicProperty().get());

    if (viewModel.getMailToProperty().get().contains(","))
      getApplicationContext().getMessages().text("Multiple Users not yet supported").showError();

    currentMessage.setCreatorId(getModel().getMe().getId());
    String message = messageType != null && messageType.equals(MessageType.MAIL) ?
        getMailEditorContent() : viewModel.getChatContentProperty().get();
    log.info("Set message to " + message);
    currentMessage.setMessage(message);

    for (File nextFile : viewModel.getFiles()) {
      log.info("Add file " + nextFile.getAbsolutePath() + " to message");
      currentMessage.addDocumentsItem(nextFile.getAbsolutePath());
    }

    boolean firstMail = currentMessageContainer.getMessage().isEmpty();

    //set all former message to read
    for (MessageInfo next : currentMessageContainer.getMessage()) {
      if (next.getReadtime() == null) {
        log.info("Set message " + next.getId() + " to read");
        next.setReadtime(LocalDateTime.now());
      }
    }

    if (messageType.equals(MessageType.MAIL)) {
      log.info("Send mail " + currentMessage);

      List<String> mailAdressesTo = getModel().getMailAdresses(viewModel.getMailToProperty().get());
      log.info("To: " + mailAdressesTo);

      List<String> mailAdressesCc = getModel().getMailAdresses(viewModel.getMailCCProperty().get());
      log.info("Cc: " + mailAdressesCc);

      List<String> mailAdressesBcc = getModel().getMailAdresses(viewModel.getMailBCCProperty().get());
      log.info("Bcc: " + mailAdressesBcc);

      int numberRecipients = mailAdressesTo.size() + mailAdressesCc.size() + mailAdressesBcc.size();

      try {
        String subject = (firstMail ? currentMessageContainer.getTopic() : "Re: " + currentMessageContainer.getTopic());

        currentMessage.setSendtime(LocalDateTime.now());
        currentMessage.setRecieversTo(getEntries(viewModel.getMailToProperty().get()));
        currentMessage.setRecieversCC(getEntries(viewModel.getMailCCProperty().get()));
        currentMessage.setRecieversBCC(getEntries(viewModel.getMailBCCProperty().get()));

        MailService mailService = getActionContext().getServices().getMailService();
        mailService.sendMail(subject, currentMessage.getMessage(), mailAdressesTo, mailAdressesCc, mailAdressesBcc,
            viewModel.getFiles());

        getApplicationContext().getMessages().text("Mail sent to " + numberRecipients + " recipients").showInformation();
      } catch (MessagingException e) {
        log.error(e.getLocalizedMessage(), e);
        getApplicationContext().getMessages().text("Error on sending mail: " + e.getLocalizedMessage()).showError();
      }

    } else if (messageType.equals(MessageType.CHAT)) {
      log.info("Send chat " + currentMessage);
      XMPPAdapter xmppAdapter = getActionContext().getServices().getXmppAdapter();
      try {
        currentMessage.setSendtime(LocalDateTime.now());
        List<String> entries = getEntries(viewModel.getChatToProperty().get());
        if (entries.size() > 1)
          throw new IllegalStateException("Chat does not support multiple recipients at the moment");

        currentMessage.setRecieversTo(entries);

        UserInfo recipient = getModel().getUsersOrMe(currentMessageContainer);
        xmppAdapter
            .sendMessage(getActionContext().getProperties(), recipient.getUsername(), currentMessage.getMessage(),
                viewModel.getFiles());

      } catch (InterruptedException | SmackException | IOException | XMPPException e) {
        log.error(e.getLocalizedMessage(), e);
        getApplicationContext().getMessages().text("Error on sending chat message: " + e.getLocalizedMessage()).showError();
      }
    } else {
      getApplicationContext().getMessages().text("MessageType " + messageType + " not yet supported");
    }

    //save the old message
    if (currentMessageContainer.getId() == null) {
      log.info("Message container was not yet saved, set ID and at it to model");
      currentMessageContainer.setId(UUID.randomUUID().toString());
      getModel().getMessagecontainerInfos().add(currentMessageContainer);
    }

    if (currentMessageContainer.getMessage().contains(currentMessage))
      throw new IllegalStateException("Message must not be contained in the message container yet.");

    currentMessage.setReadtime(LocalDateTime.now());

    log.info("Add messsage " + currentMessage.getId() + " to model (container " + currentMessageContainer.getId() + ")");
    currentMessageContainer.getMessage().add(currentMessage);
    //create new message, but don't save
    MessageInfo newMessage = getModel().createNewMessage(currentMessage.getType());
    newMessage.setRecieversTo(currentMessage.getRecieversTo());
    newMessage.setRecieversCC(currentMessage.getRecieversCC());
    newMessage.setRecieversBCC(currentMessage.getRecieversBCC());
    getApplicationContext().setSelectedMessageInfo(newMessage);

    adaptFileLabel();
    refreshData();

  }

  private String getTextField(final List<String> adresses) {
    if (adresses == null)
      return "";
    else
      return adresses.toString().replace("[", "").replace("]", "");
  }

  private List<String> getEntries(final String content) {
    List<String> entries = new ArrayList<>();

    if (content == null || content.trim().isEmpty())
      return entries;

    for (String next : content.split(",")) {
      entries.add(next.trim());
    }

    return entries;
  }

  @Override public void refreshData() {

    viewModel.getMailToProperty().set("");
    viewModel.getChatToProperty().set("");
    viewModel.getMailBCCProperty().set("");
    viewModel.getMailCCProperty().set("");
    setMailEditorContent("");
    viewModel.getChatContentProperty().set("");
    viewModel.getFilesContentProperty().set("");

    viewModel.setCurrentMessage(getApplicationContext().getSelectedMessageInfo());
    viewModel.setCurrentMessageContainer(getApplicationContext().getSelectedMessageContainer());

    MessagecontainerInfo currentMessageContainer = viewModel.getCurrentMessageContainer();
    MessageInfo currentMessage = viewModel.getCurrentMessage();

    viewModel.getIdProperty().set(currentMessageContainer.getId() + " -> " + currentMessage.getId());

    log.info("Refresh data for messagecontainer '" + currentMessageContainer.getTopic() + "'");

    getMainController().refreshData();

    viewModel.getTypeProperty().setValue(currentMessage.getType().toString());

    viewModel.getTopicProperty().set(currentMessageContainer.getTopic());

    if (currentMessage.getType().equals(MessageType.MAIL)) {
      viewModel.getMailEditorVisibleProperty().set(true);
      viewModel.getChatEditorVisibleProperty().set(false);
      setMailEditorContent("");
      viewModel.getMailToProperty().set(getTextField(currentMessage.getRecieversTo()));
      viewModel.getMailCCProperty().set(getTextField(currentMessage.getRecieversCC()));
      viewModel.getMailBCCProperty().set(getTextField(currentMessage.getRecieversBCC()));
    } else {
      viewModel.getMailEditorVisibleProperty().set(false);
      viewModel.getChatEditorVisibleProperty().set(true);
      viewModel.getChatContentProperty().set("");
      if (currentMessage.getRecieversTo() != null && currentMessage.getRecieversTo().size() != 1)
        throw new IllegalStateException("Only messages with one reciever allowed for chat");

      viewModel.getChatToProperty().set(viewModel.getCurrentMessage().getRecieversTo().get(0));
    }
    viewModel.getMessageInfos().setAll(currentMessageContainer.getMessage());

    List<String> userIds = new ArrayList<>();
    List<UserInfo> otherUsers = getModel().getOtherUsers(viewModel.getCurrentMessageContainer());
    for (UserInfo next : otherUsers) {
      if (next.getUsername() != null)
        userIds.add("@" + next.getUsername());
      else
        userIds.add(next.getEmail());
    }

  }
}
