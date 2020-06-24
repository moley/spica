package org.spica.fx.filter;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.spica.javaclient.model.TaskInfo;

public abstract class AbstractTaskView implements TaskView {

  private final List<TaskInfo> taskInfos = FXCollections.observableArrayList();

  public List<TaskInfo> getTaskInfos() {
    return taskInfos;
  }

  public int getNumberOfTasks () {
    return taskInfos.size();
  }
}
