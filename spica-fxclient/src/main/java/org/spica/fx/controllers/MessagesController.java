package org.spica.fx.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.PopupWindow;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.fx.Mask;
import org.spica.fx.MaskLoader;
import org.spica.fx.renderer.MessageContainerInfoCellFactory;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.UserInfo;

@Slf4j
public class MessagesController extends AbstractController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessagesController.class);


  @FXML private ListView<MessagecontainerInfo> lviMessages;
  @FXML private TextField txtSearch;


  private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();

  @FXML public void initialize() {
    lviMessages.setCellFactory(cellfactory -> new MessageContainerInfoCellFactory());
    lviMessages.setOnDragOver(new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
      }
    });

    lviMessages.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
          if (event.getCode().equals(KeyCode.SLASH)) {
            removeMessage(lviMessages.getSelectionModel().getSelectedItem());
          }
        }

    });

    lviMessages.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (event.getClickCount() == 2) {
          MessagecontainerInfo selectedItem = lviMessages.getSelectionModel().getSelectedItem();
          if (getModel().setSelectedMessageContainer(selectedItem)) {
            getActionContext().saveModel("set " + selectedItem.getTopic() + " as read");
          }
          stepToPane(Pages.MESSAGEDIALOG);
        }

      }
    });

    txtSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {

          String [] tokens = txtSearch.getText().split(" ");
          String type = tokens[0];
          String recipient = tokens[1];
          if (recipient.startsWith("#"))
            recipient = recipient.substring(1);

          String subject = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));

          String recipientMail = recipient.contains("@") ? recipient: null;
          UserInfo recipientUser = (recipientMail == null ? getModel().findUserByUsername(recipient): null);

          if (recipientMail == null && recipientUser == null)
            Notifications.create().text("User " + recipient + " not found").showError();

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

          MaskLoader<SearchboxController> maskLoader = new MaskLoader<SearchboxController>();
          Mask<SearchboxController> mask = maskLoader.load("searchbox");

          SearchboxController controller = mask.getController();
          controller.setActionContext(getActionContext());
          controller.setApplicationContext(getApplicationContext());
          controller.refreshData();

          PopOver popOver = new PopOver(mask.getParent());
          mask.getParent().visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                Boolean newValue) {
              if (oldValue == true && newValue == false) {
                txtSearch.setText(txtSearch.getText() + controller.getSelectedUser().getUsername());
                popOver.hide();
              }
            }
          });
          popOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
          popOver.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
          popOver.show(txtSearch);
          popOver.requestFocus();
          event.consume();
        }
      }
    });
  }

  private void removeMessage (final MessagecontainerInfo messagecontainerInfo) {
    getModel().getMessagecontainerInfos().remove(messagecontainerInfo);
    saveModel("Removed message " + messagecontainerInfo.getTopic() + " with " + messagecontainerInfo.getMessage().size() + " messages");
    refreshData();
  }

  @Override public void refreshData() {

    lviMessages.setItems(FXCollections.observableArrayList(getModel().getMessagecontainerInfos()));


  }
}
