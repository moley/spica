package org.spica.fx.renderer;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.spica.fx.Consts;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.utils.DateUtil;

@Slf4j
public class MessageContainerInfoCellFactory extends ListCell<MessagecontainerInfo> {

  private Model model;
  public MessageContainerInfoCellFactory (final Model model) {
    this.model = model;
  }

  private DateUtil dateUtil = new DateUtil();

  @Override protected void updateItem(MessagecontainerInfo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {

      HBox hbox = new HBox(10);
      Label lblType = new Label();
      lblType.setMinWidth(50);
      lblType.setMaxWidth(50);
      Label lblDate = new Label();
      lblDate.setMaxWidth(120);
      lblDate.setMinWidth(120);
      Label lblText = new Label();

      ButtonBar bbaUsers = new ButtonBar();
      bbaUsers.setMinWidth(200);
      bbaUsers.setMaxWidth(200);


      Node icon = null;

      String date = "";

      List<UserInfo> userInfos;
      try {
        userInfos = model.getOtherUsers(item);
      } catch (Exception e) {
        log.error("Error get other users of messagecontainer " + item.getId() + ":" + e.getLocalizedMessage(), e);
        userInfos = new ArrayList<>();
      }
      for (UserInfo next: userInfos) {
        Button btnUser = new Button(next.getUsername());
        ButtonBar.setButtonData(btnUser, ButtonBar.ButtonData.LEFT);
        bbaUsers.getButtons().add(btnUser);
      }

      if (item.getMessage().size() > 0) {
        MessageInfo firstMessage = item.getMessage().get(0);
        MessageInfo lastMessage = item.getMessage().get(item.getMessage().size() - 1);
        if (firstMessage.getType() != null) {
          if (firstMessage.getType().equals(MessageType.MAIL))
            icon = Consts.createIcon("fa-envelope", Consts.ICON_SIZE_TOOLBAR);
          else if (firstMessage.getType().equals(MessageType.PHONECALL))
            icon = Consts.createIcon("fa-phone", Consts.ICON_SIZE_TOOLBAR);
          else if (firstMessage.getType().equals(MessageType.CHAT))
            icon = Consts.createIcon("fa-comments", Consts.ICON_SIZE_TOOLBAR);
          else
            throw new IllegalStateException("Unknown message type " + firstMessage.getType() + " in container " + item.getTopic());
        }
        else
          log.error("Message " + firstMessage.getId() + " does not have a type");

        lblDate.setText(dateUtil.getDateAndTimeAsString(lastMessage.getCreationtime()));
      }

      lblType.setGraphic(icon);
      lblText.setText(item.getTopic());
      for (MessageInfo next: item.getMessage()) {
        if (next.getReadtime() == null) {
          String styleBold = "-fx-font-weight: bold";
          lblText.setStyle(styleBold);
          lblDate.setStyle(styleBold);
          break;
        }
      }

      hbox.getChildren().addAll(lblType, lblDate, bbaUsers, lblText);

      setGraphic(hbox);
      setText(null);
    }
  }
}
