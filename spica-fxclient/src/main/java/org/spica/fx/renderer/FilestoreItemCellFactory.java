package org.spica.fx.renderer;

import javafx.scene.control.ListCell;
import org.spica.commons.filestore.FilestoreItem;

public class FilestoreItemCellFactory extends ListCell<FilestoreItem> {

  @Override protected void updateItem(FilestoreItem item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    } else {
      setText(item.getName());
    }
  }
}
