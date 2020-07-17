package org.spica.fx.renderer;

import java.time.format.DateTimeFormatter;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.fx.Consts;
import org.spica.fx.UiUtils;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;

public class MessageInfoCellFactory extends ListCell<MessageInfo> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageInfoCellFactory.class);


  private Model model;

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

  public MessageInfoCellFactory (final Model model) {
    this.model = model;

  }

  @Override protected void updateItem(MessageInfo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {
      Document document = Jsoup.parseBodyFragment(item.getMessage());
      Elements ps = document.select("p");

      Node node;

      if (item.getMessage().contains("<html")) {
        WebView webView = new WebView();
        webView.getEngine().loadContent(item.getMessage());
        node = webView;
      }
      else {
        Label label = new Label();
        label.setText(item.getMessage());
        node = label;
      }

      HBox.setHgrow(node, Priority.ALWAYS);

//      bubbledLabel.setText(multilineString);

      //      bubbledLabel.setText(item.getMessage());


      HBox hBox = new HBox(10);


      //WebView webView = new WebView();
      //webView.getEngine().loadContent(item.getMessage());

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

      if (isFromMe) {
        node.getStyleClass().setAll("chat-bubble-me");
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(node);
        hBox.getChildren().add(userAndTime);
      }
      else {
        node.getStyleClass().setAll("chat-bubble-foreign");
        hBox.getChildren().add(userAndTime);
        hBox.getChildren().add(node);
        hBox.setAlignment(Pos.CENTER_LEFT);
      }
      setGraphic(hBox);

    }
  }
}
