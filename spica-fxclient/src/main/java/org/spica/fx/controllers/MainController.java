package org.spica.fx.controllers;

import com.jfoenix.controls.JFXBadge;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;
import org.spica.cli.actions.StandaloneActionContext;
import org.spica.commons.xmpp.XMPPAdapter;
import org.spica.fx.ApplicationContext;
import org.spica.fx.AutoImportClipboardThread;
import org.spica.fx.AutoImportMailsTask;
import org.spica.fx.Consts;
import org.spica.fx.clipboard.ClipboardItem;
import org.spica.javaclient.Configuration;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;

@Slf4j
public class MainController extends AbstractController  {
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

  private ListView<ClipboardItem> lviClipboardItems = new ListView<ClipboardItem>();

  private AutoImportClipboardThread autoImportThread;

  private AutoImportMailsTask autoImportMailsTask;

  private XMPPAdapter xmppAdapter = new XMPPAdapter();

  @FXML
  public void initialize () {
    setPaRootPane(paRootPane);
    btnCloseSearch.setGraphic(Consts.createIcon("fa-close", 15));

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

    for (Pages next : Pages.values()) {
      registerPane(next);

      if (next.isMainAction()) {
        Button menuItem = new Button(next.getDisplayname(), Consts.createIcon(next.getIcon(), Consts.ICON_SIZE_TOOLBAR));
        menuItem.setOnAction(event -> stepToPane(next));
        btnMainActions.getButtons().add(menuItem);
      }
    }
  }


  @Override public void refreshData() {

    StandaloneActionContext standaloneActionContext = new StandaloneActionContext();
    setActionContext(standaloneActionContext);

    setApplicationContext(new ApplicationContext());

    autoImportThread = new AutoImportClipboardThread(getApplicationContext());
    autoImportThread.start();

    Timer autoImportMailsTimer = new Timer();
    autoImportMailsTask = new AutoImportMailsTask(getActionContext());
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
      xmppAdapter.login(getActionContext().getProperties(), new IncomingChatMessageListener() {
        @Override public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
          Model model = getModel();
          /**for (MessagecontainerInfo messageContainerInfo : model.getMessagecontainerInfos()) {
            for (MessageInfo nextMessage : messageContainerInfo.getMessage()) {
              if (nextMessage.getType().equals(MessageType.CHAT) && nextMessage.getCreatorMailadresse().equalsIgnoreCase("TODO")) {

                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setType(MessageType.CHAT);
                messageInfo.setCreatorMailadresse(from.toString());
                messageInfo.setMessage(message.getBody());
                messageInfo.setCreationtime(LocalDateTime.now());
                messageContainerInfo.getMessage().add(messageInfo);
                Collections
                    .sort(getModel().getMessagecontainerInfos(), new Comparator<MessagecontainerInfo>() {
                      @Override public int compare(MessagecontainerInfo o1, MessagecontainerInfo o2) {
                        return o2.getMessage().get(0).getCreationtime().compareTo(o1.getMessage().get(0).getCreationtime());
                      }
                    });
                saveModel("Added new chatmessage from " + from.toString());

                //TODO speichern

                return;


              }
            }

          }**/

          //if no open conversation found so far
          MessagecontainerInfo newMessageContainer = new MessagecontainerInfo();
          newMessageContainer.setTopic("Chat with " + from.toString());
          MessageInfo messageInfo = new MessageInfo();
          messageInfo.setType(MessageType.CHAT);
          messageInfo.setCreatorMailadresse(from.toString());
          messageInfo.setMessage(message.getBody());
          messageInfo.setCreationtime(LocalDateTime.now());
          newMessageContainer.addMessageItem(messageInfo);
          model.getMessagecontainerInfos().add(newMessageContainer);
          Collections
              .sort(getModel().getMessagecontainerInfos(), new Comparator<MessagecontainerInfo>() {
                @Override public int compare(MessagecontainerInfo o1, MessagecontainerInfo o2) {
                  return o2.getMessage().get(0).getCreationtime().compareTo(o1.getMessage().get(0).getCreationtime());
                }
              });
          saveModel("Added new chatmessage to existing chat from " + from.toString());


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


    if (paRootPane.getCenter() == null)
      stepToPane(Pages.PLANNING);
  }
}
