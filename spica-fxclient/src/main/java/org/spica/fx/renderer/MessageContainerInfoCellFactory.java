package org.spica.fx.renderer;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import org.spica.fx.Consts;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;

public class MessageContainerInfoCellFactory extends ListCell<MessagecontainerInfo> {

  @Override protected void updateItem(MessagecontainerInfo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {
      Label lblText = new Label();


      Node icon = null;


      if (item.getMessage().size() > 0) {
        MessageInfo firstMessage = item.getMessage().get(0);
        if (firstMessage.getType().equals(MessageType.MAIL))
          icon = Consts.createIcon("fa-envelope", Consts.ICON_SIZE_TOOLBAR);
        else if (firstMessage.getType().equals(MessageType.PHONECALL))
          icon = Consts.createIcon("fa-phone", Consts.ICON_SIZE_TOOLBAR);
        else if (firstMessage.getType().equals(MessageType.CHAT))
          icon = Consts.createIcon("fa-comments", Consts.ICON_SIZE_TOOLBAR);
        else
          throw new IllegalStateException("Unknown message type " + firstMessage.getType() + " in container " + item.getTopic());
      }

      lblText.setGraphic(icon);
      lblText.setText(item.getTopic());
      for (MessageInfo next: item.getMessage()) {
        if (next.getReadtime() == null) {
          lblText.setStyle("-fx-font-weight: bold");
          break;
        }
      }
      setGraphic(lblText);
      setText(null);
    }
  }
}
