package org.spica.fx.controllers;

import com.jfoenix.controls.JFXBadge;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Timer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.cli.actions.StandaloneActionContext;
import org.spica.commons.xmpp.XMPPAdapter;
import org.spica.fx.ApplicationContext;
import org.spica.fx.AutoImportClipboardThread;
import org.spica.fx.AutoImportMailsTask;
import org.spica.fx.Consts;
import org.spica.fx.MainMenuEntry;
import org.spica.fx.Mask;
import org.spica.commons.UserPresence;
import org.spica.fx.MaskLoader;
import org.spica.fx.Reload;
import org.spica.fx.clipboard.ClipboardItem;
import org.spica.javaclient.Configuration;
import org.spica.javaclient.exceptions.NotFoundException;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;

@Slf4j
public class MainController extends AbstractController  {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
  @FXML private  TextField txtCurrentAction;
  @FXML private Button btnState;
  @FXML private JFXBadge badClipboard;
  @FXML private Button btnClipboard;
  @FXML private Button btnCloseSearch;
  @FXML private CustomTextField txtFieldSearch;
  @FXML private Button btnSearchUp;
  @FXML private Button btnSearchDown;
  @FXML private Label lblMatches;
  @FXML private CustomTextField txtSearch;
  @FXML private BorderPane paRootPane;
  @FXML private ButtonBar btnMainActions;

  private HashMap<Pages, MainMenuEntry> menuEntries = new HashMap<Pages, MainMenuEntry>();

  private ListView<ClipboardItem> lviClipboardItems = new ListView<ClipboardItem>();

  private AutoImportClipboardThread autoImportThread;

  private AutoImportMailsTask autoImportMailsTask;

  private StandaloneActionContext standaloneActionContext = new StandaloneActionContext();


  @FXML
  public void initialize () {
    setPaRootPane(paRootPane);
    btnCloseSearch.setGraphic(Consts.createIcon("fa-close", 15));




    btnState.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent event) {

        UserPresence currentPresence = getApplicationContext().getPresence();
        if (currentPresence.equals(UserPresence.ONLINE)) {
          getApplicationContext().setPresence(UserPresence.OFFLINE);
        }
        else
          getApplicationContext().setPresence(UserPresence.ONLINE);

        XMPPAdapter xmppAdapter = getActionContext().getServices().getXmppAdapter();
        xmppAdapter.setPresence(getApplicationContext().getPresence(), txtCurrentAction.getText());

        getApplicationContext().presencePropertyProperty().setValue(getApplicationContext().getPresence().toString());
      }
    });

    txtCurrentAction.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (oldValue == true && newValue == false) {
        XMPPAdapter xmppAdapter = getActionContext().getServices().getXmppAdapter();
        xmppAdapter.setPresence(getApplicationContext().getPresence(), txtCurrentAction.getText());
      }
    });

    Label lbl = new Label();
    lbl.setGraphic(Consts.createIcon("fa-search", 15));
    txtFieldSearch.setLeft(lbl);


    PopOver popOver = new PopOver(lviClipboardItems);
    btnClipboard.setOnMouseEntered(mouseEvent -> {
      //Show PopOver when mouse enters label
      popOver.show(btnClipboard);
    });

    btnClipboard.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (event.getClickCount() == 2) {
          getApplicationContext().getClipboard().clear();

        }
      }
    });

    btnSearchUp.setGraphic(Consts.createIcon("fa-chevron-up", 15));
    btnSearchDown.setGraphic(Consts.createIcon("fa-chevron-down", 15));

    btnClipboard.setGraphic(Consts.createIcon("fa-clipboard", 15));

    setActionContext(standaloneActionContext);
    setApplicationContext(new ApplicationContext());

    for (Pages next : Pages.values()) {
      registerPane(next);

      if (next.isMainAction()) {

        JFXBadge jfxBadge = new JFXBadge();
        Button menuItem = new Button(next.getDisplayname(), Consts.createIcon(next.getIcon(), Consts.ICON_SIZE_TOOLBAR));
        menuItem.setOnAction(event -> stepToPane(next));
        jfxBadge.setControl(menuItem);
        btnMainActions.getButtons().add(jfxBadge);

        MainMenuEntry mainMenuEntry = new MainMenuEntry();
        mainMenuEntry.setButton(menuItem);
        mainMenuEntry.setJfxBadge(jfxBadge);

        menuEntries.put(next, mainMenuEntry);
      }
    }
  }

  public void registerPane (final Pages pages) {
    MaskLoader maskLoader = new MaskLoader();
    try {
      Mask mask = maskLoader.load(pages.getFilename());
      AbstractController controller = mask.getController();
      controller.setPaRootPane(getPaRootPane());
      controller.setActionContext(getActionContext());
      controller.setApplicationContext(getApplicationContext());
      controller.setMainController(this);
      getRegisteredMasks().put(pages, mask);
    } catch (Exception e) {
      LOGGER.error("Error loading page " + pages.getFilename(), e);
    }

  }


  public void init() {

    autoImportThread = new AutoImportClipboardThread(getApplicationContext());
    autoImportThread.start();

    Timer autoImportMailsTimer = new Timer();

    autoImportMailsTask = new AutoImportMailsTask(getActionContext(), new Reload() {
      @Override public void reload() {

        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            Mask<MessagesController> mask = getMask(Pages.MESSAGES);
            MessagesController messagesController = mask.getController();
            showMessageNotifications();
            messagesController.refreshData();
          }
        });

      }
    });
    autoImportMailsTimer.scheduleAtFixedRate(autoImportMailsTask, 0, 60000);


    log.info("Register clipboard");
    getApplicationContext().getClipboard().getItems().addListener(new ListChangeListener<ClipboardItem>() {
      @Override public void onChanged(Change<? extends ClipboardItem> c) {
        log.info("Clipboard changed");
        badClipboard.setText(String.valueOf(getApplicationContext().getClipboard().getItems().size()));
        badClipboard.setEnabled(! getApplicationContext().getClipboard().getItems().isEmpty());
        badClipboard.refreshBadge();
        lviClipboardItems.setItems(getApplicationContext().getClipboard().getItems());

      }
    });


    try {
      String serverUrl = standaloneActionContext.getProperties().getValueOrDefault("spica.cli.serverurl", "http://localhost:8765/api");
      Configuration.getDefaultApiClient().setBasePath(serverUrl);

      /**String username = standaloneActionContext.getProperties().getValueNotNull("spica.cli.username");
      String password = standaloneActionContext.getProperties().getValueNotNull("spica.cli.password");
      HttpBasicAuth httpBasicAuth = (HttpBasicAuth) Configuration.getDefaultApiClient().getAuthentication("basicAuth");
      httpBasicAuth.setUsername(username);
      httpBasicAuth.setPassword(password);**/

      standaloneActionContext.refreshServer();
    } catch (Exception e) {
      String message = "Error while synchronization with server: " + e.getLocalizedMessage();
      log.error(message, e);
    }

    try {
      XMPPAdapter xmppAdapter = getActionContext().getServices().getXmppAdapter();
      xmppAdapter.login(getActionContext().getProperties(), new IncomingChatMessageListener() {
        @Override public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
          Model model = getModel();

          String username = from.toString().split("@")[0];
          UserInfo userInfo = null;
          try {
            userInfo = getModel().findUserByUsername(username);
          } catch (NotFoundException e) {
            Notifications.create().text("User " + username + " not found").showError();
          }
          MessagecontainerInfo openMesssageContainer = getModel().findOpenMessageContainerByUser(MessageType.CHAT, userInfo);
          
          //if no open conversation found so far create new one
          MessagecontainerInfo newMessageContainer = openMesssageContainer != null ? openMesssageContainer : new MessagecontainerInfo();
          newMessageContainer.setTopic("Chat with " + from.toString());
          MessageInfo messageInfo = new MessageInfo();
          messageInfo.setType(MessageType.CHAT);
          messageInfo.setCreatorMailadresse(from.toString());
          if (userInfo != null)
            messageInfo.setCreatorId(userInfo.getId());
          messageInfo.setMessage(message.getBody());
          messageInfo.setCreationtime(LocalDateTime.now());
          newMessageContainer.addMessageItem(messageInfo);
          if (openMesssageContainer == null)
            model.getMessagecontainerInfos().add(newMessageContainer);
          
          getModel().sortMessages();
          saveModel("Added new chatmessage to existing chat from " + from.toString());

          Platform.runLater(new Runnable() {
            @Override public void run() {
              Mask<MessagesController> mask = getMask(Pages.MESSAGES);
              MessagesController messagesController = mask.getController();
              messagesController.refreshData();


              showMessageNotifications();

              Mask<MessageDialogController> detailMask = getMask(Pages.MESSAGEDIALOG);
              MessageDialogController controller = detailMask.getController();
              controller.refreshData();

            }
          });






        }
      });
    } catch (IOException e) {
      log.error(e.getLocalizedMessage(), e);
    } catch (InterruptedException e) {
      log.error(e.getLocalizedMessage(), e);
    } catch (XMPPException e) {
      log.error(e.getLocalizedMessage(), e);
    } catch (SmackException e) {
      log.error(e.getLocalizedMessage(), e);
    }

    btnState.textProperty().bindBidirectional(getApplicationContext().presencePropertyProperty());
    btnState.textProperty().setValue(UserPresence.ONLINE.name());

    if (paRootPane.getCenter() == null)
      stepToPane(Pages.PLANNING);
  }

  public void showMessageNotifications() {
    if (getModel() == null)
      return;

    int numberOfUnreadMessages = getModel().findUnreadMessages ().size();
    if (numberOfUnreadMessages > 0) {
      log.info("Show message recieved new messages (" + numberOfUnreadMessages + ")");
      Notifications.create().owner(btnState).title("Recieved new messages").text("You have " + numberOfUnreadMessages + " unread messages ").showInformation();
    }
  }

  @Override public void refreshData() {
    int numberOfUnreadMessages = getModel().findUnreadMessages ().size();

    MainMenuEntry mainMenuEntry = menuEntries.get(Pages.MESSAGES);
    JFXBadge jfxBadge = mainMenuEntry.getJfxBadge();
    jfxBadge.setEnabled(numberOfUnreadMessages > 0);
    jfxBadge.setText(String.valueOf(numberOfUnreadMessages));
    jfxBadge.refreshBadge();

  }
}
