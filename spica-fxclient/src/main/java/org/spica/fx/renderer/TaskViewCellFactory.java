package org.spica.fx.renderer;

import javafx.scene.control.ListCell;
import org.spica.fx.filter.TaskView;

public class TaskViewCellFactory extends ListCell<TaskView> {

  @Override protected void updateItem(TaskView item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {
      setText(item.getName() + " (" + item.getNumberOfTasks() + ")");
    }
  }
}