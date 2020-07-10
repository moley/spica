package org.spica.fx.renderer;

import javafx.util.StringConverter;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TaskInfo;

public class TaskInfoStringConverter extends StringConverter<TaskInfo> {

  @Override public String toString(TaskInfo scopItem) {
    if (scopItem == null){
      return null;
    } else {
      return scopItem.getName();
    }
  }

  @Override public TaskInfo fromString(String string) {
    return null;
  }
}
