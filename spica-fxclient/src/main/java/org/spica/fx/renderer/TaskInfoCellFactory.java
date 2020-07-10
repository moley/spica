package org.spica.fx.renderer;

import javafx.scene.control.ListCell;
import org.spica.javaclient.model.TaskInfo;

public class TaskInfoCellFactory extends ListCell<TaskInfo> {

  @Override protected void updateItem(TaskInfo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    } else {
      setText(item.getName());

    }
  }
}