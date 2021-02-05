package org.spica.fx.renderer;

import java.time.format.DateTimeFormatter;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.filestore.FilestoreItem;
import org.spica.commons.filestore.FilestoreService;
import org.spica.fx.Consts;
import org.spica.fx.logic.FileStoreNavigator;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;

@Slf4j
public class MessageInfoCellFactory extends ListCell<MessageInfo> {

  private FilestoreService filestoreService;
  private Model model;

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

  public MessageInfoCellFactory (final FilestoreService filestoreService, final Model model) {
    this.filestoreService = filestoreService;
    this.model = model;
  }

  @Override protected void updateItem(MessageInfo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null || (item.getMessage() == null && item.getDocuments() == null)) {
      setText(null);
      setGraphic(null);
    }
    else {
      Node contentNode;

      if (item.getMessage() != null && item.getMessage().contains("<html")) {
        WebView webView = new WebView();
        webView.getEngine().loadContent(item.getMessage());
        webView.maxWidthProperty().bind(widthProperty().subtract(20));
        webView.minWidthProperty().bind(widthProperty().subtract(20));
        contentNode = webView;
      }
      else {
        Label label = new Label();
        label.maxWidthProperty().bind(widthProperty().subtract(20));
        label.minWidthProperty().bind(widthProperty().subtract(20));
        label.setText(item.getMessage() != null ? item.getMessage(): "");
        contentNode = label;
      }

      HBox.setHgrow(contentNode, Priority.ALWAYS);


      UserInfo userInfo = item.getCreatorId() != null ? model.findUserById (item.getCreatorId()) : null;

      boolean isFromMe = userInfo != null && model.isMe(userInfo);

      String username = userInfo != null ? userInfo.getDisplayname() : item.getCreatorMailadresse();
      if (isFromMe)
        username = "Me";
      Node icon = (userInfo != null && userInfo.getAvatar() != null) ? new ImageView(Consts.createImage(userInfo.getAvatar(), Consts.ICON_SIZE_MEDIUM)) : Consts.createIcon("fa-user", Consts.ICON_SIZE_MEDIUM);

      VBox userAndTime = new VBox();
      userAndTime.getChildren().add(new Label(username, icon));
      Label chatTimeLabel = new Label(formatter.format(item.getCreationtime().toLocalTime()));
      chatTimeLabel.getStyleClass().setAll("chat-time");
      userAndTime.getChildren().add(chatTimeLabel);

      VBox vbox = new VBox(5);
      if (isFromMe) {
        contentNode.getStyleClass().setAll("chat-bubble-me");
        userAndTime.setAlignment(Pos.CENTER_RIGHT);
      }
      else {
        contentNode.getStyleClass().setAll("chat-bubble-foreign");
        userAndTime.setAlignment(Pos.CENTER_LEFT);
      }

      vbox.getChildren().add(userAndTime);
      vbox.getChildren().add(contentNode);

      if (item.getDocuments() != null) {
        for (String next : item.getDocuments()) {
          Button button = new Button(next);
          vbox.getChildren().add(button);
          button.setOnAction(event -> {
            log.info("setOnAction on document button " + next);
            FilestoreItem filestoreItem = filestoreService.item(next);

            FileStoreNavigator fileStoreNavigator = new FileStoreNavigator();
            fileStoreNavigator.open(filestoreItem);
          });
        }
      }

      setGraphic(vbox);

    }
  }
}
