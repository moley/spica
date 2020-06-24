package org.spica.fx.renderer;

import javafx.scene.control.ListCell;
import org.spica.javaclient.model.MessagecontainerInfo;

public class MessageContainerInfoCellFactory extends ListCell<MessagecontainerInfo> {

  @Override protected void updateItem(MessagecontainerInfo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {
      setText(item.getTopic());
    }
  }
}
